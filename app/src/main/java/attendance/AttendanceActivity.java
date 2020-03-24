package attendance;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.skolera.skolera_android.R;

/**
 * Created by khaled on 2/21/17.
 */


public class AttendanceActivity extends AppCompatActivity {

    private String attendance;
    final private String attendancesKey = "attendances";
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance);

        context = this;

        Toolbar attendanceToolbar = (Toolbar) findViewById(R.id.default_toolbar_id);
        setSupportActionBar(attendanceToolbar);
        ActionBar attendanceActionBar = getSupportActionBar();
        attendanceActionBar.setDisplayHomeAsUpEnabled(true);
        attendanceActionBar.setTitle(R.string.attendanceTitle);

        Bundle extras= getIntent().getExtras();
        attendance = extras.getString(attendancesKey);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment f = AttendanceFragment.newInstance(attendance);
        ft.add(R.id.attendance_container, f);
        ft.commit();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }


}
