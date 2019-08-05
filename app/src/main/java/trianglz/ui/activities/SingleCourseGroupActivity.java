package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.CourseAssignmentPresenter;
import trianglz.core.presenters.SingleCourseGroupPresenter;
import trianglz.core.views.CourseAssignmentView;
import trianglz.core.views.SingleCourseGroupView;
import trianglz.managers.SessionManager;
import trianglz.models.AssignmentsDetail;
import trianglz.models.CourseAssignment;
import trianglz.models.CourseGroups;
import trianglz.models.Quizzes;
import trianglz.models.TeacherCourse;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class SingleCourseGroupActivity extends SuperActivity implements View.OnClickListener, CourseAssignmentPresenter, SingleCourseGroupPresenter {

    private TeacherCourse teacherCourse;
    private CourseGroups courseGroup;
    private ImageButton backBtn;
    private TextView courseGroupName;
    private LinearLayout attendanceLayout, quizzesLayout, assignmentsLayout, postsLayout;
    private CourseAssignmentView courseAssignmentView;
    private SingleCourseGroupView singleCourseGroupView;
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
        courseAssignmentView = new CourseAssignmentView(this, this);
        singleCourseGroupView = new SingleCourseGroupView(this, this);
    }

    private void openAssignmentDetailActivity(){
        if(Util.isNetworkAvailable(this)){
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + "/api/courses/" +
                    courseGroup.getCourseId()+ "/assignments";
            courseAssignmentView.getAssinmentDetail(url, null);
        }else {
            Util.showNoInternetConnectionDialog(this);
        }
    }
    private void openQuizzesDetailsActivity () {
        singleCourseGroupView.getTeacherQuizzes(courseGroup.getId() + "");
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
                showLoadingDialog();
                openQuizzesDetailsActivity();
                break;
            case R.id.layout_assignments:
                showLoadingDialog();
                openAssignmentDetailActivity();
                break;
            case R.id.layout_posts:
                openPostDetailsActivity();
                break;
        }
    }

    private void openPostDetailsActivity() {
        Intent intent = new Intent(this, PostDetailActivity.class);
        //todo remove hardcoded id and course name
        intent.putExtra(Constants.KEY_COURSE_ID, 68);
        intent.putExtra(Constants.KEY_COURSE_NAME, "English(KY06)");
        startActivity(intent);
    }

    /* Empty overridden methods because we used an already made View to call the get assignments
     details method */
    @Override
    public void onGetCourseAssignmentSuccess(ArrayList<CourseAssignment> courseAssignmentArrayList) {
    }

    @Override
    public void onGetCourseAssignmentFailure(String message, int errorCode) {

    }

    @Override
    public void onGetAssignmentDetailSuccess(ArrayList<AssignmentsDetail> assignmentsDetailArrayList, CourseAssignment courseAssignment) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        Intent intent = new Intent(this,AssignmentDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_ASSIGNMENTS,assignmentsDetailArrayList);
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        intent.putExtra(Constants.KEY_COURSE_NAME,courseGroup.getName());
        intent.putExtra(Constants.DISABLE_CLICK, true);
        intent.putExtra(Constants.AVATAR, false);
        startActivity(intent);

    }

    @Override
    public void onGetAssignmentDetailFailure(String message, int errorCode) {
        if(progress.isShowing()){
            progress.dismiss();
        }
    }

    @Override
    public void onGetTeacherQuizzesSuccess(ArrayList<Quizzes> quizzes) {
        if (progress.isShowing()) progress.hide();
        Intent intent = new Intent(this,QuizzesDetailsActivity.class);
        intent.putExtra(Constants.KEY_TEACHERS, true);
        intent.putExtra(Constants.KEY_COURSE_NAME,teacherCourse.getName());
        intent.putExtra(Constants.KEY_COURSE_GROUP_NAME,courseGroup.getName());
        intent.putExtra(Constants.KEY_QUIZZES,quizzes);
        startActivity(intent);
    }
    @Override
    public void onGetTeacherQuizzesFailure(String message, int errorCode) {
        if (progress.isShowing()) progress.hide();
    }
}
