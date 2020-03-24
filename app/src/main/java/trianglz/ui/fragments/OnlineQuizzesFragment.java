package trianglz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import trianglz.core.presenters.OnlineQuizzesPresenter;
import trianglz.core.views.OnlineQuizzesView;
import trianglz.models.QuizzCourse;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.OnlineQuizzesAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class OnlineQuizzesFragment extends Fragment implements View.OnClickListener, OnlineQuizzesAdapter.OnlineQuizzesInterface, OnlineQuizzesPresenter {
    private StudentMainActivity activity;
    private View rootView;
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private Student student;
    private RecyclerView recyclerView;
    private OnlineQuizzesAdapter adapter;
    // networking view
    private OnlineQuizzesView onlineQuizzesView;
    private SwipeRefreshLayout pullRefreshLayout;
    private LinearLayout skeletonLayout, placeholderLinearLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;
    private ArrayList<QuizzCourse> quizzCourses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_online_quizzes, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        getQuizzesCourses();
    }

    private void getQuizzesCourses() {
        showSkeleton(true);
        onlineQuizzesView.getQuizzesCourses(student.childId);

    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = Student.create(bundle.getString(Constants.STUDENT));
        }
    }

    private void bindViews() {
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        imageLoader = new PicassoLoader();
        quizzCourses = new ArrayList<>();
        studentImageView = rootView.findViewById(R.id.img_student);
        backBtn = rootView.findViewById(R.id.btn_back);
        setStudentImage(student.avatar, student.firstName + " " + student.lastName);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new OnlineQuizzesAdapter(activity, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        onlineQuizzesView = new OnlineQuizzesView(activity, this);
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        placeholderLinearLayout = rootView.findViewById(R.id.placeholder_layout);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private void setListeners() {
        backBtn.setOnClickListener(this);
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showSkeleton(true);
                pullRefreshLayout.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                placeholderLinearLayout.setVisibility(View.GONE);
                getQuizzesCourses();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.headerLayout.setVisibility(View.VISIBLE);
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onGetQuizzesCoursesSuccess(ArrayList<QuizzCourse> quizzCourses) {
        showSkeleton(false);
        showHidePlaceholder(quizzCourses);
    }

    @Override
    public void onGetQuizzesCoursesFailure(String message, int errorCode) {
        showSkeleton(false);
        this.quizzCourses = new ArrayList<>();
        showHidePlaceholder(quizzCourses);
        activity.showErrorDialog(activity, errorCode, "");
    }


    @Override
    public void onItemClicked(QuizzCourse quizzCourse) {
        openQuizzesDetailsActivity(quizzCourse);
    }

    private void openQuizzesDetailsActivity(QuizzCourse quizzCourse) {
        QuizzesDetailsFragment quizzesDetailsFragment = new QuizzesDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STUDENT, student.toString());
        bundle.putString(Constants.KEY_COURSE_QUIZZES, quizzCourse.toString());
        quizzesDetailsFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, quizzesDetailsFragment, "MenuFragments").
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
            skeletonLayout.setVisibility(View.VISIBLE);
            skeletonLayout.bringToFront();
        } else {
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            shimmer.setVisibility(View.GONE);
        }
    }

    private void showHidePlaceholder(ArrayList<QuizzCourse> quizzCourses) {
        if (quizzCourses.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholderLinearLayout.setVisibility(View.GONE);
            adapter.addData(quizzCourses);
        }
    }
}
