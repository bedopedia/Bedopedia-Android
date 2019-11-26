package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private FrameLayout listFrameLayout, placeholderFrameLayout;

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
            } else {
                courseName = bundle.getString(Constants.KEY_COURSE_NAME);
                courseId = bundle.getInt(Constants.KEY_COURSE_ID, 0);
            }
        }
        studentName = bundle.getString(Constants.KEY_STUDENT_NAME);
    }

    private void bindViews() {
        imageLoader = new PicassoLoader();
        studentImageView = rootView.findViewById(R.id.img_student);
        backBtn = rootView.findViewById(R.id.btn_back);
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new AssignmentDetailAdapter(activity, this, courseName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(10, activity), false));
        assignmentsDetailView = new AssignmentsDetailView(activity, this);
        listFrameLayout = rootView.findViewById(R.id.recycler_view_layout);
        placeholderFrameLayout = rootView.findViewById(R.id.placeholder_layout);
        placeholderTextView = rootView.findViewById(R.id.placeholder_tv);
        tabLayout = rootView.findViewById(R.id.tab_layout);
        showHidePlaceholder(getArrayList(true), true);

        headerTextView = rootView.findViewById(R.id.tv_header);
        headerTextView.setText(courseName);

        tabLayout.setSelectedTabIndicatorColor(activity.getResources().getColor(Util.checkUserColor()));
        tabLayout.setTabTextColors(activity.getResources().getColor(R.color.steel), activity.getResources().getColor(Util.checkUserColor()));

    }

    private ArrayList<AssignmentsDetail> getArrayList(boolean isOpen) {
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
                    showHidePlaceholder(getArrayList(true), true);
                else
                    showHidePlaceholder(getArrayList(false), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

    private void showHidePlaceholder(ArrayList<AssignmentsDetail> assignmentsDetails, Boolean isOpen) {
        if (assignmentsDetails.isEmpty()) {
            if (isOpen) {
                placeholderTextView.setText(activity.getResources().getString(R.string.open_assignments_placeholder));
            } else {
                placeholderTextView.setText(activity.getResources().getString(R.string.closed_assignments_placeholder));
            }
            listFrameLayout.setVisibility(View.GONE);
            placeholderFrameLayout.setVisibility(View.VISIBLE);
        } else {
            listFrameLayout.setVisibility(View.VISIBLE);
            placeholderFrameLayout.setVisibility(View.GONE);
            adapter.addData(assignmentsDetails);
        }
    }
}
