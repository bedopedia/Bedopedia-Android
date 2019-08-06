package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.skolera.skolera_android.R;

import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.CreateTeacherPostPresenter;
import trianglz.core.views.CreateTeacherPostView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.PostDetails;
import trianglz.ui.adapters.AttachmentAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class CreateTeacherPostActivity extends SuperActivity implements CreateTeacherPostPresenter, View.OnClickListener, AttachmentAdapter.AttachmentAdapterInterface {
    private Button uploadBtn, postBtn;
    private EditText postEditText;
    private ImageButton closeBtn;
    private RecyclerView recyclerView;
    private LinearLayout attachmentLayout;
    private int courseGroupId;
    private AttachmentAdapter adapter;
    private CreateTeacherPostView createTeacherPostView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teacher_post);
        bindViews();
        setListeners();
        getValuesFromIntent();
    }

    private void bindViews() {
        createTeacherPostView = new CreateTeacherPostView(this, this);
        uploadBtn = findViewById(R.id.upload_file_btn);
        postBtn = findViewById(R.id.post_btn);
        closeBtn = findViewById(R.id.close_btn);
        postEditText = findViewById(R.id.post_edit_text);
        attachmentLayout = findViewById(R.id.attachment_layout);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new AttachmentAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(16, this), false));
    }

    private void setListeners() {
        uploadBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_btn:
                onBackPressed();
                break;
            case R.id.post_btn:
                createPost();
                break;
        }
    }

    @Override
    public void onAttachmentClicked(String fileUrl) {

    }

    @Override
    public void onPostCreatedSuccess(PostDetails post) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void onPostCreatedFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        if (errorCode == 401 || errorCode == 500) {
            logoutUser(this);
        } else {
            showErrorDialog(this);
        }
    }

    void createPost() {
      //  showLoadingDialog();
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createPostCourseGroup();
        Log.d("URL", "createPost: "+url);
        createTeacherPostView.createPost(url, postEditText.getText().toString(), Integer.parseInt(SessionManager.getInstance().getUserId()), courseGroupId, "CourseGroup");
    }

    private void getValuesFromIntent() {
        courseGroupId = getIntent().getIntExtra(Constants.KEY_COURSE_GROUP_ID, 0);
    }
}
