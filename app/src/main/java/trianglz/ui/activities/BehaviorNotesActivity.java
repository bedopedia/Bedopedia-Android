package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.skolera.skolera_android.R;

import Tools.FragmentUtils;
import trianglz.ui.activities.fragments.BehaviorNotesFragment;

/**
 * Created by khaled on 2/27/17.
 */
/**
 * modified by gemy */
public class BehaviorNotesActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior_notes);
        FragmentUtils.createFragment(getSupportFragmentManager(), BehaviorNotesFragment.newInstance(), R.id.timetable_main_container );



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }

}
