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
import java.util.HashMap;

import trianglz.models.DailyNote;
import trianglz.models.RootClass;
import trianglz.ui.adapters.WeeklyPlannerAdapter;
import trianglz.ui.fragments.DayFragment;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class WeeklyPlannerActivity extends AppCompatActivity {
    private SmartTabLayout tabLayout;
    private ViewPager viewPager;
    private WeeklyPlannerAdapter adapter;
    private DayFragment fragment;
    private RootClass rootClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_planner);
        getValueFromIntent();
        bindViews();
    }

    private void getValueFromIntent() {
        rootClass = (RootClass) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_WEEKLY_PLANER);
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



    private  HashMap<String,ArrayList<DailyNote>>  getDaysOfDailyNotes(RootClass rootClass){
        ArrayList<DailyNote> dailyNoteArrayList = rootClass.getWeeklyPlans().get(0).getDailyNotes();
        HashMap<String,ArrayList<DailyNote>> dailyNoteHashMap= new HashMap<>();
        for(int i = 0 ; i < dailyNoteArrayList.size(); i++){
            DailyNote dailyNote = dailyNoteArrayList.get(i);
            String dayName = Util.getDayName(dailyNote.getDate());
            if(! dayName.isEmpty()){
                if(dailyNoteHashMap.containsKey(dayName)){
                    ArrayList<DailyNote> dailyNotes = dailyNoteHashMap.get(dayName);
                    dailyNotes.add(dailyNote);
                    dailyNoteHashMap.put(dayName,dailyNotes);
                }else {
                    ArrayList<DailyNote> dailyNotes = new ArrayList<>();
                    dailyNotes.add(dailyNote);
                    dailyNoteHashMap.put(dayName,dailyNotes);
                }

            }

        }
        return dailyNoteHashMap;
    }


}
