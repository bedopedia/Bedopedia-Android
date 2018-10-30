package trianglz.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Picasso;

import trianglz.core.presenters.LoginPresenter;
import trianglz.core.views.LoginView;
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
                if (validate(emailEditText.getText().toString(), passwordEditText.getText().toString())) {
                    super.showLoadingDialog();
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
}
