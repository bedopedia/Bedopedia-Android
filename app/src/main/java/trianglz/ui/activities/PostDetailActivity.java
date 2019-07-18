package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.PostDetailsPresenter;
import trianglz.core.views.PostDetailsView;
import trianglz.models.PostDetails;
import trianglz.utils.Constants;

public class PostDetailActivity extends AppCompatActivity implements PostDetailsPresenter {

    private RecyclerView recyclerView;
    private PostDetailsView postDetailsView;
    private Toolbar toolbar;
    private TextView courseNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        bindViews();
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
    }

    @Override
    public void ongetPostDetailsSuccess(ArrayList<PostDetails> postDetails) {

    }

    @Override
    public void ongetPostDetailsFailure() {

    }
}
