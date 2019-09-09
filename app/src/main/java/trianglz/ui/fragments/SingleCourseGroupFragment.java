package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import trianglz.ui.activities.StudentMainActivity;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 09/09/2019.
 */
public class SingleCourseGroupFragment extends Fragment implements View.OnClickListener, CourseAssignmentPresenter, SingleCourseGroupPresenter {

    private StudentMainActivity activity;
    private View rootView;
    private TeacherCourse teacherCourse;
    private CourseGroups courseGroup;
    private ImageButton backBtn;
    private TextView courseGroupName;
    private LinearLayout attendanceLayout, quizzesLayout, assignmentsLayout, postsLayout;
    private CourseAssignmentView courseAssignmentView;
    private SingleCourseGroupView singleCourseGroupView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_single_course_group, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        onBackPress();
    }
    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            teacherCourse = TeacherCourse.create(bundle.getString(Constants.TEACHER_COURSE));
            courseGroup = CourseGroups.create(bundle.getString(Constants.KEY_COURSE_GROUPS));
        }
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
        backBtn = rootView.findViewById(R.id.btn_back);
        courseGroupName =  rootView.findViewById(R.id.tv_course_name);
        attendanceLayout =  rootView.findViewById(R.id.layout_attendance);
        quizzesLayout =  rootView.findViewById(R.id.layout_quizzes);
        assignmentsLayout =  rootView.findViewById(R.id.layout_assignments);
        postsLayout =  rootView.findViewById(R.id.layout_posts);

        // assigning values
        courseGroupName.setText(courseGroup.getName());
        courseAssignmentView = new CourseAssignmentView(activity, this);
        singleCourseGroupView = new SingleCourseGroupView(activity, this);
    }


    private void onBackPress() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    activity.getSupportFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    private void openAssignmentDetailActivity(){
        if(Util.isNetworkAvailable(activity)){
            activity.showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + "/api/courses/" +
                    courseGroup.getCourseId()+ "/assignments";
            courseAssignmentView.getAssinmentDetail(url, null);
        }else {
            Util.showNoInternetConnectionDialog(activity);
        }
    }
    private void openQuizzesDetailsActivity () {
        singleCourseGroupView.getTeacherQuizzes(courseGroup.getId() + "");
    }

    private void openPostDetailsActivity() {
        PostDetailFragment postDetailFragment = new PostDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_COURSE_GROUP_ID, courseGroup.getId());
        bundle.putInt(Constants.KEY_COURSE_ID, courseGroup.getCourseId());
        bundle.putString(Constants.KEY_COURSE_NAME, teacherCourse.getName());
        postDetailFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().
                beginTransaction().add(R.id.course_root, postDetailFragment, "CoursesFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }
    private void openTeacherAttendanceActivity(){
//        Intent intent = new Intent(this, TeacherAttendanceActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                activity.getSupportFragmentManager().popBackStack();
                break;
            case R.id.layout_attendance:
                openTeacherAttendanceActivity();
                break;
            case R.id.layout_quizzes:
             //   activity.showLoadingDialog();
                openQuizzesDetailsActivity();
                break;
            case R.id.layout_assignments:
             //   activity.showLoadingDialog();
            //    openAssignmentDetailActivity();
                break;
            case R.id.layout_posts:
                openPostDetailsActivity();
                break;
        }
    }

    @Override
    public void onGetCourseAssignmentSuccess(ArrayList<CourseAssignment> courseAssignmentArrayList) {

    }

    @Override
    public void onGetCourseAssignmentFailure(String message, int errorCode) {

    }

    @Override
    public void onGetAssignmentDetailSuccess(ArrayList<AssignmentsDetail> assignmentsDetailArrayList, CourseAssignment courseAssignment) {
//        if(progress.isShowing()){
//            progress.dismiss();
//        }
//        Intent intent = new Intent(this, AssignmentDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Constants.KEY_ASSIGNMENTS,assignmentsDetailArrayList);
//        intent.putExtra(Constants.KEY_BUNDLE,bundle);
//        courseGroup.setCourseName(teacherCourse.getName());
//        intent.putExtra(Constants.KEY_COURSE_GROUPS,courseGroup.toString());
//        intent.putExtra(Constants.AVATAR, false);
//        startActivity(intent);

    }

    @Override
    public void onGetAssignmentDetailFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.hide();

    }

    @Override
    public void onGetTeacherQuizzesSuccess(ArrayList<Quizzes> quizzes) {
//        if (progress.isShowing()) progress.hide();
//        Intent intent = new Intent(this, QuizzesDetailsActivity.class);
//        intent.putExtra(Constants.KEY_TEACHERS, true);
//        intent.putExtra(Constants.KEY_COURSE_NAME,teacherCourse.getName());
//        intent.putExtra(Constants.KEY_COURSE_GROUP_NAME,courseGroup.getName());
//        intent.putExtra(Constants.KEY_COURSE_GROUPS,courseGroup.toString());
//        intent.putExtra(Constants.KEY_QUIZZES,quizzes);
//        startActivity(intent);
    }

    @Override
    public void onGetTeacherQuizzesFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.hide();

    }
}
