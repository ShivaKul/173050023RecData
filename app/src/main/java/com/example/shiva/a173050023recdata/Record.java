package com.example.shiva.a173050023recdata;

import android.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class Record extends Fragment {

    View rootView;
    ArrayList<String> listTimeStamps = new ArrayList<>();
    ListView listView = null;

    public void setRecordLabelChecked(Boolean checked, String label)
    {
        Switch recordToggle = rootView.findViewById(R.id.switch_toggle);
        recordToggle.setChecked(checked);
        EditText labelField = rootView.findViewById(R.id.labelField);
        labelField.setText(label);
    }
    private boolean checkWriteExternalPermission()
    {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = getContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    private boolean checkFineLocationPermission()
    {
        String permission = android.Manifest.permission.ACCESS_FINE_LOCATION;
        int res = getContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    private boolean checkCoarseLocationPermission()
    {
        String permission = android.Manifest.permission.ACCESS_COARSE_LOCATION;
        int res = getContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        rootView =  inflater.inflate(R.layout.activity_record, container, false);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), simple_list_item_1, listTimeStamps);
        listView = (ListView) rootView.findViewById(R.id.time_stamps);
        listView.setAdapter(adapter);

        Switch switch_r= (Switch) rootView.findViewById(R.id.switch_toggle);
        switch_r.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(getActivity(), RecordReadings.class );
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("my_pref", 0);
                EditText labelField = rootView.findViewById(R.id.labelField);
                if (isChecked) {
                    if(labelField.getText().toString().length() == 0)
                    {
                        Toast.makeText(getActivity(), "Please enter a label.", Toast.LENGTH_SHORT).show();
                        Switch toggle = rootView.findViewById(R.id.switch_toggle);
                        toggle.setChecked(false);
                        return;
                    }
                    if(!checkCoarseLocationPermission() || !checkFineLocationPermission() || !checkWriteExternalPermission())
                    {
                        Toast.makeText(getActivity(), "Please grant location and storage permissions.", Toast.LENGTH_SHORT).show();
                        Switch toggle = rootView.findViewById(R.id.switch_toggle);
                        toggle.setChecked(false);
                        return;
                    }
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("recordChecked", true);
                    editor.putString("label", labelField.getText().toString());
                    editor.commit();
                    getActivity().startService(intent);
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    if(listTimeStamps.size() > 4)
                        listTimeStamps.remove(0);
                    listTimeStamps.add(ts);
                }
                else{
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("recordChecked", false);
                    editor.commit();
                    getActivity().stopService(intent);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return rootView;
    }

}