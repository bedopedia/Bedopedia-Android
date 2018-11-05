package trianglz.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.skolera.skolera_android.R;


import Tools.FragmentUtils;
import trianglz.ui.activities.fragments.TimeTableFragment;


/**
 * Created by khaled on 3/1/17.
 */
/** file modified by gemy */
public class TimetableActivity extends AppCompatActivity {

    
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        Toolbar timeTableToolbar = findViewById(R.id.default_toolbar_id);
        setSupportActionBar(timeTableToolbar);
        ActionBar timeTableActionBar = getSupportActionBar();
        timeTableActionBar.setDisplayHomeAsUpEnabled(true);
        timeTableActionBar.setTitle(R.string.TimeTableTitle);
        FragmentUtils.createFragment(getSupportFragmentManager(), TimeTableFragment.newInstance(), R.id.timetable_main_container);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }
}
