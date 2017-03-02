package com.example.bedopedia.bedopedia_android;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import Adapters.AttendanceAdapter;
import Adapters.TimetableAdapter;


/**
 * Created by khaled on 3/1/17.
 */

public class TimetableActivity extends AppCompatActivity {

    private TimetableAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

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


        mSectionsPagerAdapter = new TimetableAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.timetable_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.timetable_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }
}
