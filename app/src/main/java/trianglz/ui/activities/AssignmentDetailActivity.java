package trianglz.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import info.hoang8f.android.segmented.SegmentedGroup;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.AssignmentsDetailPresenter;
import trianglz.core.views.AssignmentsDetailView;
import trianglz.managers.SessionManager;
import trianglz.models.AssignmentsDetail;
import trianglz.models.CourseGroups;
import trianglz.models.Student;
import trianglz.ui.adapters.AssignmentDetailAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class AssignmentDetailActivity extends SuperActivity implements View.OnClickListener, AssignmentsDetailPresenter, AssignmentDetailAdapter.AssignmentDetailInterface {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_detail);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void getValueFromIntent() {
        isStudent = SessionManager.getInstance().getStudentAccount();
        isParent = SessionManager.getInstance().getUserType();
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
        assignmentsDetailArrayList = (ArrayList<AssignmentsDetail>) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ASSIGNMENTS);
        courseGroups = CourseGroups.create(getIntent().getStringExtra(Constants.KEY_COURSE_GROUPS));
        if (courseGroups != null) {
            courseName = courseGroups.getCourseName();
            courseId = courseGroups.getCourseId();
        } else {
            courseName = getIntent().getStringExtra(Constants.KEY_COURSE_NAME);
            courseId = getIntent().getIntExtra(Constants.KEY_COURSE_ID, 0);
        }

        studentName = getIntent().getStringExtra(Constants.KEY_STUDENT_NAME);
    }


    private void bindViews() {
        imageLoader = new PicassoLoader();
        studentImageView = findViewById(R.id.img_student);
        backBtn = findViewById(R.id.btn_back);
        setStudentImage(studentName);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new AssignmentDetailAdapter(this,this,courseName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(10,this),false));
        assignmentsDetailView = new AssignmentsDetailView(this,this);
        adapter.addData(getArrayList(true));
        headerTextView = findViewById(R.id.tv_header);
        headerTextView.setText(courseName);

        // radio button for segment control
        segmentedGroup = findViewById(R.id.segmented);
        openButton = findViewById(R.id.btn_open);
        closedButton = findViewById(R.id.btn_closed);
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
        if (assignmentsDetailArrayList.isEmpty()) return null ;
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_open:
                adapter.addData(getArrayList(true));
                break;
            case R.id.btn_closed:
                adapter.addData(getArrayList(false));
                break;
        }
    }

    private void setStudentImage(String name) {
        if (getIntent().getBooleanExtra(Constants.AVATAR, true)) {
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            studentImageView.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void onItemClicked(AssignmentsDetail assignmentsDetail) {
        if (isStudent || isParent) {
            Intent intent = new Intent(this, AssignmentActivity.class);
            if (courseId != 0) intent.putExtra(Constants.KEY_COURSE_ID, courseId);
            intent.putExtra(Constants.KEY_STUDENT_NAME, studentName);
            intent.putExtra(Constants.KEY_ASSIGNMENT_DETAIL, assignmentsDetail.toString());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, AssignmentGradingActivity.class);
            intent.putExtra(Constants.KEY_COURSE_ID, courseId);
            intent.putExtra(Constants.KEY_COURSE_GROUP_ID, courseGroups.getId());
            intent.putExtra(Constants.KEY_ASSIGNMENT_ID, assignmentsDetail.getId());
            startActivity(intent);
        }
    }
}
