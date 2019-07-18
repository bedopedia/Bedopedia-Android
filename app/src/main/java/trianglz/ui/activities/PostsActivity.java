package trianglz.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.PostsPresenter;
import trianglz.core.views.PostsView;
import trianglz.models.PostDetails;
import trianglz.models.PostsResponse;
import trianglz.ui.adapters.PostsAdapter;
import trianglz.utils.Constants;

public class PostsActivity extends SuperActivity implements PostsPresenter, PostsAdapter.PostsInterface {

    private RecyclerView recyclerView;
    private PostsView postsView;
    private PostsAdapter adapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        bindViews();
        showLoadingDialog();
        postsView.getRecentPosts(getIntent().getIntExtra(Constants.KEY_STUDENT_ID,150));
    }

    private void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        recyclerView = findViewById(R.id.recycler_view);
        postsView = new PostsView(this, this);
        adapter = new PostsAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void onGetPostsSuccess(ArrayList<PostsResponse> parsePostsResponse) {
        if (progress.isShowing()) progress.dismiss();
        adapter.addData(parsePostsResponse);
    }

    @Override
    public void onGetPostsFailure(String message, int errorCode) {
        if (progress.isShowing()) progress.dismiss();
        showErrorDialog(this);
    }

    @Override
    public void onCourseClicked(int courseId, String courseName) {
        Intent intent = new Intent(this, PostDetailActivity.class);
        intent.putExtra(Constants.KEY_COURSE_ID, courseId);
        intent.putExtra(Constants.KEY_COURSE_NAME, courseName);
        startActivity(intent);
    }
}
