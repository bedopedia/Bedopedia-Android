package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.ui.adapters.WeeklyPlannerAdapter;
import trianglz.ui.fragments.DayFragment;

public class WeeklyPlannerActivity extends AppCompatActivity {
    private SmartTabLayout tabLayout;
    private ViewPager viewPager;
    private WeeklyPlannerAdapter adapter;
    private DayFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_planner);
        bindViews();
    }
    private void bindViews () {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);
        adapter = new WeeklyPlannerAdapter(getSupportFragmentManager(), this);
        adapter.addFragmentArrayList(getFragmentList());
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }
    private ArrayList<DayFragment> getFragmentList(){
        ArrayList<DayFragment> fragmentArrayList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            fragment = new DayFragment();
            fragmentArrayList.add(fragment);
        }
        return fragmentArrayList;
    }


}
