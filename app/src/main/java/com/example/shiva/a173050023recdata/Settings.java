package com.example.shiva.a173050023recdata;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Settings extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).loadSettingsFrag();
    }

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        rootView =  inflater.inflate(R.layout.activity_settings, container, false);
        final CheckBox acc = rootView.findViewById(R.id.checkbox_accelerometer);
        final CheckBox gps = rootView.findViewById(R.id.checkbox_GPS);
        acc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("my_pref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("acc", acc.isChecked());
                editor.commit();
            }
        });
        gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("my_pref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("gps", gps.isChecked());
                editor.commit();
            }
        });
        return rootView;
    }


    public boolean[] getOptions(){
        CheckBox acc = rootView.findViewById(R.id.checkbox_accelerometer);
        CheckBox gps = rootView.findViewById(R.id.checkbox_GPS);
        return new boolean[]{acc.isChecked(), gps.isChecked()};
    }

    public void  setOptions(boolean[] options){
        CheckBox acc = rootView.findViewById(R.id.checkbox_accelerometer);
        CheckBox gps = rootView.findViewById(R.id.checkbox_GPS);
        acc.setChecked(options[0]);
        gps.setChecked(options[1]);
    }
}