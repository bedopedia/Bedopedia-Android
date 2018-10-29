package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.skolera.skolera_android.R;

import trianglz.utils.Util;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText emailEditText,passwordEditText;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        setListeners();
    }

    private void bindViews() {
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setBackground(Util.getCurvedBackgroundColor(Util.convertDpToPixel(8,this),
                getResources().getColor(R.color.jade_green)));
    }

    private void setListeners() {
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                break;
        }
    }
}
