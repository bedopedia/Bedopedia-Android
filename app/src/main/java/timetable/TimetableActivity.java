package timetable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.util.List;


import timetable.Fragments.TomorrowFragment;
import timetable.TimetableAdapter;


/**
 * Created by khaled on 3/1/17.
 */

public class TimetableActivity extends AppCompatActivity {

    private TimetableAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);
        context = this;

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Timetable");
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        List<TimetableSlot> tomorrowSlots = ( List<TimetableSlot> ) bundle.getSerializable(TomorrowFragment.KEY_NAME);
        mSectionsPagerAdapter = new TimetableAdapter(getSupportFragmentManager(), tomorrowSlots);

        mViewPager = (ViewPager) findViewById(R.id.timetable_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.timetable_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


}
