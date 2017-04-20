package grades;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import attendance.AttendanceFragment;
import gradeBook.ActivityCourse;
import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Tools.Dialogue;
import gradeBook.Course;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamedkhaled on 2/13/17.
 */

public class GradesAvtivity extends AppCompatActivity {

    String student_id;
    public static Context context;
    List<CourseGroup> courseGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades);

        Bundle extras= getIntent().getExtras();
        student_id = extras.getString("student_id");
        courseGroups = (List<CourseGroup>) getIntent().getSerializableExtra("courseGroups");

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Grades");

        context = this;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment f = GradesFragment.newInstance(courseGroups);
        ft.add(R.id.grades_container, f);
        ft.commit();



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void itemClicked(int position){
        Intent i =  new Intent(getApplicationContext(), ActivityCourse.class);
        i.putExtra("student_id",student_id);
        i.putExtra("course_group_id", String.valueOf(courseGroups.get(position).getId()));
        i.putExtra("course_id", String.valueOf(courseGroups.get(position).getCourseId()));
        i.putExtra("course_name", courseGroups.get(position).getCourseName());
        startActivity(i);
    }

}
