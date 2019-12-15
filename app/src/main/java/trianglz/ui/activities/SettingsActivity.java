package trianglz.ui.activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skolera.skolera_android.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout changeLanguageConstraintLayout;
    TextView languageTextView;
    Button changePasswordButton, shareAppButton, rateAppButton, signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bindViews();
        setListeners();
    }

    private void bindViews() {
        changeLanguageConstraintLayout = findViewById(R.id.layout_change_language);
        languageTextView = findViewById(R.id.tv_language);
        changePasswordButton = findViewById(R.id.btn_change_password);
        shareAppButton = findViewById(R.id.btn_share_app);
        rateAppButton = findViewById(R.id.btn_rate_app);
        signOutButton = findViewById(R.id.btn_sign_out);

    }

    private void setListeners() {
        changeLanguageConstraintLayout.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
        shareAppButton.setOnClickListener(this);
        rateAppButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_change_language:
                break;
            case R.id.btn_change_password:
                break;
            case R.id.btn_share_app:
                break;
            case R.id.btn_rate_app:
                break;
            case R.id.btn_sign_out:
                break;
        }
    }
}
