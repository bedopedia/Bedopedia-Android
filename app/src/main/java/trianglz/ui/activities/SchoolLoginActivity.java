package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.skolera.skolera_android.R;

public class SchoolLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText schoolNameEditText;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_login);
        bindViews();
    }

    private void bindViews() {
        schoolNameEditText = findViewById(R.id.et_school_name);
        loginBtn = findViewById(R.id.btn_login);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                break;
        }
    }
}
