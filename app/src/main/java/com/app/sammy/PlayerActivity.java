package com.app.sammy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sammy.Api.Api;
import com.app.sammy.Api.ApiAudio;
import com.app.sammy.Models.AudioRequest.AudIdReq;
import com.app.sammy.Models.AudioRequest.Word;
import com.app.sammy.Models.Finalrequest.Caption;
import com.app.sammy.Models.Finalrequest.IdReq;
import com.app.sammy.Models.Responce;
import com.app.sammy.Models.TimeStamps;
import com.app.sammy.Models.sceneDes;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.StrictMath.max;

public class PlayerActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    PlayerView playerView;
    SimpleExoPlayer player;
    ImageButton imageButton,sceneButton;
    TextView textViewSubtitles;
    TextToSpeech textToSpeech;
    String media_url = null ,media_id = null ,media_id2 = null;
    List<TimeStamps> list, caption;
    IdReq idReq;
    AudIdReq audIdReq;
    ProgressDialog pDialog;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_player );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );

        media_url = getIntent().getStringExtra("LINK");
        media_id = getIntent().getStringExtra("ID1");
        media_id2 = getIntent().getStringExtra("ID2");

        playerView = findViewById( R.id.video_view );
        imageButton = findViewById( R.id.imageButton );
        sceneButton = findViewById( R.id.sceneButton );
        textViewSubtitles = findViewById( R.id.textViewSubtitles );
        aSwitch= findViewById(R.id.switch1);

        initDialog();
        showpDialog();
        getData();

        imageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput();
            }
        } );
        sceneButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player==null) return;
                showpDialog();
                getSceneSearch(player.getCurrentPosition());
            }
        } );


        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
               if (status == TextToSpeech.SUCCESS){
                   textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                       @Override
                       public void onStart(String utteranceId) {
                           Log.i("XXX", "Sucess");

                       }

                       @Override
                       // this method will always called from a background thread.
                       public void onDone(String utteranceId) {
                           Log.i("XXX", "Sucess");
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   // *** toast will not work if called from a background thread ***
                                   Toast.makeText(PlayerActivity.this,"onDone working.",Toast.LENGTH_LONG).show();
                               }
                           });
                       }

                       @Override
                       public void onError(String utteranceId) {
                            }
                   });
               }
               else{
                   Log.i("XXX", "Failed");
               }
            }
        });
        //Declare the timer
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                speakCaptions();
            }
        }, 0, 1000);
    }

    //For fetching Data
    private void getData() {
        Call<IdReq> call;
        call =  Api.getService().getTimestamps(media_id,"sd");
        call.enqueue(new Callback<IdReq>() {
            @Override
            public void onResponse(Call<IdReq> call, Response<IdReq> response) {
                if(response.isSuccessful() && response!=null &&response.body().getProgress()==100){
                    idReq=response.body();
                    getAudio();
                }
                else{
                    Toast.makeText( PlayerActivity.this, "Doing video! " +response.body().getProgress()+"% Completed",Toast.LENGTH_SHORT ).show();
                    getData();
                }
            }

            @Override
            public void onFailure(Call<IdReq> call, Throwable t) {
                Toast.makeText( PlayerActivity.this, "Failed! Retrying.." ,Toast.LENGTH_SHORT ).show();
                getData();
            }
        });
    }
    private void getAudio() {
        Call<AudIdReq> call;
        call = ApiAudio.getService().getSubtitles(media_id2);
        call.enqueue(new Callback<AudIdReq>() {
            @Override
            public void onResponse(Call<AudIdReq> call, Response<AudIdReq> response) {
                if(response.isSuccessful() && response!=null &&response.body().getProgress()==100){
                    audIdReq=response.body();
                    hidepDialog();
                    initializePlayer();
                    fillData();
                }
                else{
                    Toast.makeText( PlayerActivity.this, "Doing audio! " +response.body().getProgress()+"% Completed",Toast.LENGTH_SHORT ).show();
                    getAudio();
                }
            }

            @Override
            public void onFailure(Call<AudIdReq> call, Throwable t) {
                Toast.makeText( PlayerActivity.this, "Failed! Retrying.." ,Toast.LENGTH_SHORT ).show();
                getAudio();
            }
        });
    }
    private void getSceneSearch(long time) {
        Call< sceneDes > call;
        call = Api.getService().getSceneDescribed(media_url,(time/1000),"true");
        call.enqueue(new Callback<sceneDes>() {
            @Override
            public void onResponse(Call<sceneDes> call, Response<sceneDes> response) {
                if(response.isSuccessful() && response!=null){
                    String s = response.body().getCaption();
                    hidepDialog();
                    if(s.toLowerCase().contains("text") || s.toLowerCase().contains("logo") ){
                        List< Caption > captionList = idReq.getResponseFinal().getCaptions();
                        for( Caption c : captionList){
                            if(c.getTime()==time){
                                texttoSpeak(c.getOcr());
                                break;
                            }
                        }
                    }
                    else
                        texttoSpeak(s);
                }
                else{
                    getSceneSearch(time);
                }
            }

            @Override
            public void onFailure(Call<sceneDes> call, Throwable t) {
                Toast.makeText( PlayerActivity.this, "Failed! Retrying.." ,Toast.LENGTH_SHORT ).show();
                getSceneSearch(time);
            }
        });
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder( PlayerActivity.this ).build();
        MediaItem mediaItem = MediaItem.fromUri( media_url );
        player.setMediaItem( mediaItem );
        player.setPlayWhenReady(true);
        player.prepare();
        playerView.setPlayer( player );
        getSupportActionBar().setTitle( Uri.parse( media_url ).getLastPathSegment().toString() );
    }
    private void fillData() {
        list = new ArrayList<>();
        caption = new ArrayList<>();
        List< Word > wordList = audIdReq.getAudResponseFinal().getWords();
        List< Caption > captionList = idReq.getResponseFinal().getCaptions();
        for( Word word : wordList){
            list.add( new TimeStamps(word.getWord(),word.getTime()));
        }
        for( Caption c : captionList){
            list.add(new TimeStamps(c.getOcr(),c.getTime()));
            for(String s: c.getTags())
                list.add(new TimeStamps(s,c.getTime()));
        }
        for( Caption c : captionList){
            caption.add(new TimeStamps(c.getCaptions(),c.getTime()));
        }
    }
    private void speakCaptions(){
        if(player==null || !player.isPlaying() || aSwitch==null ) return;
        for (TimeStamps t : caption) {
            if (player.getContentPosition() >= t.getTime() && (player.getContentPosition() - 1000) <= t.getTime()) {
                textViewSubtitles.setText( t.getTag() );
                if(aSwitch.isChecked()){

                    if(t.getTag().toLowerCase().contains("text") || t.getTag().toLowerCase().contains("logo") ){
                        List< Caption > captionList = idReq.getResponseFinal().getCaptions();
                        for( Caption c : captionList){
                            if(c.getTime()==t.getTime()){
                                texttoSpeak(c.getOcr());
                                break;
                            }
                        }
                    }
                    else
                        texttoSpeak(t.getTag());
                }
            }
        }
    }


    //For voice input..
    public void getSpeechInput() {
        player.pause();
        Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() );

        if (intent.resolveActivity( getPackageManager() ) != null) {
            startActivityForResult( intent, 10 );
        } else {
            Toast.makeText( this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT ).show();
        }
    }
    private void showPopUp(List<String> timeList) {

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu( PlayerActivity.this, imageButton );
        //Inflating the Popup using xml file
        //popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        for (String s : timeList)
            popup.getMenu().add( s ).setIcon( R.drawable.ic_baseline_access_time_24 );
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String time = item.getTitle().toString();
                long hrs = Integer.parseInt( time.substring( 0, 2 ) );
                long min = Integer.parseInt( time.substring( 3, 5 ) );
                long sec = Integer.parseInt( time.substring( 6, 8 ) );
                long millis = (hrs * 3600 + min * 60 + sec) * 1000;
                Toast.makeText( PlayerActivity.this, "Seek to : " + time, Toast.LENGTH_SHORT ).show();
                player.seekTo( max(millis-1000,0) );
                player.play();
                return true;
            }
        } );

        popup.show();//showing popup menu
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        List<String> timeList = new ArrayList<>();

        if (requestCode == 10) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList< String > result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String search = result.get(0);

                for (int i = 0; i < list.size(); i++) {
                    if (search.toLowerCase().trim().contains(list.get(i).getTag().toString().toLowerCase())) {
                        long millis = Integer.parseInt(String.valueOf(list.get(i).getTime()));
                        @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                        timeList.add(hms);
                    }
                }
            }
        }
        if (timeList.isEmpty()) {
            Toast.makeText( PlayerActivity.this, "No record found!", Toast.LENGTH_SHORT ).show();
        }
        showPopUp( timeList );
    }

    //For Loading Screen ..
    protected void initDialog() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
    }
    protected void showpDialog() {

        if (!pDialog.isShowing()) pDialog.show();
    }
    protected void hidepDialog() {

        if (pDialog.isShowing()) pDialog.dismiss();
    }


    //Text_to_Speech
    private void texttoSpeak(String text) {
        if (text==null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak( text, TextToSpeech.QUEUE_FLUSH, null, null );
        } else {
            textToSpeech.speak( text, TextToSpeech.QUEUE_FLUSH, null );
        }
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage( Locale.US );
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e( "error", "This Language is not supported" );
            } else {
                Log.e( "error", "Success.!" );
            }
        } else {
            Log.e( "error", "Failed to Initialize" );
        }
    }
    @Override
    public void onBackPressed() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech=null;
        }
        if (player != null) {
            player.stop();
            player.release();
            player=null;
        }
        super.onBackPressed();
        this.finish();
    }
    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech=null;
        }
        if (player != null) {
            player.stop();
            player.release();
            player=null;
        }
        super.onDestroy();
    }
    @Override
    public void onStop() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech=null;
        }
        if (player != null) {
            player.stop();
            player.release();
            player=null;
        }
        super.onStop();
    }
}