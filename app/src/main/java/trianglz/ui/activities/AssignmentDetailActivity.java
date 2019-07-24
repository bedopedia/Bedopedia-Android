package trianglz.ui.activities;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_detail);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void getValueFromIntent() {
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
        assignmentsDetailArrayList = (ArrayList<AssignmentsDetail>) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ASSIGNMENTS);
        courseName = getIntent().getStringExtra(Constants.KEY_COURSE_NAME);
    }


    private void bindViews() {
        imageLoader = new PicassoLoader();
        studentImageView = findViewById(R.id.img_student);
        backBtn = findViewById(R.id.btn_back);
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
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
        if (SessionManager.getInstance().getStudentAccount()) {
            segmentedGroup.setTintColor(Color.parseColor("#fd8268"));
        } else if (SessionManager.getInstance().getUserType()) {
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
    public void onItemClicked(AssignmentsDetail assignmentsDetail) {
        
    }
}
