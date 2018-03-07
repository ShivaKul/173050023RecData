package com.example.shiva.a173050023recdata;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    View rootView;

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).loadLoginFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        rootView =  inflater.inflate(R.layout.activity_login_tab, container, false);
        rootView.findViewById(R.id.male).setOnClickListener(this);
        rootView.findViewById(R.id.female).setOnClickListener(this);
        rootView.findViewById(R.id.others).setOnClickListener(this);



        return rootView;
    }

    public String[] getTextViewText(){
        EditText fname =  rootView.findViewById(R.id.first_name);
        EditText lname =  rootView.findViewById(R.id.last_name);
        EditText contact =  rootView.findViewById(R.id.contact);
        EditText email =  rootView.findViewById(R.id.email);
        EditText age =  rootView.findViewById(R.id.age);
        String[] texts = {fname.getText().toString(), lname.getText().toString(), contact.getText().toString(), email.getText().toString(), age.getText().toString()};
        return texts;
    }

    public int getRadioClicked(){
        RadioGroup radioGroup =  rootView.findViewById(R.id.radio_gender);
        return radioGroup.getCheckedRadioButtonId();
    }

    public void setRadioClicked(int id){
        if(id <= -1)
            id = 0;
        RadioGroup radioGroup =  rootView.findViewById(R.id.radio_gender);
        radioGroup.check(id);
    }

    public void setTextViewText(String[] texts){
        EditText fname =  rootView.findViewById(R.id.first_name);
        EditText lname =  rootView.findViewById(R.id.last_name);
        EditText contact =  rootView.findViewById(R.id.contact);
        EditText email =  rootView.findViewById(R.id.email);
        EditText age =  rootView.findViewById(R.id.age);
        fname.setText(texts[0]);
        lname.setText(texts[1]);
        contact.setText(texts[2]);
        email.setText(texts[3]);
        age.setText(texts[4]);
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