package trianglz.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import trianglz.managers.SessionManager;

/**
 * Created by Farah A. Moniem on 13/10/2019.
 */

public class ErrorDialog extends Dialog implements DialogInterface.OnShowListener, View.OnClickListener {

    private TextView contentTextView, titleTextView;
    private String content;
    private Button submitButton;
    private Context context;


    public ErrorDialog(@NonNull Context context, String content) {
        super(context, R.style.ErrorDialog);
        View view = getLayoutInflater().inflate(R.layout.layout_error_dialog, null);
        setContentView(view);
        this.context = context;
        this.content = content;
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
    }

    private void bindViews() {
        getWindow().setDimAmount((float) 0.3);
        titleTextView = findViewById(R.id.dialog_title_tv);
        contentTextView = findViewById(R.id.dialog_content_tv);
        submitButton = findViewById(R.id.submit_btn);
        contentTextView.setText(content);
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            submitButton.setBackground(context.getResources().getDrawable(R.drawable.curved_salmon_25dp, null));
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            submitButton.setBackground(context.getResources().getDrawable(R.drawable.curved_turquoise_25, null));
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.ADMIN.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.HOD.toString())) {
            submitButton.setBackground(context.getResources().getDrawable(R.drawable.curved_cerulean_blue, null));
        } else {
            submitButton.setBackground(context.getResources().getDrawable(R.drawable.curved_jade_green_25, null));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn) {
            dismiss();
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        super.show();
    }

}
