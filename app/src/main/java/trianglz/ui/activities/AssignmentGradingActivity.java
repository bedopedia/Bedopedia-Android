package trianglz.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.skolera.skolera_android.R;

import trianglz.components.GradeFeedbackDialog;
import trianglz.ui.adapters.StudentAssignmentGradeAdapter;

public class AssignmentGradingActivity extends SuperActivity implements View.OnClickListener, StudentAssignmentGradeAdapter.StudentGradeInterface, GradeFeedbackDialog.GradeDialogInterface {
    private StudentAssignmentGradeAdapter adapter;
    private RecyclerView recyclerView;
    private ImageButton backBtn;
    private GradeFeedbackDialog gradeFeedbackDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_grading);
        onBindView();
        setListeners();
    }

    void onBindView() {
        backBtn = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new StudentAssignmentGradeAdapter(this, this);
        recyclerView.setAdapter(adapter);
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
}
