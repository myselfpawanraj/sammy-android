package com.app.sammy.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.sammy.R;

public class FragAbout extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_about, container, false);
        v.findViewById(R.id.textView6).setOnClickListener(listener);
        v.findViewById(R.id.textView9).setOnClickListener(listener);
        v.findViewById(R.id.textView10).setOnClickListener(listener);
        return v;
    }

    View.OnClickListener listener = v -> {
        String url = "";
        if(v.getId() == R.id.textView6){
            url = "https://github.com/myselfpawanraj/sammy-android";
        }
        if(v.getId() == R.id.textView9){
            url = "https://github.com/PlytonRexus/sammy-node-audio";
        }
        if(v.getId() == R.id.textView10){
            url = "https://github.com/PlytonRexus/sammy-node";
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
    };
}
