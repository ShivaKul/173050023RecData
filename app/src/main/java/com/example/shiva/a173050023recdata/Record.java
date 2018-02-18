package com.example.shiva.a173050023recdata;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class Record extends Fragment {

    ArrayList<String> listTimeStamps = new ArrayList<>();
    ListView listView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.activity_record, container, false);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), simple_list_item_1, listTimeStamps);
        listView = (ListView) rootView.findViewById(R.id.time_stamps);
        listView.setAdapter(adapter);

        Switch switch_r= (Switch) rootView.findViewById(R.id.switch_toggle);
        switch_r.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    if(listTimeStamps.size() > 4)
                        listTimeStamps.remove(0);
                    listTimeStamps.add(ts);
                }
                else{
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return rootView;
    }

}