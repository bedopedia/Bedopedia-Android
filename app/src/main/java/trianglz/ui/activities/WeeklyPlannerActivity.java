package trianglz.ui.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

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
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            TextView vgTab = (TextView) vg.getChildAt(j);
            Typeface typeface = ResourcesCompat.getFont(this,R.font.sfui_semibold );
            vgTab.setTypeface(Typeface.DEFAULT);
            vgTab.setTypeface(typeface);
        }
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
