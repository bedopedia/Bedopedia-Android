package trianglz.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.GradeFeedbackDialog;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.GradingPresenter;
import trianglz.core.views.GradingView;
import trianglz.managers.SessionManager;
import trianglz.models.Feedback;
import trianglz.models.GradeModel;
import trianglz.models.StudentSubmission;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.StudentGradeAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class GradingFragment extends Fragment implements View.OnClickListener, StudentGradeAdapter.StudentGradeInterface, GradeFeedbackDialog.GradeDialogInterface, GradingPresenter {

    private StudentMainActivity activity;
    private View rootView;
    private StudentGradeAdapter adapter;
    private RecyclerView recyclerView;
    private ImageButton backBtn;
    private GradeFeedbackDialog gradeFeedbackDialog;
    private GradingView gradingView;
    private int courseId, courseGroupId, assignmentId, quizId;
    private boolean isAssignmentsGrading;
    private String headerString;
    private String feedBack;
    private TextView headerTextView;
    private SwipeRefreshLayout pullRefreshLayout;
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;
    private StudentSubmission submission;
    private ArrayList<StudentSubmission> studentSubmissions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_assignment_grading, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        fetchData();
        showSkeleton(true);
    }


    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isAssignmentsGrading = bundle.getBoolean(Constants.KEY_ASSIGNMENTS_GRADING, true);
            if (isAssignmentsGrading) {
                courseId = bundle.getInt(Constants.KEY_COURSE_ID, -1);
                courseGroupId = bundle.getInt(Constants.KEY_COURSE_GROUP_ID, -1);
                assignmentId = bundle.getInt(Constants.KEY_ASSIGNMENT_ID, -1);
                headerString = bundle.getString(Constants.KEY_ASSIGNMENT_NAME);
            } else {
                courseGroupId = bundle.getInt(Constants.KEY_COURSE_GROUP_ID, -1);
                assignmentId = bundle.getInt(Constants.KEY_ASSIGNMENT_ID, -1);
                quizId = bundle.getInt(Constants.KEY_QUIZ_ID, -1);
                headerString = bundle.getString(Constants.KEY_QUIZ_NAME);
            }
        }
    }

    private void bindViews() {
        backBtn = rootView.findViewById(R.id.btn_back);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false));
        adapter = new StudentGradeAdapter(activity, this);
        recyclerView.setAdapter(adapter);
        gradingView = new GradingView(activity, this);
        headerTextView = rootView.findViewById(R.id.tv_header);
        if (!headerString.isEmpty()) {
            headerTextView.setText(headerString);
        } else {
            if (isAssignmentsGrading) {
                headerTextView.setText(getResources().getString(R.string.assignment));
            } else {
                headerTextView.setText(getResources().getString(R.string.quiz));
            }
        }
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));

        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void setListeners() {
        backBtn.setOnClickListener(this);
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.setRefreshing(false);
                fetchData();
                showSkeleton(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onSubmitClicked(String grade, String feedBack, int studentId) {
        if (isAssignmentsGrading) {
            gradeFeedbackDialog.dismiss();
            GradeModel gradeModel = new GradeModel();
            gradeModel.setAssignmentId(assignmentId);
            gradeModel.setCourseGroupId(courseGroupId);
            gradeModel.setCourseId(courseId);
            gradeModel.setStudentId(studentId);
            gradeModel.setGrade(Double.valueOf(grade));
            activity.showLoadingDialog();
            gradingView.postAssignmentGrade(gradeModel);
        } else {
            gradeFeedbackDialog.dismiss();
            GradeModel gradeModel = new GradeModel();
            gradeModel.setQuizId(quizId);
            gradeModel.setCourseGroupId(courseGroupId);
            gradeModel.setCourseId(courseId);
            gradeModel.setStudentId(studentId);
            gradeModel.setScore(Double.valueOf(grade));
            activity.showLoadingDialog();
            gradingView.postQuizGrade(gradeModel);
        }

        this.feedBack = feedBack;
    }

    @Override
    public void onGetAssignmentSubmissionsSuccess(ArrayList<StudentSubmission> submissions) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        showSkeleton(false);
        this.studentSubmissions = submissions;
        adapter.addData(submissions);
    }

    @Override
    public void onGetAssignmentSubmissionsFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        showSkeleton(false);
        activity.showErrorDialog(activity, errorCode, "");
    }

    @Override
    public void onPostAssignmentGradeSuccess(StudentSubmission studentSubmission) {
        if (feedBack != null) {
                String userId = SessionManager.getInstance().getUserId();
                Feedback feedback = new Feedback();
                feedback.setContent(feedBack);
                feedback.setOwnerId(Integer.valueOf(userId));
                feedback.setOnId(studentSubmission.getAssignmentId());
                feedback.setOnType("Assignment");
                feedback.setToId(studentSubmission.getStudentId());
                feedback.setToType("Student");
                activity.showLoadingDialog();
                for (StudentSubmission listSubmission : studentSubmissions) {
                    if (listSubmission.getId() == studentSubmission.getId()) {
                        feedback.setId((listSubmission.getFeedback() != null) ? listSubmission.getFeedback().getId() : null);
                        break;
                    }
                }
                if (feedback.getId() == null) {
                    gradingView.postSubmissionFeedback(feedback, true);
                } else {
                    gradingView.postSubmissionFeedback(feedback,false);
                }
        } else {
            fetchData();
        }
    }

    @Override
    public void onPostAssignmentGradeFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, "");
    }

    @Override
    public void onGetQuizzesSubmissionsSuccess(ArrayList<StudentSubmission> studentSubmissions) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        showSkeleton(false);
        adapter.setIsQuizzes();
        this.studentSubmissions = studentSubmissions;
        adapter.addData(studentSubmissions);
        pullRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onGetQuizzesSubmissionsFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, "");
    }

    @Override
    public void onPostFeedbackSuccess(Feedback feedback) {
        fetchData();
    }

    @Override
    public void onPostFeedbackFailure(String message, int errorCode) {
        Toast.makeText(activity, "Feedback already exist", Toast.LENGTH_LONG).show();
        fetchData();
    }

    @Override
    public void onPostQuizGradeSuccess(StudentSubmission studentSubmission) {
        if (feedBack != null) {
                String userId = SessionManager.getInstance().getUserId();
                Feedback feedback = new Feedback();
                feedback.setContent(feedBack);
                feedback.setOwnerId(Integer.valueOf(userId));
                feedback.setOnId(quizId);
                feedback.setOnType("Quiz");
                feedback.setToId(studentSubmission.getStudentId());
                feedback.setToType("Student");
                activity.showLoadingDialog();
                feedback.setId((submission.getFeedback() != null) ? submission.getFeedback().getId() : null);
                if (studentSubmission.getFeedback() == null) {
                    gradingView.postSubmissionFeedback(feedback, true);
                } else {
                    gradingView.postSubmissionFeedback(feedback,false);
                }
        } else {
            fetchData();
        }
    }

    @Override
    public void onPostQuizGradeFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        activity.showErrorDialog(activity, errorCode, "");

    }

    @Override
    public void onItemCLicked(StudentSubmission submission) {
        this.submission = submission;
        gradeFeedbackDialog = new GradeFeedbackDialog(activity, R.style.GradeDialog, this, submission);
        gradeFeedbackDialog.show();
    }

    @Override
    public void onDownloadButtonClick() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
    }

    private void fetchData() {
        if (isAssignmentsGrading) {
            if (courseId != -1 && courseGroupId != -1 && assignmentId != -1) {
                gradingView.getAssignmentSubmissions(courseId, courseGroupId, assignmentId);
            }
        } else {
            if (quizId != -1) {
                gradingView.getQuizzesSubmissions(quizId, courseGroupId);
            }

        }
    }

    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_grading_layout, (ViewGroup) rootView, false);
                skeletonLayout.addView(rowLayout);
            }
            recyclerView.setVisibility(View.GONE);
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
            shimmer.showShimmer(true);
            skeletonLayout.setVisibility(View.VISIBLE);
            skeletonLayout.bringToFront();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            shimmer.setVisibility(View.GONE);
        }
    }
}
