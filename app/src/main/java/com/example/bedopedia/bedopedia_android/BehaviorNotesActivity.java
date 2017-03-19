package com.example.bedopedia.bedopedia_android;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import Adapters.BehaviorNotesFragmentAdapter;

/**
 * Created by khaled on 2/27/17.
 */

public class BehaviorNotesActivity extends AppCompatActivity {
    String studentId, id;
    public static Context context;
    TabLayout tabLayout;
    private BehaviorNotesFragmentAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    TextView positiveTitle;
    TextView positiveCounter;
    TextView negativeTitle;
    TextView negativeCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.behavior_notes);

        Bundle extras= getIntent().getExtras();
        studentId = extras.getString("student_id");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Behavior notes");
        actionBarTitle.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        context = this;

        mSectionsPagerAdapter = new BehaviorNotesFragmentAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.behavior_notes_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.behavior_notes_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        View positiveTab = mSectionsPagerAdapter.getTabView(0);
        View negativeTab = mSectionsPagerAdapter.getTabView(1);
        tabLayout.getTabAt(0).setCustomView(positiveTab);
        tabLayout.getTabAt(1).setCustomView(negativeTab);

        positiveTitle = (TextView) positiveTab.findViewById(R.id.tab_title);
        positiveCounter = (TextView) positiveTab.findViewById(R.id.tab_counter);

        negativeTitle = (TextView) negativeTab.findViewById(R.id.tab_title);
        negativeCounter = (TextView) negativeTab.findViewById(R.id.tab_counter);

        positiveTitle.setTextColor(Color.parseColor("#ffffff"));
        positiveCounter.setBackgroundResource(R.drawable.notes_selected_counter);

        negativeTitle.setTextColor(Color.parseColor("#b3ffffff"));
        negativeCounter.setBackgroundResource(R.drawable.notes_unselected_counter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                if(position == 0){
                    positiveTitle.setTextColor(Color.parseColor("#ffffff"));
                    positiveCounter.setBackgroundResource(R.drawable.notes_selected_counter);

                    negativeTitle.setTextColor(Color.parseColor("#b3ffffff"));
                    negativeCounter.setBackgroundResource(R.drawable.notes_unselected_counter);
                } else{
                    positiveTitle.setTextColor(Color.parseColor("#b3ffffff"));
                    positiveCounter.setBackgroundResource(R.drawable.notes_unselected_counter);

                    negativeTitle.setTextColor(Color.parseColor("#ffffff"));
                    negativeCounter.setBackgroundResource(R.drawable.notes_selected_counter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

}
