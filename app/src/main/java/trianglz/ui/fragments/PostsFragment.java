package trianglz.ui.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.PostsAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 08/09/2019.
 */
public class PostsFragment extends Fragment implements PostsPresenter, PostsAdapter.PostsInterface {

    private StudentMainActivity activity;
    private View rootView;
    private RecyclerView recyclerView;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    private PostsView postsView;
    private Student student;
    private String studentName = "";
    private PostsAdapter adapter;
    private Toolbar toolbar;
    private int studentId;
    private SwipeRefreshLayout pullRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();

        rootView = inflater.inflate(R.layout.activity_posts, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValuesFromIntent();
        bindViews();
        setListeners();
        activity.showLoadingDialog();
        setStudentImage(student.getAvatar(), studentName);
    }



    private void getValuesFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable(Constants.STUDENT);
            studentName = student.firstName + " " + student.lastName;
            postsView = new PostsView(activity, this);
            studentId = bundle.getInt(Constants.KEY_STUDENT_ID, 150);
            postsView.getRecentPosts(bundle.getInt(Constants.KEY_STUDENT_ID, 150));
        }
    }

    private void bindViews() {
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        toolbar = rootView.findViewById(R.id.toolbar);
        studentImageView = rootView.findViewById(R.id.img_student);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.toolbarView.setVisibility(View.VISIBLE);
                    activity.headerLayout.setVisibility(View.VISIBLE);
                    getParentFragment().getChildFragmentManager().popBackStack();
                }
            });
        }
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new PostsAdapter(activity, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
    }

    private void setListeners() {
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                activity.showLoadingDialog();
                postsView.getRecentPosts(studentId);
            }
        });
    }
    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(activity)
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
        if (activity.progress.isShowing()) activity.progress.dismiss();
        adapter.addData(parsePostsResponse);
        pullRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onGetPostsFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode,"");
    }

    @Override
    public void onCourseClicked(int courseId, String courseName) {
        PostDetailFragment postDetailFragment = new PostDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_COURSE_ID, courseId);
        bundle.putString(Constants.KEY_COURSE_NAME, courseName);
        postDetailFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, postDetailFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }
}
