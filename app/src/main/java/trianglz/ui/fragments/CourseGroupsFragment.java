package trianglz.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import java.util.Arrays;

import trianglz.components.TopItemDecoration;
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
      //  onBackPress();
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
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
        courseNameTextView.setText(teacherCourse.getName());
        teacherCoursesAdapter.addCourseGroups(Arrays.asList(teacherCourse.getCourseGroups()));
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
                getParentFragment().getChildFragmentManager().popBackStack();
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
         getParentFragment().getChildFragmentManager().
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
