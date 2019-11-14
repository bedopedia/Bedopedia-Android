package trianglz.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.TeacherCoursesPresenter;
import trianglz.core.views.TeacherCoursesView;
import trianglz.managers.SessionManager;
import trianglz.models.CourseGroups;
import trianglz.models.TeacherCourse;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.TeacherCoursesAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherCoursesFragment extends Fragment implements TeacherCoursesPresenter, TeacherCoursesAdapter.TeacherCoursesInterface, StudentMainActivity.OnBackPressedInterface {

    private View rootView;
    private TeacherCoursesView teacherCoursesView;
    private StudentMainActivity activity;
    private RecyclerView recyclerView;
    private TeacherCoursesAdapter teacherCoursesAdapter;


    public TeacherCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_teacher_courses, container, false);
        activity = (StudentMainActivity) getActivity();
        bindViews();
        getParentActivity().showLoadingDialog();
        teacherCoursesView.getTeacherCourses(SessionManager.getInstance().getId());
        return rootView;
    }


    private void bindViews() {
        teacherCoursesView = new TeacherCoursesView(getParentActivity(), this);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        teacherCoursesAdapter = new TeacherCoursesAdapter(getParentActivity(), this);
        recyclerView.setAdapter(teacherCoursesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
    }

    // this method is to reduce the amount of calling getActivity()
    private StudentMainActivity getParentActivity() {
        if (activity == null) {
            activity = (StudentMainActivity) getActivity();
            return activity;
        } else {
            return activity;
        }
    }

    @Override
    public void onGetTeacherCoursesSuccess(ArrayList<TeacherCourse> teacherCourses) {
        if (!getParentActivity().progress.isShowing()) {
            getParentActivity().progress.dismiss();
        }
        teacherCoursesAdapter.addData(teacherCourses);
    }

    @Override
    public void onGetTeacherCoursesFailure(String message, int errorCode) {
        if (!getParentActivity().progress.isShowing()) {
            getParentActivity().progress.dismiss();
        }
    }

    @Override
    public void onCourseSelected(TeacherCourse teacherCourse) {
        if (teacherCourse != null) {
            CourseGroupsFragment courseGroupsFragment = new CourseGroupsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TEACHER_COURSE, teacherCourse.toString());
            courseGroupsFragment.setArguments(bundle);
            getChildFragmentManager().
                    beginTransaction().add(R.id.course_root, courseGroupsFragment, "CoursesFragments").
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(null).commit();
//            Intent intent = new Intent(getParentActivity(), CourseGroupsActivity.class);
//            intent.putExtra(Constants.TEACHER_COURSE, teacherCourse.toString());
//            startActivity(intent);
        }
    }

    @Override
    public void onCourseGroupSelected(CourseGroups courseGroups) {

    }

    @Override
    public void onBackPressed() {
        if (activity.pager.getCurrentItem() == 0){
            getChildFragmentManager().popBackStack();
            if(getChildFragmentManager().getFragments().size()==1){
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.headerLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
