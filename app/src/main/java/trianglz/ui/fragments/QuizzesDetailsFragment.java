package trianglz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.QuizzesDetailsPresenter;
import trianglz.core.views.QuizzesDetailsView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.CourseGroups;
import trianglz.models.Meta;
import trianglz.models.QuizQuestion;
import trianglz.models.QuizzCourse;
import trianglz.models.Quizzes;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.QuizzesDetailsAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class QuizzesDetailsFragment extends Fragment implements View.OnClickListener, QuizzesDetailsPresenter, QuizzesDetailsAdapter.QuizzesDetailsInterface {

    private StudentMainActivity activity;
    private View rootView;
    private ImageButton backBtn;
    private AvatarView studentImageView;
    private IImageLoader imageLoader;
    private Student student;
    private RecyclerView recyclerView;
    private QuizzesDetailsAdapter adapter;
    private String courseName = "";
    private String courseGroupName = "";
    private TextView headerTextView, placeholderTextView;
    private QuizzCourse quizzCourse;
    private ArrayList<Quizzes> quizzes;
    private QuizzesDetailsView quizzesDetailsView;
    private CourseGroups courseGroup;
    private boolean teacherMode = false;
    private int page = 1;
    private int totalPages;
    private boolean isOpen = true;
    private SwipeRefreshLayout pullRefreshLayout;
    private int quizIndex = 0;
    private ArrayList<Quizzes> isToBeSubmittedQuizzes;
    private LinearLayout placeholderLinearLayout;
    private TabLayout tabLayout;
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;
    private boolean isCalling = false;
    private QuizQuestion quizQuestion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_quizzes_details, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        if (!teacherMode) {
            getQuizzes();
        } else {
            getTeacherQuizzes();
        }
    }

    private void getTeacherQuizzes() {
        quizzesDetailsView.getTeacherQuizzes(courseGroup.getId() + "");
        showSkeleton(true);
        isCalling = true;
    }


    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getBoolean(Constants.KEY_TEACHERS, false)) {
                quizzes = bundle.getParcelableArrayList(Constants.KEY_QUIZZES);
                teacherMode = true;
                courseName = bundle.getString(Constants.KEY_COURSE_NAME);
                courseGroupName = bundle.getString(Constants.KEY_COURSE_GROUP_NAME);
                courseGroup = CourseGroups.create(bundle.getString(Constants.KEY_COURSE_GROUPS));
                return;
            }
            student = Student.create(bundle.getString(Constants.STUDENT));
            quizzCourse = QuizzCourse.create(bundle.getString(Constants.KEY_COURSE_QUIZZES));
//        assignmentsDetailArrayList = (ArrayList<AssignmentsDetail>) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ASSIGNMENTS);
            courseName = quizzCourse.getCourseName();
        }
    }

    private void bindViews() {
        imageLoader = new PicassoLoader();
        studentImageView = rootView.findViewById(R.id.img_student);
        backBtn = rootView.findViewById(R.id.btn_back);
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        placeholderTextView = rootView.findViewById(R.id.placeholder_tv);
        tabLayout = rootView.findViewById(R.id.tab_layout);
        if (!teacherMode)
            setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        if (teacherMode) {
            adapter = new QuizzesDetailsAdapter(activity, this, courseGroupName);
        } else {
            adapter = new QuizzesDetailsAdapter(activity, this, courseName);

        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(10, activity), false));
        headerTextView = rootView.findViewById(R.id.tv_header);
        isToBeSubmittedQuizzes = new ArrayList<>();
        headerTextView.setText(courseName);

        placeholderLinearLayout = rootView.findViewById(R.id.placeholder_layout);
        tabLayout.setSelectedTabIndicatorColor(activity.getResources().getColor(Util.checkUserColor()));
        tabLayout.setTabTextColors(activity.getResources().getColor(R.color.steel), activity.getResources().getColor(Util.checkUserColor()));

        quizzesDetailsView = new QuizzesDetailsView(activity, this);
        quizzes = new ArrayList<>();

        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    private void setListeners() {
        backBtn.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                recyclerView.scrollToPosition(0);
                if (tab.getPosition() == 0)
                    isOpen = true;
                else
                    isOpen = false;
                if (!isCalling)
                    showHidePlaceholder(getArrayList());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.setRefreshing(false);
                if (teacherMode)
                    getTeacherQuizzes();
                else
                    getQuizzes();
                showSkeleton(true);
                placeholderLinearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });

    }

    private ArrayList<Quizzes> getArrayList() {
        if (quizzes.isEmpty()) return quizzes;
        ArrayList<Quizzes> filteredQuizzes = new ArrayList<>();
        for (Quizzes quiz : quizzes) {
            if (quiz.getState().equals("running")) {
                if (isOpen) filteredQuizzes.add(quiz);
            } else {
                if (!isOpen) filteredQuizzes.add(quiz);
            }
        }
        return filteredQuizzes;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onGetQuizzesDetailsSuccess(ArrayList<Quizzes> quizzes, Meta meta) {
        this.quizzes.addAll(quizzes);
        populateIsToSubmitArray();
        totalPages = meta.getTotalPages();
        isCalling = false;
        showSkeleton(false);
        populateIsToSubmitArray();
        showHidePlaceholder(getArrayList());
        if (!isToBeSubmittedQuizzes.isEmpty()) {
//            submitQuizzes(isToBeSubmittedQuizzes.get(quizIndex).getStudentSubmissions().getId());
        }
    }


    @Override
    public void onGetQuizzesDetailsFailure(String message, int errorCode) {
        isCalling = false;
        showHidePlaceholder(quizzes);
        showSkeleton(false);
        activity.showErrorDialog(activity, errorCode, "");

    }

    @Override
    public void onSubmitQuizSuccess() {
        quizIndex++;
        if (quizIndex < isToBeSubmittedQuizzes.size()) {
//            submitQuizzes(isToBeSubmittedQuizzes.get(quizIndex).getStudentSubmissions().getId());
        } else {
            if (activity.progress.isShowing()) {
                activity.progress.dismiss();
            }
            getQuizzes();
        }
    }

    @Override
    public void onSubmitQuizFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }

    }

    @Override
    public void onGetQuizQuestionsSuccess(QuizQuestion quizQuestion) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        this.quizQuestion = quizQuestion;
        openQuizDetailFragment();
    }

    @Override
    public void onGetQuizQuestionsFailure(String message, int errorCode) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, "");
    }

    @Override
    public void onGetTeacherQuizzesSuccess(ArrayList<Quizzes> quizzes) {
        this.quizzes.addAll(quizzes);
        isCalling = false;
        showSkeleton(false);
        showHidePlaceholder(getArrayList());
    }

    @Override
    public void onGetTeacherQuizzesFailure(String message, int errorCode) {
        isCalling = false;
        showSkeleton(false);
        activity.showErrorDialog(activity, errorCode, "");
    }

    @Override
    public void onItemClicked(Quizzes quizzes) {
        if (!teacherMode) {
//            SingleQuizFragment singleQuizFragment = new SingleQuizFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString(Constants.KEY_QUIZZES, quizzes.toString());
//            bundle.putString(Constants.KEY_COURSE_QUIZZES, quizzCourse.toString());
//            bundle.putString(Constants.STUDENT, student.toString());
//            singleQuizFragment.setArguments(bundle);
//            getParentFragment().getChildFragmentManager().
//                    beginTransaction().add(R.id.menu_fragment_root, singleQuizFragment, "MenuFragments").
//                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
//                    addToBackStack(null).commit();
                String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getQuizQuestions(quizzes.getId());
                quizzesDetailsView.getQuizQuestions(url);
                activity.showLoadingDialog();
        } else {
            GradingFragment gradingFragment = new GradingFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_QUIZ_ID, quizzes.getId());
            if (courseGroup != null) {
                bundle.putInt(Constants.KEY_COURSE_ID, courseGroup.getCourseId());
                bundle.putInt(Constants.KEY_COURSE_GROUP_ID, courseGroup.getId());
            }
            bundle.putBoolean(Constants.KEY_ASSIGNMENTS_GRADING, false);
            bundle.putString(Constants.KEY_QUIZ_NAME, quizzes.getName());
            gradingFragment.setArguments(bundle);
            getParentFragment().getChildFragmentManager().
                    beginTransaction().add(R.id.course_root, gradingFragment, "CoursesFragments").
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(null).commit();
        }
    }

    private void getQuizzes() {
        if (Util.isNetworkAvailable(getActivity())) {
            if (page == 1)
                showSkeleton(true);
            isCalling = true;
            quizzesDetailsView.getQuizzesDetails(student.getId(), quizzCourse.getId(), page);
        } else {
            Util.showNoInternetConnectionDialog(getActivity());
        }
    }

    private void submitQuizzes(int submissionId) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.submitQuiz();
        quizzesDetailsView.submitQuiz(url, submissionId);
    }

    @Override
    public void onReachPosition() {
        page++;
        if (page <= totalPages) {
            getQuizzes();
        }
    }

    private void populateIsToSubmitArray() {
        isToBeSubmittedQuizzes.clear();
        quizIndex = 0;
        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).getState().equals("running") && quizzes.get(i).getStudentSubmissions() != null) {
                if (!quizzes.get(i).getStudentSubmissions().getIsSubmitted()) {
                    if (calculateTimerDuration(quizzes.get(i).getDuration(), quizzes.get(i).getStudentSubmissions().getCreatedAt()) <= 0) {
                        isToBeSubmittedQuizzes.add(quizzes.get(i));
                    }
                }
            }
        }
    }

    private long calculateTimerDuration(long quizDuration, String createdAt) {
        long durationLeft = 0, timeElapsed;
        quizDuration = quizDuration * 1000; //seconds convert
        Date createdAtDate = Util.convertIsoToDate(createdAt);
        timeElapsed = System.currentTimeMillis() - createdAtDate.getTime();
        if (timeElapsed > quizDuration) {
            return 0;
        } else {
            durationLeft = quizDuration - timeElapsed;
            return durationLeft;
        }
    }

    private void showHidePlaceholder(ArrayList<Quizzes> quizzes) {
        if (quizzes.isEmpty()) {
            if (isOpen) {
                placeholderTextView.setText(activity.getResources().getString(R.string.open_assignments_placeholder));
            } else {
                placeholderTextView.setText(activity.getResources().getString(R.string.closed_assignments_placeholder));
            }
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholderLinearLayout.setVisibility(View.GONE);
            adapter.addData(quizzes);
        }
    }

    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_assignment_layout, (ViewGroup) rootView, false);
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

    private void openQuizDetailFragment() {
        QuizDetailsFragment quizDetailsFragment = new QuizDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_QUIZ_QUESTION, quizQuestion.toString());
        quizDetailsFragment.setArguments(bundle);
        getParentFragment().getChildFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, quizDetailsFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }
}
