package trianglz.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.core.presenters.PostsPresenter;
import trianglz.core.views.PostsView;
import trianglz.models.PostsResponse;
import trianglz.models.Student;
import trianglz.ui.adapters.PostsAdapter;
import trianglz.utils.Constants;

public class PostsActivity extends SuperActivity implements PostsPresenter, PostsAdapter.PostsInterface {

    private RecyclerView recyclerView;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    private PostsView postsView;
    private Student student;
    private String studentName="";
    private PostsAdapter adapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        getValuesFromIntent();
        bindViews();
        showLoadingDialog();
        setStudentImage(student.getAvatar(), studentName);
        postsView.getRecentPosts(getIntent().getIntExtra(Constants.KEY_STUDENT_ID,150));
    }

    private void getValuesFromIntent() {
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
        studentName= student.firstName + " " + student.lastName;
    }

    private void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        studentImageView = findViewById(R.id.img_student);
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
    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(this)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
                    .transform(new CircleTransform())
                    .into(studentImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }


    @Override
    public void onGetPostsSuccess(ArrayList<PostsResponse> parsePostsResponse) {
        if (progress.isShowing()) progress.dismiss();
        adapter.addData(parsePostsResponse);
    }

    @Override
    public void onGetPostsFailure(String message, int errorCode) {
        if (progress.isShowing()) progress.dismiss();
        showErrorDialog(this, errorCode,"");
    }

    @Override
    public void onCourseClicked(int courseId, String courseName) {
        Intent intent = new Intent(this, PostDetailActivity.class);
        intent.putExtra(Constants.KEY_COURSE_ID, courseId);
        intent.putExtra(Constants.KEY_COURSE_NAME, courseName);
        startActivity(intent);
    }

}
