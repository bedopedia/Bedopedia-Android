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

    String studentId;
    public static Context context;
    List<CourseGroup> courseGroups;
    final String studentIdKey = "student_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades);

        Bundle extras= getIntent().getExtras();
        studentId = extras.getString(studentIdKey);
        courseGroups = (List<CourseGroup>) getIntent().getSerializableExtra("courseGroups");

        Toolbar gradesActivityToolbar = (Toolbar) findViewById(R.id.default_toolbar_id);
        setSupportActionBar(gradesActivityToolbar);
        ActionBar gradesActivityActionbar = getSupportActionBar();
        gradesActivityActionbar.setDisplayHomeAsUpEnabled(true);
        gradesActivityActionbar.setTitle(R.string.GradesTitle);
        context = this;

        FragmentUtils.createFragment(getSupportFragmentManager(),GradesFragment.newInstance(courseGroups , studentId), R.id.grades_container);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }


}
