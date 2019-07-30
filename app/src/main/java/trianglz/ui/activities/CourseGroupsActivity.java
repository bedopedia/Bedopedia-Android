package trianglz.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.Arrays;

import trianglz.components.BottomItemDecoration;
import trianglz.models.CourseGroups;
import trianglz.models.TeacherCourse;
import trianglz.ui.adapters.TeacherCoursesAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class CourseGroupsActivity extends SuperActivity implements TeacherCoursesAdapter.TeacherCoursesInterface, View.OnClickListener {

    private TeacherCourse teacherCourse;
    private RecyclerView recyclerView;
    private TeacherCoursesAdapter teacherCoursesAdapter;
    private TextView courseNameTextView;
    private ImageButton backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_groups);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void getValueFromIntent() {
        teacherCourse = TeacherCourse.create(getIntent().getStringExtra(Constants.TEACHER_COURSE));
    }
    private void bindViews () {
        backBtn = findViewById(R.id.btn_back);
        courseNameTextView = findViewById (R.id.tv_course_name);
        recyclerView = findViewById(R.id.recycler_view);
        teacherCoursesAdapter = new TeacherCoursesAdapter(this,this);
        recyclerView.setAdapter(teacherCoursesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new BottomItemDecoration((int) Util.convertDpToPixel(6,this),false));
        courseNameTextView.setText(teacherCourse.getName());
        teacherCoursesAdapter.addCourseGroups(Arrays.asList(teacherCourse.getCourseGroups()));
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onCourseSelected(TeacherCourse teacherCourse) {

    }

    @Override
    public void onCourseGroupSelected(CourseGroups courseGroups) {
        Intent intent = new Intent(this, SingleCourseGroupActivity.class);
        intent.putExtra(Constants.KEY_COURSE_GROUPS, courseGroups.toString());
        intent.putExtra(Constants.TEACHER_COURSE, teacherCourse.toString());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
