package trianglz.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.BottomItemDecoration;
import trianglz.components.HideKeyboardOnTouch;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.PostReplyPresenter;
import trianglz.core.views.PostReplyView;
import trianglz.managers.SessionManager;
import trianglz.models.PostDetails;
import trianglz.models.UploadedObject;
import trianglz.ui.AttachmentsActivity;
import trianglz.ui.adapters.PostReplyAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class PostReplyActivity extends SuperActivity implements PostReplyAdapter.PostReplyInterface, PostReplyPresenter, View.OnClickListener {
    PostDetails postDetails;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView owner;
    private PostReplyAdapter adapter;
    private String ownerName;
    private LinearLayout rootView, inputLayout;
    private Button sendReplyButton;
    private EditText replyEditText;
    private String courseName = "Course";

    private PostReplyView postReplyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reply);
        checkIntent();
        bindViews();
        setListeners();
    }

    private void setListeners() {
        rootView.setOnTouchListener(new HideKeyboardOnTouch(this));
        sendReplyButton.setOnClickListener(this);
    }


    private void checkIntent() {
        postDetails = PostDetails.create(getIntent().getStringExtra(Constants.POST_DETAILS));
        courseName = getIntent().getStringExtra(Constants.KEY_COURSE_NAME);
        ownerName = postDetails.getOwner().getName();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(rootView.getWindowToken(), 0);

    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        owner = findViewById(R.id.tv_course_name);
        owner.setText(ownerName);
        adapter = new PostReplyAdapter(this, this);
        recyclerView.setAdapter(adapter);
        adapter.addData(postDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(16,this),false));
        recyclerView.addItemDecoration(new BottomItemDecoration((int) Util.convertDpToPixel(50,this),false));
        rootView = findViewById(R.id.root_view);
        inputLayout = findViewById(R.id.ll_input);

        postReplyView = new PostReplyView(this,this);
        sendReplyButton = findViewById (R.id.send);
        replyEditText = findViewById(R.id.et_reply);
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
        intent.putExtra(Constants.KEY_COURSE_NAME, courseName);
        startActivity(intent);
    }

    @Override
    public void onReplyClicked() {
        inputLayout.setVisibility(View.VISIBLE);
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        replyEditText.requestFocus();
    }

    @Override
    public void onBackPressed() {
        inputLayout.setVisibility(View.GONE);
        super.onBackPressed();
    }

    @Override
    public void onPostReplySuccess() {
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        progress.dismiss();
    }

    @Override
    public void onPostReplyFailure(String message, int errorCode) {
        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        progress.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                if (!replyEditText.getText().toString().isEmpty()) {
                    showLoadingDialog();
                    postReplyView.postReply(replyEditText.getText().toString(), SessionManager.getInstance().getUserId(), postDetails.getId());
                }
                break;
        }
    }
}
