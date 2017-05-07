package timetable;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.bedopedia.bedopedia_android.R;


import Tools.FragmentUtils;


/**
 * Created by khaled on 3/1/17.
 */

public class TimetableActivity extends AppCompatActivity {

    
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        Toolbar tb = (Toolbar) findViewById(R.id.default_toolbar_id);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Time table");

        FragmentUtils.createFragment(getSupportFragmentManager(), TimeTableFragment.newInstance(), R.id.timetable_main_container);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }

}
