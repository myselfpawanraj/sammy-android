package com.app.sammy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.sammy.Api.Api;
import com.app.sammy.Models.Responce;
import com.app.sammy.PlayerActivity.PlayerActivity;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoSelect extends AppCompatActivity {
    CardView cardView1,cardView2,cardView3;
    ImageButton linkButton, uploadButton;
    TextInputEditText textInputEditText;
    private Uri selectedImage=null;
    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_select);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar2));
        getSupportActionBar().setTitle("Upload Media");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardView1=findViewById(R.id.cardVideo1);
        cardView2=findViewById(R.id.cardVideo2);
        cardView3=findViewById(R.id.cardVideo3);
        linkButton=findViewById(R.id.linkButton);
        //uploadButton=findViewById(R.id.uploadButton);
        textInputEditText= findViewById(R.id.textInput);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string="https://raw.githubusercontent.com/PlytonRexus/sammy-web/master/videoplayback.mp4";
                uploadData(string);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string="https://raw.githubusercontent.com/PlytonRexus/sammy-web/master/videoplayback_2.mp4";
                uploadData(string);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string="https://raw.githubusercontent.com/PlytonRexus/sammy-web/master/videoplayback_3.mp4";
                uploadData(string);
            }
        });
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textInputEditText.getText() == null) return;
                String string=textInputEditText.getText().toString();
                uploadData(string);
            }
        });
//        uploadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openImageChooser();
//            }
//        });
    }
    private void openImageChooser(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    selectedImage = data.getData();
                    uploadData(selectedImage+"");
                }
                break;
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
    private void uploadData(String media_url) {
        initDialog();
        Log.d("URI =  ", media_url + "" );

        Call<Responce> postList;
        postList =  Api.getService().uploadByLink(media_url,true);
        postList.enqueue(new Callback<Responce>() {
            @Override
            public void onResponse(Call<Responce> call, Response<Responce> response) {
                hideDialog();
                Intent i = new Intent(VideoSelect.this, PlayerActivity.class);
                i.putExtra("LINK",media_url);
                i.putExtra("ID1",response.body().getId1());
                i.putExtra("ID2",response.body().getId2());
                startActivity(i);
            }
            @Override
            public void onFailure(Call<Responce> call, Throwable t) {
                hideDialog();
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_LONG).show();
            }
        });
    }
    protected void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected void hideDialog() {
        if (pDialog.isShowing()) pDialog.dismiss();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return super.onSupportNavigateUp();
    }
}