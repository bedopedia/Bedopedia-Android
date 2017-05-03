package grades;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import Tools.FragmentUtils;
import com.example.bedopedia.bedopedia_android.R;
import java.util.List;

/**
 * Created by mohamedkhaled on 2/13/17.
 */

public class GradesAvtivity extends AppCompatActivity {

    String student_id;
    public static Context context;
    List<CourseGroup> courseGroups;
    String studentIdKey = "student_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades);

        Bundle extras= getIntent().getExtras();
        student_id = extras.getString(studentIdKey);
        courseGroups = (List<CourseGroup>) getIntent().getSerializableExtra("courseGroups");

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.GradesTitle);

        context = this;

        FragmentUtils.createFragment(getSupportFragmentManager(),GradesFragment.newInstance(courseGroups , student_id), R.id.grades_container);
        


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }


}
