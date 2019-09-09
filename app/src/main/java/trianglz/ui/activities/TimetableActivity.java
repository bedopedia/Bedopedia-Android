package trianglz.ui.activities;

import android.content.Context;
import android.os.Bundle;

import com.skolera.skolera_android.R;


/**
 * Created by khaled on 3/1/17.
 */
/** file modified by gemy */
public class TimetableActivity extends SuperActivity {

    
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
    //    FragmentUtils.createFragment(getSupportFragmentManager(), TimeTableFragment.newInstance(), R.id.timetable_main_container);
    }

}
