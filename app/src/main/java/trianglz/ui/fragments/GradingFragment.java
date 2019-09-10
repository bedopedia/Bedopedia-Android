package trianglz.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.GradeFeedbackDialog;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
//        activity.toolbarView.setVisibility(View.GONE);
//        activity.headerLayout.setVisibility(View.GONE);
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
    }

    void setListeners() {
        backBtn.setOnClickListener(this);
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
        adapter.addData(submissions);
    }

    @Override
    public void onGetAssignmentSubmissionsFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        Util.showErrorDialog(activity, "Skolera", message);
    }

    @Override
    public void onPostAssignmentGradeSuccess(StudentSubmission studentSubmission) {
        if (feedBack != null) {
            if (!feedBack.isEmpty()) {
                String userId = SessionManager.getInstance().getUserId();
                Feedback feedback = new Feedback();
                feedback.setContent(feedBack);
                feedback.setOwnerId(Integer.valueOf(userId));
                feedback.setOnId(studentSubmission.getAssignmentId());
                feedback.setOnType("Assignment");
                feedback.setToId(studentSubmission.getStudentId());
                feedback.setToType("Student");
                activity.showLoadingDialog();
                gradingView.postSubmissionFeedback(feedback);
            } else {
                fetchData();
            }
        } else {
            fetchData();
        }
    }

    @Override
    public void onPostAssignmentGradeFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        if (errorCode == 401 || errorCode == 500) {
            activity.logoutUser(activity);
        } else {
            Util.showErrorDialog(activity, "Skolera", "Something went wrong, please try again");
        }
    }

    @Override
    public void onGetQuizzesSubmissionsSuccess(ArrayList<StudentSubmission> studentSubmissions) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        adapter.setIsQuizzes();
        adapter.addData(studentSubmissions);
    }

    @Override
    public void onGetQuizzesSubmissionsFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        if (errorCode == 401 || errorCode == 500) {
            activity.logoutUser(activity);
        } else {
            Util.showErrorDialog(activity, "Skolera", "Something went wrong, please try again");
        }
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
            if (!feedBack.isEmpty()) {
                String userId = SessionManager.getInstance().getUserId();
                Feedback feedback = new Feedback();
                feedback.setContent(feedBack);
                feedback.setOwnerId(Integer.valueOf(userId));
                feedback.setOnId(quizId);
                feedback.setOnType("Quiz");
                feedback.setToId(studentSubmission.getStudentId());
                feedback.setToType("Student");
                activity.showLoadingDialog();
                gradingView.postSubmissionFeedback(feedback);
            } else {
                fetchData();
            }
        } else {
            fetchData();
        }
    }

    @Override
    public void onPostQuizGradeFailure(String message, int errorCode) {
        if (activity.progress.isShowing()) activity.progress.dismiss();
        if (errorCode == 401 || errorCode == 500) {
            activity.logoutUser(activity);
        } else {
            Util.showErrorDialog(activity, "Skolera", "Something went wrong, please try again");
        }
    }

    @Override
    public void onItemCLicked(Double grade, String feedback, int studentId) {
        gradeFeedbackDialog = new GradeFeedbackDialog(activity, R.style.GradeDialog, this, grade + "", feedback, studentId);
        gradeFeedbackDialog.show();
    }

    @Override
    public void onDownloadButtonClick() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
    }

    private void fetchData() {
        if (!activity.progress.isShowing()) activity.showLoadingDialog();
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
}
