package trianglz.ui.changepassword;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.skolera.skolera_android.R;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ChangePasswordFragment.newInstance())
                    .commitNow();
        }
    }
}
