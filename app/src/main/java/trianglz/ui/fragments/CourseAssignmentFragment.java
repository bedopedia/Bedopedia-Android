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

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.core.presenters.CourseAssignmentPresenter;
import trianglz.core.views.CourseAssignmentView;
import trianglz.managers.SessionManager;
import trianglz.models.AssignmentsDetail;
import trianglz.models.CourseAssignment;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.CourseAssignmentAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class CourseAssignmentFragment extends Fragment implements View.OnClickListener, CourseAssignmentPresenter, CourseAssignmentAdapter.CourseAssignmentAdapterInterface {

    private StudentMainActivity activity;
    private View rootView;
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private Student student;
    private RecyclerView recyclerView;
    private CourseAssignmentAdapter courseAssignmentAdapter;
    private CourseAssignmentView courseAssignmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
//        activity.toolbarView.setVisibility(View.GONE);
//        activity.headerLayout.setVisibility(View.GONE);
        rootView = inflater.inflate(R.layout.activity_course_assignment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        getCourseAssignment();
      //  onBackPress();
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

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable(Constants.STUDENT);
        }
    }

    private void bindViews() {
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        imageLoader = new PicassoLoader();
        studentImageView = rootView.findViewById(R.id.img_student);
        backBtn = rootView.findViewById(R.id.btn_back);
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        courseAssignmentAdapter = new CourseAssignmentAdapter(activity, this);
        recyclerView.setAdapter(courseAssignmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        courseAssignmentView = new CourseAssignmentView(activity, this);
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(activity)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
                    .transform(new CircleTransform())
                    .into(studentImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }

    private void getCourseAssignment() {
        if (Util.isNetworkAvailable(activity)) {
            activity.showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + "/api/students/" +
                    student.getId() + "/course_groups_with_assignments_number";
            courseAssignmentView.getCourseAssignment(url);
        } else {
            Util.showNoInternetConnectionDialog(activity);
        }
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
    public void onGetCourseAssignmentSuccess(ArrayList<CourseAssignment> courseAssignmentArrayList) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        courseAssignmentAdapter.addData(courseAssignmentArrayList);
    }

    @Override
    public void onGetCourseAssignmentFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        if (errorCode == 401 || errorCode == 500) {
            activity.logoutUser(activity);
        } else {
            activity.showErrorDialog(activity);
        }
    }

    @Override
    public void onGetAssignmentDetailSuccess(ArrayList<AssignmentsDetail> assignmentsDetailArrayList, CourseAssignment courseAssignment) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        openAssignmentDetailActivity(assignmentsDetailArrayList, courseAssignment);
    }

    @Override
    public void onGetAssignmentDetailFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        if (errorCode == 401 || errorCode == 500) {
            activity.logoutUser(activity);
        } else {
            activity.showErrorDialog(activity);
        }
    }

    @Override
    public void onItemClicked(CourseAssignment courseAssignment) {
        if (Util.isNetworkAvailable(activity)) {
            activity.showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + "/api/courses/" +
                    courseAssignment.getId() + "/assignments";
            courseAssignmentView.getAssinmentDetail(url, courseAssignment);
        } else {
            Util.showNoInternetConnectionDialog(activity);
        }
    }

    private void openAssignmentDetailActivity(ArrayList<AssignmentsDetail> assignmentsDetailArrayList,
                                              CourseAssignment courseAssignment) {
        //  Intent intent = new Intent(this, AssignmentDetailActivity.class);
        AssignmentDetailFragment assignmentDetailFragment = new AssignmentDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_STUDENT_NAME, student.getFirstName() + " " + student.getLastName());
        bundle.putSerializable(Constants.KEY_ASSIGNMENTS, assignmentsDetailArrayList);
        bundle.putSerializable(Constants.STUDENT, student);
        if (courseAssignment.getCourseName() != null) {
            bundle.putString(Constants.KEY_COURSE_NAME, courseAssignment.getCourseName());
        } else {
            bundle.putString(Constants.KEY_COURSE_NAME, "");
        }
        bundle.putInt(Constants.KEY_COURSE_ID, courseAssignment.getId());
        assignmentDetailFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, assignmentDetailFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

}
