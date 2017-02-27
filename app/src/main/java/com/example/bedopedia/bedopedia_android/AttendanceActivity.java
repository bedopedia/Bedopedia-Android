package com.example.bedopedia.bedopedia_android;

import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Adapters.AbsentLateAdapter;
import Adapters.AttendanceAdapter;
import Adapters.MyKidsAdapter;

/**
 * Created by khaled on 2/21/17.
 */


public class AttendanceActivity extends AppCompatActivity {

    private AttendanceAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String attendance;

    public static List<Date> absentDates = new ArrayList<Date>();
    public static List<Date> lateDates = new ArrayList<Date>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Attendance");
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        Bundle extras= getIntent().getExtras();

        attendance = extras.getString("attendances");
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(attendance);
        final JsonArray attenobdances = tradeElement.getAsJsonArray();
        lateDates.clear();
        absentDates.clear();

        for(JsonElement element: attenobdances){
            JsonObject day = element.getAsJsonObject();
            Date date = new Date();
            date.setTime(day.get("date").getAsLong());
            if(day.get("status").getAsString().equals("late")){
                lateDates.add(date);
            }
            else if(day.get("status").getAsString().equals("absent")){
                absentDates.add(date);
            }
        }

        mSectionsPagerAdapter = new AttendanceAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }
}
