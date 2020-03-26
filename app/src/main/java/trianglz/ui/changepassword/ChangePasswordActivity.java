package trianglz.ui.changepassword;

import android.os.Bundle;

import com.skolera.skolera_android.R;

import trianglz.ui.activities.SuperActivity;

public class ChangePasswordActivity extends SuperActivity {

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
