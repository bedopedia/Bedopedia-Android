package trianglz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import android.widget.TextView;

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
import trianglz.core.presenters.AssignmentsDetailPresenter;
import trianglz.core.views.AssignmentsDetailView;
import trianglz.managers.SessionManager;
import trianglz.models.AssignmentsDetail;
import trianglz.models.CourseAssignment;
import trianglz.models.CourseGroups;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.AssignmentDetailAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class AssignmentDetailFragment extends Fragment implements View.OnClickListener, AssignmentsDetailPresenter, AssignmentDetailAdapter.AssignmentDetailInterface {

    private StudentMainActivity activity;
    private View rootView;
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private Student student;
    private RecyclerView recyclerView;
    private AssignmentDetailAdapter adapter;
    private AssignmentsDetailView assignmentsDetailView;
    private ArrayList<AssignmentsDetail> assignmentsDetailArrayList;
    private String courseName = "";
    private TextView headerTextView, placeholderTextView;
    private int courseId;
    private String studentName;
    private CourseGroups courseGroups;
    private TabLayout tabLayout;
    private LinearLayout placeholderLinearLayout;
    private SwipeRefreshLayout pullRefreshLayout;
    private boolean isOpen = true;
    private CourseAssignment courseAssignment;
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_assignment_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        //todo if student
        if (Util.isNetworkAvailable(activity)) {
            getAssignmentDetails();

        } else {
            Util.showNoInternetConnectionDialog(activity);
        }
    }


    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable(Constants.STUDENT);
            assignmentsDetailArrayList = (ArrayList<AssignmentsDetail>) bundle.getSerializable(Constants.KEY_ASSIGNMENTS);
            courseGroups = CourseGroups.create(bundle.getString(Constants.KEY_COURSE_GROUPS));
            if (courseGroups != null) {
                courseName = courseGroups.getCourseName();
                courseId = courseGroups.getCourseId();
                courseAssignment = null;
            } else {
                courseName = bundle.getString(Constants.KEY_COURSE_NAME);
                courseId = bundle.getInt(Constants.KEY_COURSE_ID, 0);
                courseAssignment = (CourseAssignment) bundle.getSerializable(Constants.KEY_COURSE_ASSIGNMENT);
            }
        }
        studentName = bundle.getString(Constants.KEY_STUDENT_NAME);
    }

    private void bindViews() {
        imageLoader = new PicassoLoader();
        assignmentsDetailArrayList = new ArrayList<>();
        studentImageView = rootView.findViewById(R.id.img_student);
        backBtn = rootView.findViewById(R.id.btn_back);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new AssignmentDetailAdapter(activity, this, courseName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(10, activity), false));
        assignmentsDetailView = new AssignmentsDetailView(activity, this);
        placeholderTextView = rootView.findViewById(R.id.placeholder_tv);
        tabLayout = rootView.findViewById(R.id.tab_layout);

        headerTextView = rootView.findViewById(R.id.tv_header);
        headerTextView.setText(courseName);

        tabLayout.setSelectedTabIndicatorColor(activity.getResources().getColor(Util.checkUserColor()));
        tabLayout.setTabTextColors(activity.getResources().getColor(R.color.steel), activity.getResources().getColor(Util.checkUserColor()));
        if (!SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString())) {
            setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        }
        placeholderLinearLayout = rootView.findViewById(R.id.placeholder_layout);
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private ArrayList<AssignmentsDetail> getArrayList() {
        ArrayList<AssignmentsDetail> filteredDetails = new ArrayList<>();
        if (assignmentsDetailArrayList.isEmpty()) return new ArrayList<>();
        for (AssignmentsDetail assignmentsDetail : assignmentsDetailArrayList) {
            if (assignmentsDetail.getState().equals("running")) {
                if (isOpen) filteredDetails.add(assignmentsDetail);
            } else {
                if (!isOpen) filteredDetails.add(assignmentsDetail);
            }
        }
        return filteredDetails;
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    isOpen = true;
                else
                    isOpen = false;
                showHidePlaceholder(getArrayList());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.setRefreshing(false);
                getAssignmentDetails();
                placeholderLinearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onItemClicked(AssignmentsDetail assignmentsDetail) {
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            if ((assignmentsDetail.getDescription() == null || assignmentsDetail.getDescription().trim().isEmpty()) && assignmentsDetail.getUploadedFilesCount() == 0) {
                activity.showErrorDialog(activity, -3, activity.getResources().getString(R.string.no_content));
            } else {
                AssignmentFragment assignmentFragment = new AssignmentFragment();
                Bundle bundle = new Bundle();
                if (courseId != 0) bundle.putInt(Constants.KEY_COURSE_ID, courseId);
                bundle.putString(Constants.KEY_STUDENT_NAME, studentName);
                bundle.putString(Constants.KEY_ASSIGNMENT_DETAIL, assignmentsDetail.toString());
                assignmentFragment.setArguments(bundle);
                getParentFragment().getChildFragmentManager().
                        beginTransaction().add(R.id.menu_fragment_root, assignmentFragment, "MenuFragments").
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                        addToBackStack(null).commit();
            }
        } else {
            GradingFragment gradingFragment = new GradingFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_COURSE_ID, courseId);
            bundle.putInt(Constants.KEY_COURSE_GROUP_ID, courseGroups.getId());
            bundle.putString(Constants.KEY_ASSIGNMENT_NAME, assignmentsDetail.getName());
            bundle.putInt(Constants.KEY_ASSIGNMENT_ID, assignmentsDetail.getId());
            gradingFragment.setArguments(bundle);
            getParentFragment().getChildFragmentManager().
                    beginTransaction().add(R.id.course_root, gradingFragment, "CoursesFragments").
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(null).commit();

        }
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

    private void showHidePlaceholder(ArrayList<AssignmentsDetail> assignmentsDetails) {
        if (assignmentsDetails.isEmpty()) {
            if (isOpen) {
                placeholderTextView.setText(activity.getResources().getString(R.string.open_assignments_placeholder));
            } else {
                placeholderTextView.setText(activity.getResources().getString(R.string.closed_assignments_placeholder));
            }
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholderTextView.setVisibility(View.GONE);
            adapter.addData(assignmentsDetails);
        }
    }

    @Override
    public void onGetAssignmentDetailSuccess(ArrayList<AssignmentsDetail> assignmentsDetailArrayList, CourseAssignment courseAssignment) {
        this.assignmentsDetailArrayList = assignmentsDetailArrayList;
        showHidePlaceholder(getArrayList());
        showSkeleton(false);
    }

    @Override
    public void onGetAssignmentDetailFailure(String message, int errorCode) {
        showHidePlaceholder(getArrayList());
        activity.showErrorDialog(activity, errorCode, "");
        showSkeleton(false);
    }

    void getAssignmentDetails() {
        String url = SessionManager.getInstance().getBaseUrl() + "/api/courses/" +
                courseId + "/assignments/";
        assignmentsDetailView.getAssignmentDetail(url, courseAssignment);
        showSkeleton(true);
    }

    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_assignment_layout, (ViewGroup) rootView, false);
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
