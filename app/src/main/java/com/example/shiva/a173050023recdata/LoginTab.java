package com.example.shiva.a173050023recdata;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginTab extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.activity_login_tab, container, false);
        rootView.findViewById(R.id.male).setOnClickListener(this);
        rootView.findViewById(R.id.female).setOnClickListener(this);
        rootView.findViewById(R.id.others).setOnClickListener(this);
        return rootView;
    }

    public void onClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.male:
                if (checked)
                    // male
                    break;
            case R.id.female:
                if (checked)
                    // female
                    break;
            case R.id.others:
                if(checked)
                    // others
                    break;
        }
    }



}