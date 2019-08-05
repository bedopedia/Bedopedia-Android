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

import trianglz.components.GradeFeedbackDialog;
import trianglz.core.presenters.AssignmentGradingPresenter;
import trianglz.core.views.AssignmentGradingView;
import trianglz.ui.adapters.StudentAssignmentGradeAdapter;
import trianglz.utils.Constants;

public class AssignmentGradingActivity extends SuperActivity implements View.OnClickListener, StudentAssignmentGradeAdapter.StudentGradeInterface, GradeFeedbackDialog.GradeDialogInterface, AssignmentGradingPresenter {
    private StudentAssignmentGradeAdapter adapter;
    private RecyclerView recyclerView;
    private ImageButton backBtn;
    private GradeFeedbackDialog gradeFeedbackDialog;
    private AssignmentGradingView assignmentGradingView;
    private int courseId, courseGroupId, assignmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_grading);
        onBindView();
        getValueFromIntent();
        setListeners();
        if (courseId != -1 && courseGroupId != -1 && assignmentId != -1) {
            showLoadingDialog();
            assignmentGradingView.getAssignmentSubmissions(courseId, courseGroupId, assignmentId);
        }
    }

    private void getValueFromIntent() {
        courseId = getIntent().getIntExtra(Constants.KEY_COURSE_ID, -1);
        courseGroupId = getIntent().getIntExtra(Constants.KEY_COURSE_GROUP_ID, -1);
        assignmentId = getIntent().getIntExtra(Constants.KEY_ASSIGNMENT_ID, -1);
    }

    void onBindView() {
        backBtn = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new StudentAssignmentGradeAdapter(this, this);
        recyclerView.setAdapter(adapter);
        assignmentGradingView = new AssignmentGradingView(this,this);
    }

    void setListeners() {
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onGradeButtonClick(String grade, String feedback) {
        gradeFeedbackDialog = new GradeFeedbackDialog(this, R.style.GradeDialog, this, grade, feedback, this);
        gradeFeedbackDialog.show();

    }

    @Override
    public void onDownloadButtonClick() {
        //todo replace with real URL
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
    }


    @Override
    public void onSubmitClicked() {
        gradeFeedbackDialog.dismiss();
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
    public void onGetAssignmentSubmissionsSuccess() {
        if (progress.isShowing()) progress.dismiss();
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetAssignmentSubmissionsFailure(String message, int errorCode) {
        if (progress.isShowing()) progress.dismiss();
        Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show();
    }
}
