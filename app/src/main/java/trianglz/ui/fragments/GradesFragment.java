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
import trianglz.core.presenters.GradesPresenter;
import trianglz.core.views.GradesView;
import trianglz.models.PostsResponse;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.GradesAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 03/09/2019.
 */
public class GradesFragment extends Fragment implements GradesAdapter.GradesAdapterInterface, GradesPresenter {

    private View rootView;
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private RecyclerView recyclerView;
    private GradesAdapter gradesAdapter;
    private Toolbar toolbar;
    private Student student;
    private SwipeRefreshLayout pullRefreshLayout;
    private IImageLoader imageLoader;
    private GradesView gradesView;
    private StudentMainActivity activity;
    private LinearLayout skeletonLayout, placeholderLinearLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_grades, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        showSkeleton(true);
    }


    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable(Constants.STUDENT);
        }
    }

    private void bindViews() {
        gradesView = new GradesView(getActivity(), this);
        if (!activity.progress.isShowing()) activity.progress.show();
        gradesView.getGradesCourses(student.actableId);
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        backBtn = rootView.findViewById(R.id.btn_back);
        toolbar = rootView.findViewById(R.id.toolbar);
        studentImageView = rootView.findViewById(R.id.img_student);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        placeholderLinearLayout = rootView.findViewById(R.id.placeholder_layout);
        gradesAdapter = new GradesAdapter(activity, this);
        imageLoader = new PicassoLoader();
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        String studentName = student.firstName + " " + student.lastName;
        setStudentImage(student.getAvatar(), studentName);
        recyclerView.setAdapter(gradesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.headerLayout.setVisibility(View.VISIBLE);
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        });
    }
    private void setListeners() {
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showSkeleton(true);
                pullRefreshLayout.setRefreshing(false);
                placeholderLinearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
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
    public void onSubjectSelected(PostsResponse postsResponse) {
        openSelectGradingPeriodFragment(postsResponse);
    }

    private void openSelectGradingPeriodFragment(PostsResponse postsResponse) {

        SelectPeriodFragment selectPeriodFragment = new SelectPeriodFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_COURSE_GROUPS, postsResponse);
        bundle.putSerializable(Constants.STUDENT, student);
        selectPeriodFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, selectPeriodFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();

    }

    @Override
    public void onGetGradesCoursesSuccess(ArrayList<PostsResponse> arrayList) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        if (arrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholderLinearLayout.setVisibility(View.GONE);
            gradesAdapter.addData(arrayList);

        }
    }

    @Override
    public void onGetGradesCoursesFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        showSkeleton(false);
        activity.showErrorDialog(activity, errorCode, "");
    }

    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_row_layout, (ViewGroup) rootView, false);
                skeletonLayout.addView(rowLayout);
            }
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
            shimmer.showShimmer(true);
            skeletonLayout.setVisibility(View.VISIBLE);
            skeletonLayout.bringToFront();
        } else {
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            shimmer.setVisibility(View.GONE);
        }
    }
}
