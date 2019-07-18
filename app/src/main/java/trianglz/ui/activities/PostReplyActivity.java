package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reply);
        checkIntent();
        bindViews();
    }

    private void checkIntent() {
        postDetails = PostDetails.create(getIntent().getStringExtra(Constants.POST_DETAILS));
        ownerName = postDetails.getOwner().getName();
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
    }

    @Override
    public void onAttachmentClicked(ArrayList<UploadedObject> uploadedObjects) {

    }
}
