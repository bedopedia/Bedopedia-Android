package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skolera.skolera_android.R;

import trianglz.models.TeacherCourse;
import trianglz.utils.Constants;

public class CourseGroupsActivity extends SuperActivity {

    private TeacherCourse teacherCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_groups);
        getValueFromIntent();
    }

    private void getValueFromIntent() {
        teacherCourse = TeacherCourse.create(getIntent().getStringExtra(Constants.TEACHER_COURSE));  
    }
}
