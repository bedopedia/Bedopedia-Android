package trianglz.ui.activities;

import android.content.Intent;
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
import trianglz.core.presenters.AssignmentsDetailPresenter;
import trianglz.core.presenters.CourseAssignmentPresenter;
import trianglz.core.views.CourseAssignmentView;
import trianglz.managers.SessionManager;
import trianglz.models.AssignmentsDetail;
import trianglz.models.CourseAssignment;
import trianglz.models.Student;
import trianglz.ui.adapters.OnlineQuizzesAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class OnlineQuizzesActivity extends SuperActivity implements View.OnClickListener, OnlineQuizzesAdapter.OnlineQuizzesInterface, AssignmentsDetailPresenter, CourseAssignmentPresenter {
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private Student student;
    private RecyclerView recyclerView;
    private OnlineQuizzesAdapter adapter;
    private CourseAssignmentView courseAssignmentView;
    private ArrayList<AssignmentsDetail> assignmentsDetailArrayList;
    private String courseName = "";
    private TextView headerTextView;
    private RadioButton openButton, closedButton;
    private SegmentedGroup segmentedGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_quizzes);
        getValueFromIntent();
        bindViews();
        setListeners();
        getCourseAssignment();
    }

    private void getCourseAssignment() {
        if (Util.isNetworkAvailable(this)) {
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + "/api/students/" +
                    student.getId() + "/course_groups_with_assignments_number";
            courseAssignmentView.getCourseAssignment(url);
        } else {
            Util.showNoInternetConnectionDialog(this);
        }
    }
    private void getValueFromIntent() {
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
    }
    private void bindViews() {
        imageLoader = new PicassoLoader();
        studentImageView = findViewById(R.id.img_student);
        backBtn = findViewById(R.id.btn_back);
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new OnlineQuizzesAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        courseAssignmentView = new CourseAssignmentView(this, this);
    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(this)
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

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onGetCourseAssignmentSuccess(ArrayList<CourseAssignment> courseAssignmentArrayList) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        adapter.addData(courseAssignmentArrayList);
    }

    @Override
    public void onGetCourseAssignmentFailure(String message, int errorCode) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            logoutUser(this);
        }else {
            showErrorDialog(this);
        }
    }

    @Override
    public void onGetAssignmentDetailSuccess(ArrayList<AssignmentsDetail> assignmentsDetailArrayList,
                                             CourseAssignment courseAssignment) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        OpenQuizzesDetailsActivity(assignmentsDetailArrayList,courseAssignment);
    }

    private void OpenQuizzesDetailsActivity(ArrayList<AssignmentsDetail> assignmentsDetailArrayList,
                                            CourseAssignment courseAssignment) {
        Intent intent = new Intent(this,QuizzesDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_ASSIGNMENTS,assignmentsDetailArrayList);
        bundle.putSerializable(Constants.STUDENT,student);
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        if(courseAssignment.getCourseName() != null){
            intent.putExtra(Constants.KEY_COURSE_NAME,courseAssignment.getCourseName());
        }else {
            intent.putExtra(Constants.KEY_COURSE_NAME,"");
        }
        startActivity(intent);
    }

    @Override
    public void onGetAssignmentDetailFailure(String message, int errorCode) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        showErrorDialog(this);
    }

    @Override
    public void onItemClicked(CourseAssignment courseAssignment) {
        if(Util.isNetworkAvailable(this)){
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + "/api/courses/" +
                    courseAssignment.getId() + "/assignments";
            courseAssignmentView.getAssinmentDetail(url,courseAssignment);
        }else {
            Util.showNoInternetConnectionDialog(this);
        }

    }
}