package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.BottomItemDecoration;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.PostDetailsPresenter;
import trianglz.core.views.PostDetailsView;
import trianglz.managers.SessionManager;
import trianglz.models.PostDetails;
import trianglz.models.UploadedObject;
import trianglz.ui.AttachmentsActivity;
import trianglz.ui.adapters.PostDetailsAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class PostDetailActivity extends SuperActivity implements PostDetailsPresenter, View.OnClickListener, PostDetailsAdapter.PostDetailsInterface {

    private RecyclerView recyclerView;
    private PostDetailsView postDetailsView;
    private Toolbar toolbar;
    private TextView courseNameTextView;
    private PostDetailsAdapter adapter;
    private String subjectName;
    private FloatingActionButton addPostFab;
    private boolean isStudent,isParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        bindViews();
        setListeners();
    }

    private void setListeners() {
        addPostFab.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog();
        //todo gets only the student post not course group posts for teacher
        postDetailsView.getPostDetails(getIntent().getIntExtra(Constants.KEY_COURSE_ID, 0));
    }

    private void bindViews() {
        isStudent = SessionManager.getInstance().getStudentAccount();
        isParent = SessionManager.getInstance().getUserType();
        addPostFab=findViewById(R.id.add_post_btn);
        recyclerView = findViewById(R.id.recycler_view);
        postDetailsView = new PostDetailsView(this, this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (!isStudent &&!isParent){
            addPostFab.setVisibility(View.VISIBLE);
        }
        courseNameTextView = findViewById(R.id.tv_course_name);
        subjectName = getIntent().getStringExtra(Constants.KEY_COURSE_NAME);
        courseNameTextView.setText(subjectName);
        adapter = new PostDetailsAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(16,this),false));
        recyclerView.addItemDecoration(new BottomItemDecoration((int) Util.convertDpToPixel(16,this),false));
    }

    @Override
    public void ongetPostDetailsSuccess(ArrayList<PostDetails> postDetails) {
        adapter.addData(postDetails);
        if (progress.isShowing()) progress.dismiss();
    }

    @Override
    public void ongetPostDetailsFailure() {
        if (progress.isShowing()) progress.dismiss();
    }

    @Override
    public void onAttachmentClicked(ArrayList<UploadedObject> uploadedObjects) {
        Intent intent = new Intent(this, AttachmentsActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<String> uploadedObjectStrings = new ArrayList<>();
        for (UploadedObject uploadedObject : uploadedObjects) {
            uploadedObjectStrings.add(uploadedObject.toString());
        }
        if (!uploadedObjectStrings.isEmpty()) bundle.putStringArrayList(Constants.KEY_UPLOADED_OBJECTS, uploadedObjectStrings);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        intent.putExtra(Constants.KEY_COURSE_NAME, subjectName);
        startActivity(intent);
    }

    @Override
    public void onCardClicked(PostDetails postDetails) {
        Intent intent = new Intent(this, PostReplyActivity.class);
        intent.putExtra(Constants.KEY_COURSE_NAME, subjectName);
        intent.putExtra(Constants.POST_DETAILS, postDetails.toString());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_post_btn:
                openCreatePostActivity();
                break;
        }
    }

    private void openCreatePostActivity() {
        Intent intent = new Intent(this, CreateTeacherPostActivity.class);
        startActivity(intent);
    }
}
