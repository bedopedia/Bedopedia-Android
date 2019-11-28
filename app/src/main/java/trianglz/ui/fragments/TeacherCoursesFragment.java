package trianglz.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
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
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;

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
        showSkeleton(true);
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
        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        showSkeleton(false);
        teacherCoursesAdapter.addData(teacherCourses);
    }

    @Override
    public void onGetTeacherCoursesFailure(String message, int errorCode) {
        showSkeleton(false);
        activity.showErrorDialog(activity, errorCode, "");
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
        }
    }

    @Override
    public void onCourseGroupSelected(CourseGroups courseGroups) {

    }

    @Override
    public void onBackPressed() {
        if (activity.pager.getCurrentItem() == 0) {
            getChildFragmentManager().popBackStack();
            if (getChildFragmentManager().getFragments().size() == 1) {
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.headerLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_row_layout, null);
                skeletonLayout.addView(rowLayout);
            }
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
            shimmer.showShimmer(true);
            skeletonLayout.setVisibility(View.VISIBLE);
            skeletonLayout.bringToFront();
        } else {
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            shimmer.setVisibility(View.GONE);
        }
    }

}
