package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.Arrays;

import trianglz.components.BottomItemDecoration;
import trianglz.models.CourseGroups;
import trianglz.models.TeacherCourse;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.TeacherCoursesAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 09/09/2019.
 */
public class CourseGroupsFragment extends Fragment implements TeacherCoursesAdapter.TeacherCoursesInterface, View.OnClickListener {


    private StudentMainActivity activity;
    private View rootView;
    private TeacherCourse teacherCourse;
    private RecyclerView recyclerView;
    private TeacherCoursesAdapter teacherCoursesAdapter;
    private TextView courseNameTextView;
    private ImageButton backBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_course_groups, container, false);
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
        if (bundle != null)
            teacherCourse = TeacherCourse.create(bundle.getString(Constants.TEACHER_COURSE));
    }

    private void bindViews() {
        activity.headerLayout.setVisibility(View.GONE);
        activity.toolbarView.setVisibility(View.GONE);
        backBtn = rootView.findViewById(R.id.btn_back);
        courseNameTextView = rootView.findViewById(R.id.tv_course_name);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        teacherCoursesAdapter = new TeacherCoursesAdapter(activity, this);
        recyclerView.setAdapter(teacherCoursesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomItemDecoration((int) Util.convertDpToPixel(6, activity), false));
        courseNameTextView.setText(teacherCourse.getName());
        teacherCoursesAdapter.addCourseGroups(Arrays.asList(teacherCourse.getCourseGroups()));
    }

    private void onBackPress() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    activity.getSupportFragmentManager().popBackStack();
                    if (activity.getSupportFragmentManager().getBackStackEntryCount() == 1) {
                        activity.toolbarView.setVisibility(View.VISIBLE);
                        activity.headerLayout.setVisibility(View.VISIBLE);
                    }

                    return true;
                }
                return false;
            }
        });
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.headerLayout.setVisibility(View.VISIBLE);
                activity.getSupportFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onCourseSelected(TeacherCourse teacherCourse) {

    }
     @Override
    public void onCourseGroupSelected(CourseGroups courseGroups) {
        SingleCourseGroupFragment singleCourseGroupFragment = new SingleCourseGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_COURSE_GROUPS, courseGroups.toString());
        bundle.putString(Constants.TEACHER_COURSE, teacherCourse.toString());
        singleCourseGroupFragment.setArguments(bundle);
        activity.getSupportFragmentManager().
                beginTransaction().add(R.id.course_root, singleCourseGroupFragment, "CoursesFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }
//    @Override
//    public void onCourseGroupSelected(CourseGroups courseGroups) {
//        Intent intent = new Intent(activity, SingleCourseGroupActivity.class);
//        intent.putExtra(Constants.KEY_COURSE_GROUPS, courseGroups.toString());
//        intent.putExtra(Constants.TEACHER_COURSE, teacherCourse.toString());
//        startActivity(intent);
//    }
}
