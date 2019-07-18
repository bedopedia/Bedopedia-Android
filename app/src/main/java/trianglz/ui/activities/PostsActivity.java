package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.PostsPresenter;
import trianglz.core.views.PostsView;
import trianglz.models.PostsResponse;

public class PostsActivity extends SuperActivity implements PostsPresenter {

    private RecyclerView recyclerView;
    private PostsView postsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        bindViews();
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        postsView = new PostsView(this, this);
        showLoadingDialog();
        postsView.getRecentPosts();
    }

    @Override
    public void onGetPostsSuccess(ArrayList<PostsResponse> parsePostsResponse) {
        if (progress.isShowing()) progress.dismiss();
    }

    @Override
    public void onGetPostsFailure(String message, int errorCode) {
        if (progress.isShowing()) progress.dismiss();
    }
}
