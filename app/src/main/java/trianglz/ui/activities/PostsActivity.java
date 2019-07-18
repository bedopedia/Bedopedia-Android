package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.PostsPresenter;
import trianglz.core.views.PostsView;
import trianglz.models.PostsResponse;
import trianglz.ui.adapters.PostsAdapter;
import trianglz.utils.Constants;

public class PostsActivity extends SuperActivity implements PostsPresenter {

    private RecyclerView recyclerView;
    private PostsView postsView;
    private PostsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        bindViews();
        showLoadingDialog();
        postsView.getRecentPosts(getIntent().getIntExtra(Constants.KEY_STUDENT_ID,150));
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        postsView = new PostsView(this, this);
        adapter = new PostsAdapter(this);
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
}
