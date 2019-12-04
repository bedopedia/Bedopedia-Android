package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.SingleCourseGroupPresenter;
import trianglz.core.views.SingleCourseGroupView;
import trianglz.models.CourseGroups;
import trianglz.models.Quizzes;
import trianglz.models.TeacherCourse;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 09/09/2019.
 */
public class SingleCourseGroupFragment extends Fragment implements View.OnClickListener, SingleCourseGroupPresenter {

    private StudentMainActivity activity;
    private View rootView;
    private TeacherCourse teacherCourse;
    private CourseGroups courseGroup;
    private ImageButton backBtn;
    private TextView courseGroupName;
    private LinearLayout attendanceLayout, quizzesLayout, assignmentsLayout, postsLayout;
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
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
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
        courseGroupName = rootView.findViewById(R.id.tv_course_name);
        attendanceLayout = rootView.findViewById(R.id.layout_attendance);
        quizzesLayout = rootView.findViewById(R.id.layout_quizzes);
        assignmentsLayout = rootView.findViewById(R.id.layout_assignments);
        postsLayout = rootView.findViewById(R.id.layout_posts);

        // assigning values
        courseGroupName.setText(courseGroup.getName());
        singleCourseGroupView = new SingleCourseGroupView(activity, this);
    }

    private void openAssignmentDetailActivity() {
        AssignmentDetailFragment assignmentDetailFragment = new AssignmentDetailFragment();
        Bundle bundle = new Bundle();
        courseGroup.setCourseName(teacherCourse.getName());
        bundle.putString(Constants.KEY_COURSE_GROUPS, courseGroup.toString());
        bundle.putBoolean(Constants.AVATAR, false);
        assignmentDetailFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.course_root, assignmentDetailFragment, "CoursesFragment").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();

    }

    private void openQuizzesDetailsActivity() {
        singleCourseGroupView.getTeacherQuizzes(courseGroup.getId() + "");
    }

    private void openPostDetailsActivity() {
        PostDetailFragment postDetailFragment = new PostDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_COURSE_GROUP_ID, courseGroup.getId());
        //       bundle.putInt(Constants.KEY_COURSE_ID, courseGroup.getCourseId());
        bundle.putInt(Constants.KEY_COURSE_ID, courseGroup.getId());
        bundle.putString(Constants.KEY_COURSE_NAME, teacherCourse.getName());
        postDetailFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.course_root, postDetailFragment, "CoursesFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    private void openTeacherAttendanceActivity() {
        TeacherAttendanceFragment teacherAttendanceFragment = new TeacherAttendanceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_COURSE_GROUP_ID, courseGroup.getId());
        teacherAttendanceFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.course_root, teacherAttendanceFragment, "CoursesFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.layout_attendance:
                openTeacherAttendanceActivity();
                break;
            case R.id.layout_quizzes:
                openQuizzesDetailsActivity();
                break;
            case R.id.layout_assignments:
                openAssignmentDetailActivity();
                break;
            case R.id.layout_posts:
                openPostDetailsActivity();
                break;
        }
    }


    @Override
    public void onGetTeacherQuizzesSuccess(ArrayList<Quizzes> quizzes) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        QuizzesDetailsFragment quizzesDetailsFragment = new QuizzesDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.KEY_TEACHERS, true);
        bundle.putString(Constants.KEY_COURSE_NAME, teacherCourse.getName());
        bundle.putString(Constants.KEY_COURSE_GROUP_NAME, courseGroup.getName());
        bundle.putString(Constants.KEY_COURSE_GROUPS, courseGroup.toString());
        bundle.putParcelableArrayList(Constants.KEY_QUIZZES, quizzes);

        quizzesDetailsFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.course_root, quizzesDetailsFragment, "CoursesFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    @Override
    public void onGetTeacherQuizzesFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();

    }
}

