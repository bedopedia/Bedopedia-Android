package trianglz.ui.fragments;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.TopItemDecoration;
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
    private LinearLayout skeletonLayout, placeholderLinearLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;
    private ImageButton backButton;
    private ArrayList<PostsResponse> parsePostsResponse;

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
        getRecentPosts();
        setStudentImage(student.getAvatar(), studentName);
    }


    private void getValuesFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable(Constants.STUDENT);
            studentName = student.firstName + " " + student.lastName;
            postsView = new PostsView(activity, this);
            studentId = bundle.getInt(Constants.KEY_STUDENT_ID, 150);
        }
    }

    private void bindViews() {
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        toolbar = rootView.findViewById(R.id.toolbar);
        this.parsePostsResponse = new ArrayList<>();
        placeholderLinearLayout = rootView.findViewById(R.id.placeholder_layout);
        studentImageView = rootView.findViewById(R.id.img_student);
        backButton = rootView.findViewById(R.id.btn_back);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new PostsAdapter(activity, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void setListeners() {
        pullRefreshLayout.setOnRefreshListener(() -> {
            getRecentPosts();
            pullRefreshLayout.setRefreshing(false);
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.GONE);
        });
        backButton.setOnClickListener(v -> {
            activity.toolbarView.setVisibility(View.VISIBLE);
            activity.headerLayout.setVisibility(View.VISIBLE);
            getParentFragment().getChildFragmentManager().popBackStack();
        });
    }

    private void getRecentPosts() {
        showSkeleton(true);
        postsView.getRecentPosts(studentId);
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
        showSkeleton(false);
        this.parsePostsResponse = parsePostsResponse;
        showHidePlaceholder(parsePostsResponse);
    }

    @Override
    public void onGetPostsFailure(String message, int errorCode) {
        showSkeleton(false);
        activity.showErrorDialog(activity, errorCode, "");
        this.parsePostsResponse = new ArrayList<>();
        showHidePlaceholder(parsePostsResponse);
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

    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_course_layout, (ViewGroup) rootView, false);
                skeletonLayout.addView(rowLayout);
            }
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
            shimmer.showShimmer(true);
            recyclerView.setVisibility(View.GONE);
            skeletonLayout.setVisibility(View.VISIBLE);
            skeletonLayout.bringToFront();
        } else {
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            recyclerView.setVisibility(View.VISIBLE);
            shimmer.setVisibility(View.GONE);
        }
    }

    private void showHidePlaceholder(ArrayList<PostsResponse> parsePostsResponse) {
        if (parsePostsResponse.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholderLinearLayout.setVisibility(View.GONE);
            adapter.addData(parsePostsResponse);
        }
    }
}
