package trianglz.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.skolera.skolera_android.R;

/**
 * Created by Farah A. Moniem on 05/08/2019.
 */
public class GradeFeedbackDialog extends Dialog implements DialogInterface.OnShowListener, DialogInterface.OnCancelListener, View.OnClickListener {

    private EditText studentGradeEditText, studentFeedbackEditText;
    private Button submitButton;
    private Context context;
    private String grade, feedback;
    private GradeDialogInterface gradeDialogInterface;

    public GradeFeedbackDialog(@NonNull Context context, @StyleRes
            int theme, GradeDialogInterface gradeDialogInterface, String grade, String feedback) {
        super(context, theme);
        View view = getLayoutInflater().inflate(R.layout.dialog_grade_feedback, null);
        setContentView(view);
        this.grade = grade;
        this.feedback = feedback;
        this.gradeDialogInterface = gradeDialogInterface;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBindView();
        setListeners();
    }

    void setListeners() {
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

    void onBindView() {
        studentGradeEditText = findViewById(R.id.student_grade_edittext);
        studentFeedbackEditText = findViewById(R.id.student_feedback_edittext);
        submitButton = findViewById(R.id.submit_btn);
        studentGradeEditText.setText(grade);
        studentFeedbackEditText.setText(feedback);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn) {
            if (validate(studentGradeEditText.getText().toString())) {
                gradeDialogInterface.onSubmitClicked(studentGradeEditText.getText().toString(),
                        studentFeedbackEditText.getText().toString());
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
        void onSubmitClicked(String grade, String feedBack);
    }

    public boolean validate(String grade) {
        boolean valid = true;

        if (grade.isEmpty()) {
            valid = false;
        }
        return valid;
    }
}
