package com.example.shiva.a173050023recdata;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private  int lastTab = 0;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    boolean inputisOkay(){
        EditText contact_no = findViewById(R.id.contact);
        EditText email = findViewById((R.id.email));
        try {
            if (!validate(email.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please enter valid e-mail address.", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (contact_no.length() != 10) {
                Toast.makeText(getApplicationContext(), "Please enter 10 digit contact number.", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!Pattern.matches("[0-9]+", contact_no.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please enter 10 digit contact number.", Toast.LENGTH_SHORT).show();
                return false;
            }
            RadioGroup radioGroup = findViewById(R.id.radio_gender);
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Please choose gender", Toast.LENGTH_SHORT).show();
                return false;
            }
            EditText firstName = findViewById(R.id.first_name);
            EditText lastName = findViewById(R.id.last_name);
            EditText age = findViewById(R.id.age);
            if (firstName.getText().toString().length() == 0 || lastName.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Name can't be left blank.", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!Pattern.matches("[0-9]+", age.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Age must be numeric.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e)
        {

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));
        tabLayout.addTab(tabLayout.newTab().setText("Record"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //viewPager.setPagingEnabled(false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(!inputisOkay())
                {
                    viewPager.setCurrentItem(0);
                    tabLayout.getTabAt(0).select();
                }
                else
                    tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if((lastTab != 0) || inputisOkay())
                    viewPager.setCurrentItem(tab.getPosition());
                else
                {
                    tab = tabLayout.getTabAt(lastTab);
                    tab.select();
                }
                lastTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }
    public void goToRecord(View view){
        if(inputisOkay())
        {
            CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.pager);
            viewPager.setCurrentItem(2);
        }
    }
}