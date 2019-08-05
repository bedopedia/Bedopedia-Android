package trianglz.ui.activities;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.SingleAssignmentPresenter;
import trianglz.core.views.SingleAssignmentView;
import trianglz.models.AssignmentsDetail;
import trianglz.models.SingleAssignment;
import trianglz.models.UploadedObject;
import trianglz.ui.adapters.AttachmentAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class AssignmentActivity extends SuperActivity implements AttachmentAdapter.AttachmentAdapterInterface, SingleAssignmentPresenter, View.OnClickListener {
    private AssignmentsDetail assignmentsDetail;
    private TextView courseNameTextView, courseDescriptionTextView;
    private RecyclerView recyclerView;
    private AttachmentAdapter adapter;
    private SingleAssignmentView singleAssignmentView;
    private int courseId;
    private AvatarView avatarView;
    private String studentName;
    private CardView cardView;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ReadIntent();
        bindViews();
        setListeners();
        showLoadingDialog();
        singleAssignmentView.showAssignment(courseId, assignmentsDetail.getId());
    }

    private void ReadIntent() {
        assignmentsDetail = AssignmentsDetail.create(getIntent().getStringExtra(Constants.KEY_ASSIGNMENT_DETAIL));
        courseId = getIntent().getIntExtra(Constants.KEY_COURSE_ID, 0);
        studentName = getIntent().getStringExtra(Constants.KEY_STUDENT_NAME);
    }


    private void bindViews() {
        backBtn = findViewById(R.id.btn_back);
        courseNameTextView = findViewById(R.id.tv_course_name);
        courseDescriptionTextView = findViewById (R.id.tv_course_description);
        cardView = findViewById (R.id.card_view);
        if (assignmentsDetail.getName() == null || assignmentsDetail.getName().isEmpty()) {
            courseNameTextView.setText(getResources().getString(R.string.assignments));
        } else {
            courseNameTextView.setText(assignmentsDetail.getName());
        }
        if (assignmentsDetail.getDescription() == null || assignmentsDetail.getDescription().isEmpty()) {
            cardView.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.VISIBLE);
            courseDescriptionTextView.setText(assignmentsDetail.getDescription());
        }
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new AttachmentAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(16,this),false));
        singleAssignmentView = new SingleAssignmentView(this, this);
        avatarView = findViewById(R.id.img_student);
        IImageLoader imageLoader = new PicassoLoader();
        imageLoader.loadImage(avatarView, new AvatarPlaceholderModified(studentName), "Path of Image");
        if (studentName == null || studentName.equals("") || studentName.isEmpty()) {
            avatarView.setVisibility(View.INVISIBLE);
        }
    }
    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onAttachmentClicked(String fileUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
        startActivity(browserIntent);
    }

    @Override
    public void onShowAssignmentSuccess(SingleAssignment singleAssignment) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        ArrayList<UploadedObject> uploadedObjects = new ArrayList<>(Arrays.asList(singleAssignment.getUploadedFiles()));
        adapter.addData(uploadedObjects);
    }

    @Override
    public void onShowAssignmentFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}