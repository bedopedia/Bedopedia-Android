package trianglz.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import trianglz.managers.SessionManager;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 15/12/2019.
 */
public class ChangePasswordDialog extends Dialog implements DialogInterface.OnShowListener, View.OnClickListener {

    private EditText oldPasswordEditText, newPasswordEditText;
    private Button updateButton;
    private ImageButton cancelButton;
    private View oldPasswordErrorView, newPasswordErrorView;
    private TextView oldPasswordShow, newPasswordShow, newPasswordError, oldPasswordError;
    private Context context;
    private DialogConfirmationInterface dialogConfirmationInterface;


    public ChangePasswordDialog(@NonNull Context context, DialogConfirmationInterface dialogConfirmationInterface) {
        super(context, R.style.ErrorDialog);
        View view = getLayoutInflater().inflate(R.layout.change_password_dialog, null);
        setContentView(view);
        this.context = context;
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
        updateButton.setOnClickListener(this);
        newPasswordShow.setOnClickListener(this);
        oldPasswordShow.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        oldPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPasswordErrorView.setBackgroundColor(context.getResources().getColor(R.color.greyish));
                oldPasswordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPasswordErrorView.setBackgroundColor(context.getResources().getColor(R.color.greyish));
                newPasswordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void bindViews() {
        getWindow().setDimAmount((float) 0.3);
        oldPasswordEditText = findViewById(R.id.et_old_password);
        newPasswordEditText = findViewById(R.id.et_new_password);
        oldPasswordShow = findViewById(R.id.btn_show_old_password);
        newPasswordShow = findViewById(R.id.btn_show_new_password);
        updateButton = findViewById(R.id.update_btn);
        cancelButton = findViewById(R.id.cancel_btn);
        oldPasswordErrorView = findViewById(R.id.view_old_password);
        newPasswordErrorView = findViewById(R.id.view_new_password);
        oldPasswordError = findViewById(R.id.old_password_error_tv);
        newPasswordError = findViewById(R.id.new_password_error_tv);
        oldPasswordShow.setTextColor(context.getResources().getColor(Util.checkUserColor()));
        newPasswordShow.setTextColor(context.getResources().getColor(Util.checkUserColor()));
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            updateButton.setBackground(context.getResources().getDrawable(R.drawable.curved_salmon_25dp, null));
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            updateButton.setBackground(context.getResources().getDrawable(R.drawable.curved_turquoise_25, null));
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.ADMIN.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.HOD.toString())) {
            updateButton.setBackground(context.getResources().getDrawable(R.drawable.curved_cerulean_blue, null));
        } else {
            updateButton.setBackground(context.getResources().getDrawable(R.drawable.curved_jade_green_25, null));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_old_password:
                if (oldPasswordEditText.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    oldPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    oldPasswordShow.setText(context.getResources().getString(R.string.hide));
                    oldPasswordShow.setTextColor(context.getResources().getColor(R.color.warm_grey));
                } else {
                    oldPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    oldPasswordShow.setText(context.getResources().getString(R.string.show));
                    oldPasswordShow.setTextColor(context.getResources().getColor(Util.checkUserColor()));
                }
                break;
            case R.id.btn_show_new_password:
                if (newPasswordEditText.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    newPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPasswordShow.setText(context.getResources().getString(R.string.hide));
                    newPasswordShow.setTextColor(context.getResources().getColor(R.color.warm_grey));
                } else {
                    newPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPasswordShow.setText(context.getResources().getString(R.string.show));
                    newPasswordShow.setTextColor(context.getResources().getColor(Util.checkUserColor()));
                }
                break;
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.update_btn:
                boolean newValid = validate(oldPasswordEditText.getText().toString(), oldPasswordError, oldPasswordErrorView);
                boolean oldValid = validate(newPasswordEditText.getText().toString(), newPasswordError, newPasswordErrorView);
                if (newValid && oldValid) {
                    dialogConfirmationInterface.onUpdate();
                    dismiss();
                }
                break;
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        super.show();
    }

    public interface DialogConfirmationInterface {
        void onUpdate();
    }

    public boolean validate(String password, TextView textView, View view) {
        boolean valid = true;
        if (password.isEmpty() || password.length() < 5) {
            textView.setVisibility(View.VISIBLE);
            if (password.isEmpty()) {
                view.setBackgroundColor(context.getResources().getColor(R.color.tomato));
                textView.setText(context.getResources().getString(R.string.password_is_empty));
            } else {
                view.setBackgroundColor(context.getResources().getColor(R.color.tomato));
                textView.setText(context.getResources().getString(R.string.password_length_error));
            }
            valid = false;
        }
        return valid;
    }
}
