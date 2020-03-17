package trianglz.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.skolera.skolera_android.R;

/**
 * Created by Farah A. Moniem on 15/09/2019.
 */
public class ExcusedDialog extends Dialog implements DialogInterface.OnShowListener, View.OnClickListener {

    private EditText excuseEditText;
    private Button submitButton;
    private ExcusedDialogInterface excusedDialogInterface;
    private Context context;
    private int studentId;
    private String excusedString = "";


    public ExcusedDialog(@NonNull Context context, ExcusedDialogInterface excusedDialogInterface, int studentId) {
        super(context, R.style.GradeDialog);
        View view = getLayoutInflater().inflate(R.layout.layout_excused_dialog, null);
        setContentView(view);
        this.context = context;
        this.excusedDialogInterface = excusedDialogInterface;
        this.studentId = studentId;
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
        excuseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (excuseEditText.getText().toString().isEmpty()) {
                    excuseEditText.setBackgroundResource(R.drawable.curved_tomato);
                } else {
                    excuseEditText.setBackgroundResource(R.drawable.curved_cool_grey);
                }
            }
        });
    }

    private void bindViews() {
        excuseEditText = findViewById(R.id.excuse_reason_edit_text);
        submitButton = findViewById(R.id.submit_btn);
        excuseEditText.setText(excusedString);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn) {
            if (validate(excuseEditText.getText().toString())) {
                excusedDialogInterface.onSubmitClicked(excuseEditText.getText().toString(), studentId);
                dismiss();
            } else {
                excuseEditText.setBackgroundResource(R.drawable.curved_tomato);
            }
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        super.show();
    }


    public boolean validate(String grade) {
        boolean valid = true;

        if (grade.isEmpty()) {
            valid = false;
        }
        return valid;
    }

    public void setExcusedString(String excusedString) {
        this.excusedString = excusedString;
    }

    public interface ExcusedDialogInterface {
        void onSubmitClicked(String comment, int studentId);
    }
}
