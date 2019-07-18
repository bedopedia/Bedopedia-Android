package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import trianglz.core.views.PostDetailsView;
import trianglz.ui.adapters.PostDetailsAdapter;
import trianglz.utils.Constants;

public class PostReplyActivity extends AppCompatActivity {
    private RecyclerView postRecylceView, replyRecycleView;
    private Toolbar toolbar;
    private TextView courseNameTextView;
    private String subjectName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reply);
        bindViews();
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
        courseNameTextView = findViewById(R.id.tv_course_name);
        subjectName = getIntent().getStringExtra(Constants.KEY_COURSE_NAME);
        courseNameTextView.setText(subjectName);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
