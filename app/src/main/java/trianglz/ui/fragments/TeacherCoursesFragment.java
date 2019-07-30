package trianglz.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.TeacherCoursesPresenter;
import trianglz.core.views.TeacherCoursesView;
import trianglz.managers.SessionManager;
import trianglz.models.TeacherCourse;
import trianglz.ui.activities.StudentMainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherCoursesFragment extends Fragment implements TeacherCoursesPresenter {

    private TeacherCoursesView teacherCoursesView;
    private StudentMainActivity activity;


    public TeacherCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_courses, container, false);
        activity = (StudentMainActivity) getActivity();
        bindViews();
        getParentActivity().showLoadingDialog();
        teacherCoursesView.getTeacherCourses(SessionManager.getInstance().getId());
        return view;
    }


    private void bindViews() {
        teacherCoursesView = new TeacherCoursesView(getParentActivity(), this);
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
        Toast.makeText(getParentActivity(), "success", Toast.LENGTH_SHORT).show();
        Toast.makeText(getParentActivity(), "success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetTeacherCoursesFailure(String message, int errorCode) {

    }
}
