package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import trianglz.ui.adapters.CourseAssignmentAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class CourseAssignmentActivity extends SuperActivity implements View.OnClickListener, CourseAssignmentPresenter, CourseAssignmentAdapter.CourseAssignmentAdapterInterface {
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private Student student;
    private RecyclerView recyclerView;
    private CourseAssignmentAdapter courseAssignmentAdapter;
    private CourseAssignmentView courseAssignmentView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_assignment);
        getValueFromIntent();
        bindViews();
        setListeners();
        getCourseAssignment();
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
        courseAssignmentAdapter = new CourseAssignmentAdapter(this,this);
        recyclerView.setAdapter(courseAssignmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        courseAssignmentView = new CourseAssignmentView(this,this);
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
        }
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

    @Override
    public void onGetCourseAssignmentSuccess(ArrayList<CourseAssignment> courseAssignmentArrayList) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        courseAssignmentAdapter.addData(courseAssignmentArrayList);
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
        openAssignmentDetailActivity(assignmentsDetailArrayList,courseAssignment);
    }

    private void openAssignmentDetailActivity(ArrayList<AssignmentsDetail> assignmentsDetailArrayList,
                                              CourseAssignment courseAssignment) {
        Intent intent = new Intent(this,AssignmentDetailActivity.class);
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
