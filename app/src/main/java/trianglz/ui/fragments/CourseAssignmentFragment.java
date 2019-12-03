package trianglz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.TopItemDecoration;
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
    private SwipeRefreshLayout pullRefreshLayout;
    private RecyclerView recyclerView;
    private CourseAssignmentAdapter courseAssignmentAdapter;
    private CourseAssignmentView courseAssignmentView;
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
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
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        studentImageView = rootView.findViewById(R.id.img_student);
        backBtn = rootView.findViewById(R.id.btn_back);
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        courseAssignmentAdapter = new CourseAssignmentAdapter(activity, this);
        recyclerView.setAdapter(courseAssignmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
        courseAssignmentView = new CourseAssignmentView(activity, this);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showSkeleton(true);
                pullRefreshLayout.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                getCourseAssignment();
            }
        });
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
            showSkeleton(true);
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
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onGetCourseAssignmentSuccess(ArrayList<CourseAssignment> courseAssignmentArrayList) {
        showSkeleton(false);
        recyclerView.setVisibility(View.VISIBLE);
        courseAssignmentAdapter.addData(courseAssignmentArrayList);

    }

    @Override
    public void onGetCourseAssignmentFailure(String message, int errorCode) {
        showSkeleton(false);
        recyclerView.setVisibility(View.VISIBLE);
        activity.showErrorDialog(activity, errorCode, "");

    }

    @Override
    public void onGetAssignmentDetailSuccess(ArrayList<AssignmentsDetail> assignmentsDetailArrayList, CourseAssignment courseAssignment) {

    }

    @Override
    public void onGetAssignmentDetailFailure(String message, int errorCode) {
        showSkeleton(false);
        activity.showErrorDialog(activity, errorCode, "");

    }

    @Override
    public void onItemClicked(CourseAssignment courseAssignment) {
        openAssignmentDetailActivity(courseAssignment);

    }

    private void openAssignmentDetailActivity(CourseAssignment courseAssignment) {
        //  Intent intent = new Intent(this, AssignmentDetailActivity.class);
        AssignmentDetailFragment assignmentDetailFragment = new AssignmentDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_STUDENT_NAME, student.getFirstName() + " " + student.getLastName());
        bundle.putSerializable(Constants.STUDENT, student);
        if (courseAssignment.getCourseName() != null) {
            bundle.putString(Constants.KEY_COURSE_NAME, courseAssignment.getCourseName());
        } else {
            bundle.putString(Constants.KEY_COURSE_NAME, "");
        }
        bundle.putInt(Constants.KEY_COURSE_ID, courseAssignment.getCourseId());
        bundle.putSerializable(Constants.KEY_COURSE_ASSIGNMENT, courseAssignment);

        assignmentDetailFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, assignmentDetailFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_course_layout, (ViewGroup) rootView, false);
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
