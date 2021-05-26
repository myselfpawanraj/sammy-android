package com.app.sammy.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.sammy.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import static android.content.Context.MODE_PRIVATE;

public class FragSettings extends Fragment {
    SwitchMaterial aSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_dummy, container, false);
        aSwitch = v.findViewById(R.id.switch2);
        setChecked();
        aSwitch.setOnClickListener(v1 -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(String.valueOf(R.string.app_name),MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putBoolean(String.valueOf(R.string.scene_describer_volume), aSwitch.isChecked());
            myEdit.apply();
        });
        return v;
    }

    private void setChecked() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(String.valueOf(R.string.app_name),MODE_PRIVATE);
        boolean aBoolean = sharedPreferences.getBoolean(String.valueOf(R.string.scene_describer_volume), true);
        aSwitch.setChecked(aBoolean);
    }
}
