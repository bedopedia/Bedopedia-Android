package trianglz.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.skolera.skolera_android.R;

import Tools.FragmentUtils;
import trianglz.ui.fragments.TimeTableFragment;


/**
 * Created by khaled on 3/1/17.
 */
/** file modified by gemy */
public class TimetableActivity extends AppCompatActivity {

    
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        FragmentUtils.createFragment(getSupportFragmentManager(), TimeTableFragment.newInstance(), R.id.timetable_main_container);
    }

}
