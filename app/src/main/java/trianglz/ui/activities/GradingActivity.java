package trianglz.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.components.GradeFeedbackDialog;
import trianglz.core.presenters.GradingPresenter;
import trianglz.core.views.GradingView;
import trianglz.models.PostAssignmentGradeModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_grading);
        getValueFromIntent();
        onBindView();
        setListeners();
        if (isAssignmentsGrading) {
            if (courseId != -1 && courseGroupId != -1 && assignmentId != -1) {
                showLoadingDialog();
                gradingView.getAssignmentSubmissions(courseId, courseGroupId, assignmentId);
            }
        } else {
            if (quizId != -1) {
                showLoadingDialog();
                gradingView.getQuizzesSubmissions(quizId);
            }

        }
    }

    private void getValueFromIntent() {
        isAssignmentsGrading = getIntent().getBooleanExtra(Constants.KEY_ASSIGNMENTS_GRADING, true);
        if (isAssignmentsGrading) {
            courseId = getIntent().getIntExtra(Constants.KEY_COURSE_ID, -1);
            courseGroupId = getIntent().getIntExtra(Constants.KEY_COURSE_GROUP_ID, -1);
            assignmentId = getIntent().getIntExtra(Constants.KEY_ASSIGNMENT_ID, -1);
        } else {
            quizId = getIntent().getIntExtra(Constants.KEY_QUIZ_ID, -1);
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
        gradeFeedbackDialog.dismiss();
        PostAssignmentGradeModel gradeModel = new PostAssignmentGradeModel();
        gradeModel.setAssignmentId(assignmentId);
        gradeModel.setCourseGroupId(courseGroupId);
        gradeModel.setCourseId(courseId);
        gradeModel.setStudentId(studentId);
        gradeModel.setGrade(Double.valueOf(grade));
        showLoadingDialog();
        gradingView.postAssignmentGrade(gradeModel);
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
    public void onPostAssignmentGradeSuccess() {
        if (progress.isShowing()) progress.dismiss();
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
}
