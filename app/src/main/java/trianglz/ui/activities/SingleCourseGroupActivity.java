package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import trianglz.models.Assignment;
import trianglz.models.CourseGroups;
import trianglz.models.TeacherCourse;
import trianglz.utils.Constants;

public class SingleCourseGroupActivity extends SuperActivity implements View.OnClickListener {

    private TeacherCourse teacherCourse;
    private CourseGroups courseGroup;
    private ImageButton backBtn;
    private TextView courseGroupName;
    private LinearLayout attendanceLayout, quizzesLayout, assignmentsLayout, postsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_course_group);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void getValueFromIntent() {
        teacherCourse = TeacherCourse.create(getIntent().getStringExtra(Constants.TEACHER_COURSE));
        courseGroup = CourseGroups.create(getIntent().getStringExtra(Constants.KEY_COURSE_GROUPS));
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);

        // layouts listeners
        attendanceLayout.setOnClickListener(this);
        quizzesLayout.setOnClickListener(this);
        assignmentsLayout.setOnClickListener(this);
        postsLayout.setOnClickListener(this);
    }

    private void bindViews() {
        // binding
        backBtn = findViewById(R.id.btn_back);
        courseGroupName = findViewById(R.id.tv_course_name);
        attendanceLayout = findViewById(R.id.layout_attendance);
        quizzesLayout = findViewById(R.id.layout_quizzes);
        assignmentsLayout = findViewById(R.id.layout_assignments);
        postsLayout = findViewById(R.id.layout_posts);

        // assigning values
        courseGroupName.setText(courseGroup.getName());
    }

    private void openAssignmentDetailActivity(){
//        Intent intent = new Intent(this, AssignmentDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Constants.STUDENT, teacherCourse.stud);
//        intent.putExtra(Constants.KEY_BUNDLE, bundle);
//        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.layout_attendance:
                break;
            case R.id.layout_quizzes:
                break;
            case R.id.layout_assignments:
                break;
            case R.id.layout_posts:
                break;
        }
    }
}
