package trianglz.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.BottomItemDecoration;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.PostDetailsPresenter;
import trianglz.core.views.PostDetailsView;
import trianglz.models.PostDetails;
import trianglz.models.UploadedObject;
import trianglz.ui.AttachmentsActivity;
import trianglz.ui.adapters.PostDetailsAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class PostDetailActivity extends SuperActivity implements PostDetailsPresenter, PostDetailsAdapter.PostDetailsInterface {

    private RecyclerView recyclerView;
    private PostDetailsView postDetailsView;
    private Toolbar toolbar;
    private TextView courseNameTextView;
    private PostDetailsAdapter adapter;
    private String subjectName;
    private int courseId;
    private int lastPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        bindViews();
        setListeners();
        courseId = getIntent().getIntExtra(Constants.KEY_COURSE_ID, 0);
    }

    private void setListeners() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog();
        postDetailsView.getPostDetails(courseId,1);
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        postDetailsView = new PostDetailsView(this, this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
    public void loadNextPage(int page) {
        if (lastPage != page) {
            postDetailsView.getPostDetails(courseId,page);
            lastPage = page;
        }

    }

    @Override
    public void onCardClicked(PostDetails postDetails) {
        Intent intent = new Intent(this, PostReplyActivity.class);
        intent.putExtra(Constants.KEY_COURSE_NAME, subjectName);
        intent.putExtra(Constants.POST_DETAILS, postDetails.toString());
        startActivity(intent);
    }
}
