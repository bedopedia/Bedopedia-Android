package trianglz.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.skolera.skolera_android.R;

import trianglz.models.Feedback;
import trianglz.models.StudentSubmission;

/**
 * Created by Farah A. Moniem on 05/08/2019.
 */
public class GradeFeedbackDialog extends Dialog implements DialogInterface.OnShowListener, DialogInterface.OnCancelListener, View.OnClickListener {

    private EditText studentGradeEditText, studentFeedbackEditText;
    private Button submitButton;
    private Context context;
    private String grade;
    private Feedback feedback;
    private GradeDialogInterface gradeDialogInterface;
    private int studentId;
    private TextView feedbackTextView;

    public GradeFeedbackDialog(@NonNull Context context, @StyleRes
            int theme, GradeDialogInterface gradeDialogInterface, StudentSubmission submission) {
        super(context, theme);
        View view = getLayoutInflater().inflate(R.layout.dialog_grade_feedback, null);
        setContentView(view);
        this.gradeDialogInterface = gradeDialogInterface;
        this.context = context;
        if (submission.getGrade() != null || submission.getScore() != null) {
            if (submission.getGrade() == null) {
                this.grade = String.valueOf(submission.getScore());
            } else {
                this.grade = String.valueOf(submission.getGrade());
            }
        } else {
            this.grade = "";
        }
        this.feedback = submission.getFeedback();
        this.studentId = submission.getStudentId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        setListeners();
    }

    private void setListeners() {
        this.setOnShowListener(this);
        submitButton.setOnClickListener(this);
        studentGradeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (studentGradeEditText.getText().toString().isEmpty()) {
                    studentGradeEditText.setBackgroundResource(R.drawable.curved_tomato);
                } else {
                    studentGradeEditText.setBackgroundResource(R.drawable.curved_cool_grey);
                }
            }
        });
    }

    private void bindViews() {
        studentGradeEditText = findViewById(R.id.student_grade_edittext);
        studentFeedbackEditText = findViewById(R.id.student_feedback_edittext);
        feedbackTextView = findViewById(R.id.tv_feedback);
        submitButton = findViewById(R.id.submit_btn);
        studentGradeEditText.setText(grade);
        studentFeedbackEditText.setText((feedback != null) ? feedback.getContent() : "");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn) {
            if (validate(studentGradeEditText.getText().toString())) {
                gradeDialogInterface.onSubmitClicked(studentGradeEditText.getText().toString(),
                        studentFeedbackEditText.getText().toString(), studentId);
            } else {
                studentGradeEditText.setBackgroundResource(R.drawable.curved_tomato);
            }
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        super.show();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        studentGradeEditText.addTextChangedListener(null);
    }

    public interface GradeDialogInterface {
        void onSubmitClicked(String grade, String feedBack, int studentId);
    }

    public boolean validate(String grade) {
        boolean valid = true;

        if (grade.isEmpty()) {
            valid = false;
        }
        return valid;
    }
}
