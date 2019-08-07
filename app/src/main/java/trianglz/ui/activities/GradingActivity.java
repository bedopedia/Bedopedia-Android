package trianglz.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import trianglz.ui.adapters.StudentGradeAdapter;
import trianglz.utils.Constants;

public class GradingActivity extends SuperActivity implements View.OnClickListener, StudentGradeAdapter.StudentGradeInterface, GradeFeedbackDialog.GradeDialogInterface, GradingPresenter {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_grading);
        getValueFromIntent();
        onBindView();
        setListeners();
        fetchData();
    }

    private void getValueFromIntent() {
        isAssignmentsGrading = getIntent().getBooleanExtra(Constants.KEY_ASSIGNMENTS_GRADING, true);
        if (isAssignmentsGrading) {
            courseId = getIntent().getIntExtra(Constants.KEY_COURSE_ID, -1);
            courseGroupId = getIntent().getIntExtra(Constants.KEY_COURSE_GROUP_ID, -1);
            assignmentId = getIntent().getIntExtra(Constants.KEY_ASSIGNMENT_ID, -1);
            headerString = getIntent().getStringExtra(Constants.KEY_ASSIGNMENT_NAME);
        } else {
            courseGroupId = getIntent().getIntExtra(Constants.KEY_COURSE_GROUP_ID, -1);
            assignmentId = getIntent().getIntExtra(Constants.KEY_ASSIGNMENT_ID, -1);
            quizId = getIntent().getIntExtra(Constants.KEY_QUIZ_ID, -1);
            headerString = getIntent().getStringExtra(Constants.KEY_QUIZ_NAME);
        }
    }

    void onBindView() {
        backBtn = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        adapter = new StudentGradeAdapter(this, this);
        recyclerView.setAdapter(adapter);
        gradingView = new GradingView(this,this);
        headerTextView = findViewById(R.id.tv_header);
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
    public void onGradeButtonClick(String grade, String feedback, int studentId) {
        gradeFeedbackDialog = new GradeFeedbackDialog(this, R.style.GradeDialog, this, grade, feedback, studentId);
        gradeFeedbackDialog.show();

    }

    @Override
    public void onDownloadButtonClick() {
        //todo replace with real URL
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
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
            showLoadingDialog();
            gradingView.postAssignmentGrade(gradeModel);
        }else {
            gradeFeedbackDialog.dismiss();
            GradeModel gradeModel = new GradeModel();
            gradeModel.setQuizId(quizId);
            gradeModel.setCourseGroupId(courseGroupId);
            gradeModel.setCourseId(courseId);
            gradeModel.setStudentId(studentId);
            gradeModel.setScore(Double.valueOf(grade));
            showLoadingDialog();
            gradingView.postQuizGrade(gradeModel);
        }

        this.feedBack = feedBack;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onGetAssignmentSubmissionsSuccess(ArrayList<StudentSubmission> submissions) {
        if (progress.isShowing()) progress.dismiss();
        adapter.addData(submissions);
    }

    @Override
    public void onGetAssignmentSubmissionsFailure(String message, int errorCode) {
        if (progress.isShowing()) progress.dismiss();
        Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show();
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
                showLoadingDialog();
                gradingView.postSubmissionFeedback(feedback);
            } else {
                fetchData();
            }
        } else {
            fetchData();
        }
    }

    private void fetchData() {
        if (!progress.isShowing()) showLoadingDialog();
        if (isAssignmentsGrading) {
            if (courseId != -1 && courseGroupId != -1 && assignmentId != -1) {
                gradingView.getAssignmentSubmissions(courseId, courseGroupId, assignmentId);
            }
        } else {
            if (quizId != -1) {
                gradingView.getQuizzesSubmissions(quizId);
            }

        }
    }

    @Override
    public void onPostAssignmentGradeFailure(String message, int errorCode) {
        if (progress.isShowing()) progress.dismiss();
    }

    @Override
    public void onGetQuizzesSubmissionsSuccess(ArrayList<StudentSubmission> studentSubmissions) {
        if (progress.isShowing()) progress.dismiss();
        adapter.setIsQuizzes();
        adapter.addData(studentSubmissions);
    }

    @Override
    public void onGetQuizzesSubmissionsFailure(String message, int errorCode) {
        if (progress.isShowing()) progress.dismiss();

    }

    @Override
    public void onPostFeedbackSuccess(Feedback feedback) {
        fetchData();
    }

    @Override
    public void onPostFeedbackFailure(String message, int errorCode) {
        Toast.makeText(this, "Feedback already exist", Toast.LENGTH_LONG).show();
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
                showLoadingDialog();
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
        if (progress.isShowing()) progress.dismiss();
    }
}
