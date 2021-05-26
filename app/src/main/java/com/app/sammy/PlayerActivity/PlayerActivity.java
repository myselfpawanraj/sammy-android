package com.app.sammy.PlayerActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sammy.Api.Api;
import com.app.sammy.Models.AudioRequest.AudIdReq;
import com.app.sammy.Models.AudioRequest.Word;
import com.app.sammy.Models.Finalrequest.Caption;
import com.app.sammy.Models.Finalrequest.IdReq;
import com.app.sammy.Models.TimeStamps;
import com.app.sammy.Models.sceneDes;
import com.app.sammy.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.StrictMath.max;

public class PlayerActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, Contract.View {
    @BindView(R.id.video_view)
    PlayerView playerView;
    @BindView(R.id.imageButton)
    Button imageButton;
    @BindView(R.id.sceneButton)
    Button sceneButton;
    @BindView(R.id.textViewSubtitles)
    TextView textViewSubtitles;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.switch1)
    SwitchMaterial aSwitch;

    Presenter presenter;
    TextToSpeech textToSpeech;
    SimpleExoPlayer player;
    String media_url = null, media_id = null, media_id2 = null;
    List< TimeStamps > list, caption;
    IdReq idReq;
    AudIdReq audIdReq;
    ProgressDialog pDialog;
    int percentAudio, percentVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);

        media_url = getIntent().getStringExtra("LINK");
        media_id = getIntent().getStringExtra("ID1");
        media_id2 = getIntent().getStringExtra("ID2");
        presenter = new Presenter(this);

        initDialog();
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Uri.parse(media_url).getLastPathSegment());
        ButterKnife.bind(this);

        //For fetching Data
        presenter.getVideo(media_id);
        presenter.getAudioData(media_id2);

        imageButton.setOnClickListener(v -> getSpeechInput());
        sceneButton.setOnClickListener(v -> {
            pDialog = new ProgressDialog(this);
            showDialog();
            player.pause();
            presenter.getSceneSearch(player.getCurrentPosition(), media_url, idReq);
        });


        textToSpeech = new TextToSpeech(this, this);

        //Declare the timer
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                speakCaptions();
            }
        }, 0, 1000);
    }

    private void initializePlayer() {
        if (player == null)
            player = new SimpleExoPlayer.Builder(PlayerActivity.this).build();
        player.setThrowsWhenUsingWrongThread(false);
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(media_url);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
    }

    private void fillData() {
        list = new ArrayList<>();
        caption = new ArrayList<>();
        List< Word > wordList = audIdReq.getAudResponseFinal().getWords();
        List< Caption > captionList = idReq.getResponseFinal().getCaptions();

        if (wordList != null)
            for (Word word : wordList) {
                list.add(new TimeStamps(word.getWord(), word.getTime()));
            }

        if (captionList != null)
            for (Caption c : captionList) {
                list.add(new TimeStamps(c.getOcr(), c.getTime()));
                caption.add(new TimeStamps(c.getCaptions(), c.getTime()));
                for (String s : c.getTags())
                    list.add(new TimeStamps(s, c.getTime()));
            }
    }

    private void speakCaptions() {
        if (player == null || !player.isPlaying() || aSwitch == null || !aSwitch.isChecked())
            return;
        for (TimeStamps t : caption) {
            if (player.getContentPosition() >= t.getTime() && (player.getContentPosition() - 1000) <= t.getTime()) {
                textViewSubtitles.setText(t.getTag());
                if (t.getTag().toLowerCase().contains("text") || t.getTag().toLowerCase().contains("logo")) {
                    List< Caption > captionList = idReq.getResponseFinal().getCaptions();
                    for (Caption c : captionList) {
                        if (c.getTime().equals(t.getTime())) {
                            textToSpeech(c.getOcr());
                            break;
                        }
                    }
                } else {
                    textToSpeech(t.getTag());
                }
            }
        }
    }


    //For voice input..
    @SuppressLint("QueryPermissionsNeeded")
    public void getSpeechInput() {
        player.pause();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopUp(List< String > timeList) {

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(PlayerActivity.this, imageButton);
        //Inflating the Popup using xml file
        //popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        for (String s : timeList)
            popup.getMenu().add(s).setIcon(R.drawable.ic_baseline_access_time_24);
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            String time = item.getTitle().toString();
            long hrs = Integer.parseInt(time.substring(0, 2));
            long min = Integer.parseInt(time.substring(3, 5));
            long sec = Integer.parseInt(time.substring(6, 8));
            long millis = (hrs * 3600 + min * 60 + sec) * 1000;
            Toast.makeText(PlayerActivity.this, "Seek to : " + time, Toast.LENGTH_SHORT).show();
            player.seekTo(max(millis - 1000, 0));
            player.play();
            return true;
        });

        popup.show();//showing popup menu
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List< String > timeList = new ArrayList<>();

        if (requestCode == 10) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList< String > result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String search = result.get(0);

                for (int i = 0; i < list.size(); i++) {
                    if (search.toLowerCase().trim().contains(list.get(i).getTag().toLowerCase())) {
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
            Toast.makeText(PlayerActivity.this, "No record found!", Toast.LENGTH_SHORT).show();
        }
        showPopUp(timeList);
    }

    //For Loading Screen ..
    protected void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected void showDialog() {
        pDialog.setTitle(null);
        pDialog.setMessage("Loading...");
        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }


    //Text_to_Speech
    private void textToSpeech(String text) {
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
        if (text == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params, "ID");
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return super.onNavigateUp();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "This Language is not supported");
            } else {
                Log.e("TextToSpeech", "Success.!");
            }
            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    player.pause();
                }

                @Override
                public void onDone(String utteranceId) {
                    player.play();
                }

                @Override
                public void onError(String utteranceId) {

                }
            });
        } else {
            Log.e("TextToSpeech", "Failed to Initialize");
        }
    }

    @Override
    public void onError(String body) {
        Toast.makeText(this, body, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onVideoSuccess(IdReq body) {
        percentVideo = body.getProgress();
        updateDialogue();
        if (body.getProgress() == 100) {
            idReq = body;
        } else {
            presenter.getVideo(media_id);
        }
        if (idReq != null && audIdReq != null) {
            hideDialog();
            initializePlayer();
            fillData();
        }
    }

    @Override
    public void onAudioSuccess(AudIdReq body) {
        percentAudio = body.getProgress();
        updateDialogue();
        if (body.getProgress() == 100) {
            audIdReq = body;
        } else {
            presenter.getAudioData(media_id2);
        }
        if (idReq != null && audIdReq != null) {
            hideDialog();
            initializePlayer();
            fillData();
        }
    }

    @Override
    public void onSceneSuccess(String body) {
        hideDialog();
        textToSpeech(body);
    }

    private void updateDialogue() {
        pDialog.setMessage("Video route working... " + percentVideo + "%\nAudio route working... " + percentAudio + "%");
    }
}