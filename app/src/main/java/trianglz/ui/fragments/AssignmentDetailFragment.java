package trianglz.ui.fragments;

import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import info.hoang8f.android.segmented.SegmentedGroup;
import trianglz.components.AvatarPlaceholderModified;
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
    private TextView headerTextView;
    private RadioButton openButton, closedButton;
    private SegmentedGroup segmentedGroup;
    private int courseId;
    private String studentName;
    boolean isStudent, isParent;
    private CourseGroups courseGroups;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
//        activity.toolbarView.setVisibility(View.GONE);
//        activity.headerLayout.setVisibility(View.GONE);
        rootView = inflater.inflate(R.layout.activity_assignment_detail, container, false);
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

    private void onBackPress() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    private void getValueFromIntent() {
        isStudent = SessionManager.getInstance().getStudentAccount();
        isParent = SessionManager.getInstance().getUserType();
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
        setStudentImage(studentName);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new AssignmentDetailAdapter(activity, this, courseName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(10, activity), false));
        assignmentsDetailView = new AssignmentsDetailView(activity, this);
        adapter.addData(getArrayList(true));
        headerTextView = rootView.findViewById(R.id.tv_header);
        headerTextView.setText(courseName);

        // radio button for segment control
        segmentedGroup = rootView.findViewById(R.id.segmented);
        openButton = rootView.findViewById(R.id.btn_open);
        closedButton = rootView.findViewById(R.id.btn_closed);
        segmentedGroup.check(openButton.getId());
        if (isStudent) {
            segmentedGroup.setTintColor(Color.parseColor("#fd8268"));
        } else if (isParent) {
            segmentedGroup.setTintColor(Color.parseColor("#06c4cc"));
        } else {
            segmentedGroup.setTintColor(Color.parseColor("#007ee5"));
        }
    }

    private ArrayList<AssignmentsDetail> getArrayList(boolean isOpen) {
        if (assignmentsDetailArrayList.isEmpty()) return null;
        ArrayList<AssignmentsDetail> filteredDetails = new ArrayList<>();
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
        openButton.setOnClickListener(this);
        closedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                activity.getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_open:
                adapter.addData(getArrayList(true));
                break;
            case R.id.btn_closed:
                adapter.addData(getArrayList(false));
                break;
        }
    }

    @Override
    public void onItemClicked(AssignmentsDetail assignmentsDetail) {
        if (isStudent || isParent) {
            AssignmentFragment assignmentFragment = new AssignmentFragment();
            Bundle bundle = new Bundle();
            if (courseId != 0) bundle.putInt(Constants.KEY_COURSE_ID, courseId);
            bundle.putString(Constants.KEY_STUDENT_NAME, studentName);
            bundle.putString(Constants.KEY_ASSIGNMENT_DETAIL, assignmentsDetail.toString());
            assignmentFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().
                    beginTransaction().add(R.id.menu_fragment_root, assignmentFragment).
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(null).commit();
        } else {
//            Intent intent = new Intent(this, GradingActivity.class);
//            intent.putExtra(Constants.KEY_COURSE_ID, courseId);
//            intent.putExtra(Constants.KEY_COURSE_GROUP_ID, courseGroups.getId());
//            intent.putExtra(Constants.KEY_ASSIGNMENT_NAME, assignmentsDetail.getName());
//            intent.putExtra(Constants.KEY_ASSIGNMENT_ID, assignmentsDetail.getId());
//            startActivity(intent);
        }
    }

    private void setStudentImage(String name) {
        Bundle bundle = this.getArguments();
        if (bundle.getBoolean(Constants.AVATAR, true)) {
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            studentImageView.setVisibility(View.INVISIBLE);
        }

    }
}
