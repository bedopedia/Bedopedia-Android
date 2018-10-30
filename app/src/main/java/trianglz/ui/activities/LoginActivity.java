package trianglz.ui.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.skolera.skolera_android.R;

import trianglz.utils.Util;

public class LoginActivity extends SuperActivity implements View.OnClickListener{
    private EditText emailEditText,passwordEditText;
    private Button loginBtn;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        setListeners();
    }

    private void bindViews() {
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_verify);
        loginBtn.setBackground(Util.getCurvedBackgroundColor(Util.convertDpToPixel(8,this),
                getResources().getColor(R.color.jade_green)));
    }

    private void setListeners() {
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_verify:
                super.showLoadingDialog();
//                if(validate(emailEditText.getText().toString(),passwordEditText.getText().toString())){
//
//                }
                break;
        }
    }


    public boolean validate(String email, String password) {
        boolean valid = true;
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // TODO: 10/29/2018
//            ((AutoCompleteTextView) rootView.findViewById(R.id.email)).setError("Enter a valid email address");
            valid = false;
        } else {
//            ((AutoCompleteTextView) rootView.findViewById(R.id.email)).setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            // TODO: 10/29/2018  Enter a valid password
//            ((AutoCompleteTextView) rootView.findViewById(R.id.password_edit_text)).setError("");
            valid = false;
        } else {
//            ((AutoCompleteTextView) rootView.findViewById(R.id.password_edit_text)).setError(null);
        }

        return valid;
    }

}
