package behaviorNotes;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.bedopedia.bedopedia_android.R;

import Tools.FragmentUtils;

/**
 * Created by khaled on 2/27/17.
 */

public class BehaviorNotesActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.behavior_notes);



        Toolbar behaviorNotesToolbar = (Toolbar) findViewById(R.id.default_toolbar_id);
        setSupportActionBar(behaviorNotesToolbar);
        ActionBar behaviorNotesActionbar = getSupportActionBar();
        behaviorNotesActionbar.setDisplayHomeAsUpEnabled(true);
        behaviorNotesActionbar.setTitle(R.string.behaviorNotesTitle);


        FragmentUtils.createFragment(getSupportFragmentManager(), BehaviorNotesFragment.newInstance(), R.id.timetable_main_container );



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }

}
