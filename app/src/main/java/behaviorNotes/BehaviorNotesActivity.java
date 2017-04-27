package behaviorNotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

import Tools.FragmentUtils;
import behaviorNotes.Adapters.BehaviorNotesFragmentAdapter;
import behaviorNotes.Fragments.NegativeFragment;
import behaviorNotes.Fragments.PositiveFragment;
import login.ForgetPasswordFRagment;

/**
 * Created by khaled on 2/27/17.
 */

public class BehaviorNotesActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.behavior_notes);



        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Behaviour notes");


        FragmentUtils.createFragment(getSupportFragmentManager(), BehaviorNotesFragment.newInstance(), R.id.timetable_main_container );



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }

}
