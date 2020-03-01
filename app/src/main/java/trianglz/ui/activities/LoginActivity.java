package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.HashMap;

import trianglz.components.ChangePasswordDialog;
import trianglz.components.HideKeyboardOnTouch;
import trianglz.core.presenters.LoginPresenter;
import trianglz.core.views.LoginView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Actor;
import trianglz.models.School;
import trianglz.models.Student;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class LoginActivity extends SuperActivity implements View.OnClickListener, LoginPresenter,
        TextView.OnEditorActionListener, ChangePasswordDialog.DialogConfirmationInterface {
    private MaterialEditText emailEditText;
    private MaterialEditText passwordEditText;
    private Button loginBtn;
    private LoginView loginView;
    private School school;
    private ImageView schoolImageView;
    private ImageButton backBtn;
    private String baseUrl;
    private LinearLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        setListeners();
        getDataFromIntent();
    }

    private void bindViews() {
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setBackground(Util.getCurvedBackgroundColor(Util.convertDpToPixel(8, this),
                getResources().getColor(R.color.jade_green)));
        loginView = new LoginView(this, this);
        schoolImageView = findViewById(R.id.img_school);
        backBtn = findViewById(R.id.btn_back);
        parentView = findViewById(R.id.parent_view);
        baseUrl = SessionManager.getInstance().getBaseUrl();

    }

    private void setListeners() {
        loginBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        parentView.setOnTouchListener(new HideKeyboardOnTouch(this));
        passwordEditText.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (Util.isNetworkAvailable(this)) {
                    if (validate(emailEditText.getText().toString(), passwordEditText.getText().toString())) {
                        super.showLoadingDialog();
                        String url = school.schoolUrl + "/api/auth/sign_in";
                        loginView.login(url, emailEditText.getText().toString(), passwordEditText.getText().toString(), school.schoolUrl);
                    }
                } else {
                    showErrorDialog(this, -3, getResources().getString(R.string.no_internet_connection));
                }
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }


    public boolean validate(String email, String password) {
        boolean valid = true;
        if (email.isEmpty()) {
            showErrorMessage(emailEditText, getResources().getString(R.string.email_is_empty));
            valid = false;
        }
        if (password.isEmpty() || password.length() < 4) {
            if (password.isEmpty()) {
                showErrorMessage(passwordEditText, getResources().getString(R.string.password_is_empty));
            } else {
                showErrorMessage(passwordEditText, getResources().getString(R.string.password_length_error));
            }
            valid = false;
        }

        return valid;
    }

    public void getDataFromIntent() {
        school = (School) getIntent().getSerializableExtra(Constants.SCHOOL);
        if (school != null) {
            if (!school.avatarUrl.isEmpty() && school.avatarUrl != null) {
                Picasso.with(this)
                        .load(school.avatarUrl)
                        .fit()
                        .placeholder(getResources().getDrawable(R.drawable.logo_icon))
                        .into(schoolImageView);
            }
        }

    }

    private void showErrorMessage(MaterialEditText editText, String message) {
        editText.setError(message);
        editText.setUnderlineColor(getResources().getColor(R.color.pale_red));
        editText.setHideUnderline(false);
        editText.setErrorColor(getResources().getColor(R.color.tomato));
    }

    @Override
    public void onLoginSuccess(Actor actor) {
        openStudentDetailActivity(actor);
        String url = school.schoolUrl + "/api/auth/sign_in";
        SessionManager.getInstance().setloginValues(url, emailEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    @Override
    public void onLoginSuccess() {
        progress.dismiss();
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            openHomeActivity();
        }
        String url = school.schoolUrl + "/api/auth/sign_in";
        SessionManager.getInstance().setloginValues(url, emailEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    @Override
    public void onLoginFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        if (errorCode == 401) {
            showErrorDialog(this, -3, getResources().getString(R.string.wrong_username_or_password));
        } else if (errorCode == 406) {
            ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(this, this, getResources().getString(R.string.choose_new_password));
            changePasswordDialog.show();
            changePasswordDialog.headerHashMap = SessionManager.getInstance().getHeaderHashMap();
            changePasswordDialog.userId = Integer.parseInt(SessionManager.getInstance().getUserId());
            changePasswordDialog.setOldPasswordValue(passwordEditText.getText().toString());

        } else {
            showErrorDialog(this, errorCode, "");
        }
    }

    @Override
    public void onGetStudentsHomeSuccess(Student student, JSONArray attendanceJsonArray) {
        String url = school.schoolUrl + "/api/auth/sign_in";
        SessionManager.getInstance().setloginValues(url, emailEditText.getText().toString(),
                passwordEditText.getText().toString());
        if (progress.isShowing()) {
            progress.dismiss();
        }
        openStudentDetailActivity(student, attendanceJsonArray);


    }

    @Override
    public void onGetStudentsHomeFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        if (errorCode == 401) {
            showErrorDialog(this, -3, getResources().getString(R.string.wrong_username_or_password));
        } else {
            showErrorDialog(this, errorCode, "");
        }
    }


    private void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
    }

    private void openStudentDetailActivity(Actor actor) {
        Intent intent = new Intent(this, StudentMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_ACTOR, actor);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        this.startActivity(intent);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            switch (textView.getId()) {
                case R.id.et_password:
                    if (Util.isNetworkAvailable(this)) {
                        if (validate(emailEditText.getText().toString(), passwordEditText.getText().toString())) {
                            super.showLoadingDialog();
                            String url = school.schoolUrl + "/api/auth/sign_in";
                            loginView.login(url, emailEditText.getText().toString(), passwordEditText.getText().toString(), school.schoolUrl);
                        }
                    } else {
                        showErrorDialog(this, -3, getResources().getString(R.string.no_internet_connection));
                    }
                    break;

            }
        }
        return false;
    }

    private void openStudentDetailActivity(Student student, JSONArray studentAttendance) {
        Intent intent = new Intent(this, StudentMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        bundle.putSerializable(Constants.KEY_ATTENDANCE, studentAttendance.toString());
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);
    }

    @Override
    public void onUpdatePassword(String oldPassword, String newPassword, HashMap<String, String> headerHashMap, int userId) {
        changePassword(oldPassword, newPassword, headerHashMap, userId);
    }


    @Override
    public void onPasswordChangedSuccess(String newPassword) {
        if (progress.isShowing())
            progress.dismiss();
        SessionManager.getInstance().setPassword(newPassword);
        passwordEditText.setText("");
        emailEditText.setText("");
    }

    @Override
    public void onPasswordChangedFailure(String message, int errorCode) {
        if (progress.isShowing())
            progress.dismiss();
        showErrorDialog(this, -3, message);
    }

    void changePassword(String oldPassword, String newPassword, HashMap<String, String> headerHashMap, int userId) {
        String url = baseUrl + ApiEndPoints.changePassword(userId);
        loginView.changePassword(url, oldPassword, userId, newPassword, headerHashMap);
        showLoadingDialog();
    }


}
