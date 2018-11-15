package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Picasso;

import trianglz.core.presenters.LoginPresenter;
import trianglz.core.views.LoginView;
import trianglz.managers.SessionManager;
import trianglz.models.School;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class LoginActivity extends SuperActivity implements View.OnClickListener, LoginPresenter {
    private MaterialEditText emailEditText;
    private MaterialEditText passwordEditText;
    private Button loginBtn;
    private LoginView loginView;
    private School school;
    private ImageView schoolImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        setListeners();
        getDataFromIntent();
        onClick(loginBtn);
    }

    private void bindViews() {
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setBackground(Util.getCurvedBackgroundColor(Util.convertDpToPixel(8,this),
                getResources().getColor(R.color.jade_green)));
        loginView = new LoginView(this, this);
        schoolImageView = findViewById(R.id.img_school);
    }

    private void setListeners() {
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if(Util.isNetworkAvailable(this)){
                    if (validate(emailEditText.getText().toString(), passwordEditText.getText().toString())) {
                        super.showLoadingDialog();
                        String url = school.schoolUrl + "/api/auth/sign_in";
                        loginView.login(url,emailEditText.getText().toString(), passwordEditText.getText().toString(),school.schoolUrl);
                    }
                }else {
                    Util.showErrorDialog(LoginActivity.this,getResources().getString(R.string.skolera),
                            getResources().getString(R.string.no_internet_connection));
                }
                break;
        }
    }


    public boolean validate(String email, String password) {
        boolean valid = true;
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if(email.isEmpty()){
                showErrorMessage(emailEditText, getResources().getString(R.string.email_is_empty));
            }else {
                showErrorMessage(emailEditText, getResources().getString(R.string.enter_valid_email));
            }
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
    public void onLoginSuccess() {
        progress.dismiss();
        openHomeActivity();
        String url = school.schoolUrl + "/api/auth/sign_in";
        SessionManager.getInstance().setloginValues(url,emailEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    @Override
    public void onLoginFailure(String message, int errorCode) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        if(errorCode == 401 || errorCode == 500 ){
            logoutUser(this);
        }else {
            showErrorDialog(this);
        }
    }


    private void openHomeActivity(){
        Intent intent = new Intent(this,HomeActivity.class);
        this.startActivity(intent);
    }
}
