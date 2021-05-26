package com.app.sammy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.sammy.R;
import com.app.sammy.VideoSelect;

public class FragHome extends Fragment {
    Button button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate( R.layout.fragment_home, container, false);
        button = v.findViewById( R.id.buttonTest );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getContext(), VideoSelect.class ) );
            }
        } );
        return v;
    }
}
