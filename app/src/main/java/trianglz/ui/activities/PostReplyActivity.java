package trianglz.ui.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.BottomItemDecoration;
import trianglz.components.HideKeyboardOnTouch;
import trianglz.components.TopItemDecoration;
import trianglz.models.PostDetails;
import trianglz.models.UploadedObject;
import trianglz.ui.adapters.PostReplyAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class PostReplyActivity extends AppCompatActivity implements PostReplyAdapter.PostReplyInterface {
    PostDetails postDetails;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView owner;
    private PostReplyAdapter adapter;
    private String ownerName;
    private EditText replyEditText;
    private LinearLayout rootView, inputLayout;
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
    }


    private void checkIntent() {
        postDetails = PostDetails.create(getIntent().getStringExtra(Constants.POST_DETAILS));
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
        replyEditText = findViewById (R.id.et_message);
        rootView = findViewById(R.id.root_view);
        inputLayout = findViewById(R.id.ll_input);
    }

    @Override
    public void onAttachmentClicked(ArrayList<UploadedObject> uploadedObjects) {

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
}
