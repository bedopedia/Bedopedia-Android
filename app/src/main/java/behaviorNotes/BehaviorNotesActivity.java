package behaviorNotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.io.Serializable;
import java.util.List;

import attendance.AttendanceFragment;
import behaviorNotes.Adapters.BehaviorNotesFragmentAdapter;
import behaviorNotes.Fragments.BehaviourNotesFragment;
import behaviorNotes.Fragments.NegativeFragment;
import behaviorNotes.Fragments.PositiveFragment;

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

        Bundle extras = getIntent().getExtras();
        studentId = extras.getString("student_id");


        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle("Behaviour notes");

        context = this;




        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        android.support.v4.app.Fragment f = BehaviourNotesFragment.newInstance(studentId ,(Serializable)extras.getSerializable(PositiveFragment.KEY_NAME) ,(Serializable)extras.getSerializable(PositiveFragment.KEY_NAME) );
        ft.add(R.id.behaviourContainer, f);
        ft.commit();





    }



}
