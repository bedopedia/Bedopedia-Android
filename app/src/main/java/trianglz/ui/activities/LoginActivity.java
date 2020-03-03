package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
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
    private MaterialEditText emailEditText, passwordEditText;
    private TextView emailErrorTextView, passwordErrorTextView;
    private Button loginBtn;
    private LoginView loginView;
    private School school;
    private ImageView schoolImageView;
    private ImageButton backBtn;
    private String baseUrl;
    private LinearLayout parentView;
    private View emailView;
    private ImageButton showHidePasswordButton;
    private RoundCornerProgressBar progressBar;

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

        progressBar = findViewById(R.id.progress_bar);
        emailErrorTextView = findViewById(R.id.email_error_tv);
        passwordErrorTextView = findViewById(R.id.password_error_tv);
        emailView = findViewById(R.id.email_view);
        showHidePasswordButton = findViewById(R.id.show_hide_password_image_button);
    }

    private void setListeners() {
        loginBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        showHidePasswordButton.setOnClickListener(this);
        parentView.setOnTouchListener(new HideKeyboardOnTouch(this));
        passwordEditText.setOnEditorActionListener(this);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emailView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white_four));
                emailErrorTextView.setVisibility(View.INVISIBLE);
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordErrorTextView.setVisibility(View.INVISIBLE);
                progressBar.setProgressColor(getResources().getColor(R.color.jade_green));
                Log.d("count", "afterTextChanged: " + s.toString().trim().length());
                if (s.toString().trim().length() > 6) {
                    progressBar.setProgress(100);
                } else {
                    progressBar.setProgress((10 * (float) s.length()) / 6);
                }
            }
        });
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
            case R.id.show_hide_password_image_button:
                if (passwordEditText.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showHidePasswordButton.setImageResource(R.drawable.ic_unshow_password);
                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showHidePasswordButton.setImageResource(R.drawable.ic_show_password);
                }
                break;
        }
    }


    public boolean validate(String email, String password) {
        boolean valid = true;
        if (email.isEmpty()) {
            showEmailErrorMessage(getResources().getString(R.string.email_is_empty), emailErrorTextView);
            valid = false;
        }
        if (password.isEmpty() || password.length() < 6) {
            if (password.isEmpty()) {
                showPasswordErrorMessage(getResources().getString(R.string.password_is_empty), passwordErrorTextView);
            } else {
                showPasswordErrorMessage(getResources().getString(R.string.password_length_error), passwordErrorTextView);
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

    private void showEmailErrorMessage(String message, TextView textView) {
        textView.setText(message);
        emailView.setBackgroundColor(ContextCompat.getColor(this, R.color.pale_red));
        textView.setVisibility(View.VISIBLE);
    }

    private void showPasswordErrorMessage(String message, TextView textView) {
        textView.setText(message);
        progressBar.setProgressColor(getResources().getColor(R.color.pale_red));
        progressBar.setProgress(100);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoginSuccess(Actor actor) {
        openStudentDetailActivity(actor);
        String url = school.schoolUrl + "/api/auth/sign_in";
    }

    @Override
    public void onParentLoginSuccess() {
        progress.dismiss();
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            openHomeActivity();
        }
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
