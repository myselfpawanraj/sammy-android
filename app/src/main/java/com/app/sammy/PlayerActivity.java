package com.app.sammy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sammy.Models.TimeStamps;
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

import static java.lang.StrictMath.max;

public class PlayerActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private PlayerView playerView;
    public SimpleExoPlayer player;
    private ImageButton imageButton;
    private TextView textViewSubtitles;
    private TextToSpeech textToSpeech;

    final String media_url = "https://thepaciellogroup.github.io/AT-browser-tests/video/ElephantsDream.mp4";
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    List<TimeStamps> list, caption;
    public int temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_player );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );

        playerView = findViewById( R.id.video_view );
        imageButton = findViewById( R.id.imageButton );
        textViewSubtitles = findViewById( R.id.textViewSubtitles );
        textToSpeech = new TextToSpeech( this, this );

        list = new ArrayList<>();
        caption = new ArrayList<>();
        list.add( new TimeStamps( "demo", "2319" ) );
        list.add( new TimeStamps( "closed", "3119" ) );
        list.add( new TimeStamps( "reyes", "3559" ) );
        list.add( new TimeStamps( "why", "5139" ) );
        list.add( new TimeStamps( "now", "5519" ) );
        list.add( new TimeStamps( "okay", "6000" ) );
        list.add( new TimeStamps( "good", "7759" ) );
        list.add( new TimeStamps( "what", "10059" ) );
        list.add( new TimeStamps( "do", "10199" ) );
        list.add( new TimeStamps( "you", "10199" ) );
        list.add( new TimeStamps( "see", "10199" ) );
        list.add( new TimeStamps( "at", "10199" ) );
        list.add( new TimeStamps( "your", "10699" ) );
        list.add( new TimeStamps( "left", "10859" ) );
        list.add( new TimeStamps( "side", "11239" ) );
        list.add( new TimeStamps( "maillotins", "11500" ) );
        list.add( new TimeStamps( "really", "15880" ) );
        list.add( new TimeStamps( "no", "17500" ) );
        list.add( new TimeStamps( "nothing", "17710" ) );
        list.add( new TimeStamps( "at", "17900" ) );
        list.add( new TimeStamps( "all", "18000" ) );
        list.add( new TimeStamps( "really", "18000" ) );
        list.add( new TimeStamps( "and", "19000" ) );
        list.add( new TimeStamps( "at", "19000" ) );
        list.add( new TimeStamps( "your", "19959" ) );
        list.add( new TimeStamps( "right", "20119" ) );
        list.add( new TimeStamps( "what", "20340" ) );
        list.add( new TimeStamps( "do", "20699" ) );
        list.add( new TimeStamps( "you", "20939" ) );
        list.add( new TimeStamps( "see", "21159" ) );
        list.add( new TimeStamps( "at", "21299" ) );
        list.add( new TimeStamps( "your", "21420" ) );
        list.add( new TimeStamps( "right", "21600" ) );
        list.add( new TimeStamps( "side", "21899" ) );
        list.add( new TimeStamps( "o", "22000" ) );
        list.add( new TimeStamps( "the", "24000" ) );
        list.add( new TimeStamps( "same", "24000" ) );
        list.add( new TimeStamps( "project", "25139" ) );
        list.add( new TimeStamps( "de", "27000" ) );
        list.add( new TimeStamps( "same", "27000" ) );
        list.add( new TimeStamps( "nothing", "28000" ) );
        list.add( new TimeStamps( "great", "29500" ) );


        caption.add( new TimeStamps( "the background is black", "1200" ) );
        caption.add( new TimeStamps( "a man is looking at the camera", "13833" ) );
        caption.add( new TimeStamps( "a man is holding a dog", "15500" ) );
        caption.add( new TimeStamps( "two men are smiling", "20542" ) );
        caption.add( new TimeStamps( "two men are standing", "20583" ) );
        caption.add( new TimeStamps( "a man is standing", "23458" ) );
        caption.add( new TimeStamps( "the background is black", "30132" ) );
        caption.add( new TimeStamps( "celebrating 10 years of oqen movies", "32238" ) );

        initializePlayer();
        imageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput();
            }
        } );
        if (player != null) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate( new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            textViewSubtitles.post( new Runnable() {
                                @Override
                                public void run() {
                                    for (TimeStamps t : caption) {
                                        if (player.getContentPosition() >= Integer.parseInt( t.getTime() ) && (player.getContentPosition() - 1000) <= Integer.parseInt( t.getTime() )) {
                                            textViewSubtitles.setText( t.getTag() );
                                            texttoSpeak( t.getTag() );
                                        }
                                    }
                                }
                            } );
                        }
                    } );
                }
            }, 0, 1000 );
        }
    }

    private void initializePlayer() {
        temp = 0;
        player = new SimpleExoPlayer.Builder( this ).build();
        MediaItem mediaItem = MediaItem.fromUri( media_url );
        player.setMediaItem( mediaItem );
        player.setPlayWhenReady( playWhenReady );
        player.seekTo( currentWindow, playbackPosition );
        player.prepare();
        playerView.setPlayer( player );
        getSupportActionBar().setTitle( Uri.parse( media_url ).getLastPathSegment().toString() );
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        List<String> timeList = new ArrayList<>();

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
                    String search = result.get( 0 );

                    for (int i = 0; i < list.size(); i++) {
                        if (search.toLowerCase().trim().contains( list.get( i ).getTag().toString().toLowerCase() )) {
                            long millis = Integer.parseInt( String.valueOf( list.get( i ).getTime() ) );
                            @SuppressLint("DefaultLocale") String hms = String.format( "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours( millis ),
                                    TimeUnit.MILLISECONDS.toMinutes( millis ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( millis ) ),
                                    TimeUnit.MILLISECONDS.toSeconds( millis ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( millis ) ) );
                            timeList.add( hms );
                        }
                    }
                }
                break;
        }
        if (timeList.isEmpty()) {
            Toast.makeText( PlayerActivity.this, "No record found!", Toast.LENGTH_SHORT ).show();
        }
        showPopUp( timeList );
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

    //Text_to_Speech

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage( Locale.US );
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e( "error", "This Language is not supported" );
            } else {
                texttoSpeak( "" );
            }
        } else {
            Log.e( "error", "Failed to Initialize" );
        }
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void texttoSpeak(String text) {
        if ("".equals( text )) {
            text = "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak( text, TextToSpeech.QUEUE_FLUSH, null, null );
        } else {
            textToSpeech.speak( text, TextToSpeech.QUEUE_FLUSH, null );
        }
        while (textToSpeech.isSpeaking()) {

        }
    }
}