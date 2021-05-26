package com.app.sammy.videoselect;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.sammy.Api.Api;
import com.app.sammy.Models.Responce;
import com.app.sammy.PlayerActivity.PlayerActivity;
import com.app.sammy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoSelect extends AppCompatActivity implements Contract {
    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView;
    @BindView(R.id.button_upload)
    Button button_upload;
    @BindView(R.id.button_link)
    Button button_link;

    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_select);
        ButterKnife.bind(this);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar2));
        getSupportActionBar().setTitle("Select Media");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpRecycler();


        button_upload.setOnClickListener(v -> {
            Toast.makeText(this, "Route not working currently", Toast.LENGTH_SHORT).show();

        });
        button_link.setOnClickListener(v -> {
            showLinkDialogue();
        });
    }

    @Override
    public void uploadData(String media_url) {
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

    private void setUpRecycler() {
        List<SelectorModel> list = new ArrayList<>();

        list.add(new SelectorModel("Mask",
                "https://raw.githubusercontent.com/PlytonRexus/sammy-web/master/videoplayback.mp4",
                " "));
        list.add(new SelectorModel("Lego Man",
                "https://raw.githubusercontent.com/PlytonRexus/sammy-web/master/videoplayback_2.mp4",
                " "));
        list.add(new SelectorModel("Mickey Mouse",
                "https://raw.githubusercontent.com/PlytonRexus/sammy-web/master/videoplayback_3.mp4",
                " "));


        recyclerView.setAdapter(new SelectorAdapter(this,list));
    }
    private void showLinkDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoSelect.this);
        View view = this.getLayoutInflater().inflate(R.layout.dialogue_link, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        EditText textInputLayout = view.findViewById(R.id.editText);
        ImageButton imageButton = view.findViewById(R.id.linkButton);

        view.findViewById(R.id.imageView9).setOnClickListener(view1 -> {
                alertDialog.dismiss();
        });

        imageButton.setOnClickListener(view1 -> {
            String url = textInputLayout.getText().toString().trim();
            if(!url.isEmpty()){
                textInputLayout.setError(null);
                uploadData(url);
                alertDialog.dismiss();
            }
            else{
                textInputLayout.setError("Url cannot be empty");
            }
        });
    }
}