package trianglz.ui.activities;

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

import trianglz.core.presenters.PostDetailsPresenter;
import trianglz.core.views.PostDetailsView;
import trianglz.models.PostDetails;
import trianglz.ui.adapters.PostDetailsAdapter;
import trianglz.utils.Constants;

public class PostDetailActivity extends SuperActivity implements PostDetailsPresenter {

    private RecyclerView recyclerView;
    private PostDetailsView postDetailsView;
    private Toolbar toolbar;
    private TextView courseNameTextView;
    private PostDetailsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        bindViews();
        showLoadingDialog();
        postDetailsView.getPostDetails(getIntent().getIntExtra(Constants.KEY_COURSE_ID, 0));
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
        courseNameTextView.setText(getIntent().getStringExtra(Constants.KEY_COURSE_NAME));
        adapter = new PostDetailsAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void ongetPostDetailsSuccess(ArrayList<PostDetails> postDetails) {
        adapter.addData(postDetails);
        if (progress.isShowing()) progress.dismiss();
    }

    @Override
    public void ongetPostDetailsFailure() {
        if (progress.isShowing()) progress.dismiss();
        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
    }
}
