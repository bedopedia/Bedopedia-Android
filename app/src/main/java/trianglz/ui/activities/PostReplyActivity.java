package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import trianglz.models.PostDetails;
import trianglz.utils.Constants;

public class PostReplyActivity extends AppCompatActivity {
    PostDetails postDetails;
    private RecyclerView postRecylceView, replyRecycleView;
    private Toolbar toolbar;
    private TextView owner;
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
        postRecylceView = findViewById(R.id.recycler_view);
        replyRecycleView = findViewById(R.id.reply_recycler);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        owner = findViewById(R.id.tv_course_name);
        owner.setText(ownerName);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
