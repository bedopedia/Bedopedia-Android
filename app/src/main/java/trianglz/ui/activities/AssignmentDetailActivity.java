package trianglz.ui.activities;

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
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.AdapterPaginationInterface;
import trianglz.core.presenters.AssignmentsDetailPresenter;
import trianglz.core.views.AssignmentsDetailView;
import trianglz.managers.SessionManager;
import trianglz.models.PendingAssignment;
import trianglz.models.Student;
import trianglz.ui.adapters.PendingAssignmentAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class AssignmentDetailActivity extends SuperActivity implements View.OnClickListener, AssignmentsDetailPresenter, AdapterPaginationInterface {
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private Student student;
    private RecyclerView recyclerView;
    private PendingAssignmentAdapter pendingAssignmentAdapter;
    private AssignmentsDetailView assignmentsDetailView;
    private int pageNumber;
    private boolean newIncomingAssignmentData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_detail);
        getValueFromIntent();
        bindViews();
        setListeners();
        getAssignmentDetail(false);
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
        pendingAssignmentAdapter = new PendingAssignmentAdapter(this,this);
        recyclerView.setAdapter(pendingAssignmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(20,this),false));
        assignmentsDetailView = new AssignmentsDetailView(this,this);
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

    @Override
    public void onGetAssignmentDetailSuccess(ArrayList<PendingAssignment> pendingAssignmentArrayList) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        newIncomingAssignmentData = pendingAssignmentArrayList.size() != 0;
        pendingAssignmentAdapter.addData(pendingAssignmentArrayList, newIncomingAssignmentData);

    }

    @Override
    public void onGetAssignmentDetailFailure(String message, int errorCode) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            logoutUser(this);
        }else {
            showErrorDialog(this);
        }
    }


    private void getAssignmentDetail(boolean pagination) {
        if (Util.isNetworkAvailable(this)) {
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + "/api/users/" +
                    SessionManager.getInstance().getUserId() + "/assignments";
            if (!pagination) {
                pageNumber = 1;
            }
            assignmentsDetailView.getAssignmentDetails(url, pageNumber,10+"");
        } else {
            Util.showNoInternetConnectionDialog(this);
        }
    }

    @Override
    public void onReachPosition() {
        pageNumber++;
        getAssignmentDetail(true);
    }
}
