package trianglz.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.skolera.skolera_android.R;

import trianglz.managers.SessionManager;

/**
 * Created by Farah A. Moniem on 13/10/2019.
 */

public class ErrorDialog extends Dialog implements DialogInterface.OnShowListener, View.OnClickListener {

    private TextView contentTextView, titleTextView;
    private String content;
    private Button confirmButton, cancelButton;
    private Context context;
    private DialogConfirmationInterface dialogConfirmationInterface;
    private DialogType dialogType;


    public ErrorDialog(@NonNull Context context, String content, DialogType dialogType) {
        super(context, R.style.ErrorDialog);
        View view = getLayoutInflater().inflate(R.layout.layout_error_dialog, null);
        setContentView(view);
        this.context = context;
        this.content = content;
        this.dialogType = dialogType;
    }

    public ErrorDialog(@NonNull Context context, String content, DialogType dialogType, DialogConfirmationInterface dialogConfirmationInterface) {
        super(context, R.style.ErrorDialog);
        View view = getLayoutInflater().inflate(R.layout.layout_error_dialog, null);
        setContentView(view);
        this.context = context;
        this.content = content;
        this.dialogType = dialogType;
        this.dialogConfirmationInterface = dialogConfirmationInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        setListeners();
    }

    private void setListeners() {
        this.setOnShowListener(this);
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private void bindViews() {
        getWindow().setDimAmount((float) 0.3);
        titleTextView = findViewById(R.id.dialog_title_tv);
        contentTextView = findViewById(R.id.dialog_content_tv);
        confirmButton = findViewById(R.id.submit_btn);
        cancelButton = findViewById(R.id.cancel_btn);
        contentTextView.setText(content);
        if (dialogType == DialogType.CONFIRMATION) {
            cancelButton.setVisibility(View.VISIBLE);
        } else if (dialogType == DialogType.QUIZ_SUBMISSION) {
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setText(context.getResources().getString(R.string.solve_now));
            cancelButton.setBackground(context.getResources().getDrawable(R.drawable.curved_jade_green_25, null));
            confirmButton.setText(context.getResources().getString(R.string.submit_quiz));

        } else {
            cancelButton.setVisibility(View.GONE);
        }
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            confirmButton.setBackground(context.getResources().getDrawable(R.drawable.curved_salmon_25dp, null));
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            confirmButton.setBackground(context.getResources().getDrawable(R.drawable.curved_turquoise_25, null));
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.ADMIN.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.HOD.toString())) {
            confirmButton.setBackground(context.getResources().getDrawable(R.drawable.curved_cerulean_blue, null));
        } else {
            confirmButton.setBackground(context.getResources().getDrawable(R.drawable.curved_jade_green_25, null));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                if (dialogType == DialogType.CONFIRMATION) {
                    dialogConfirmationInterface.onConfirm();
                    dismiss();
                } else {
                    dismiss();
                }
                break;
            case R.id.cancel_btn:
                if(dialogConfirmationInterface != null){
                    dialogConfirmationInterface.onCancel();
                }
                break;
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        super.show();
    }

    public enum DialogType {
        CONFIRMATION, ERROR, QUIZ_SUBMISSION
    }

    public interface DialogConfirmationInterface {
        void onConfirm();
        void onCancel();
    }
}
