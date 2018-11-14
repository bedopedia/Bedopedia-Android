package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.skolera.skolera_android.R;

import trianglz.core.presenters.SplashPresenter;
import trianglz.core.views.SplashView;
import trianglz.managers.SessionManager;

public class SplashActivity extends AppCompatActivity implements SplashPresenter {
    private SplashView splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashView = new SplashView(this, this);
        if (SessionManager.getInstance().getIsLoggedIn()) {
            splashView.login();
        } else {
            startSchoolCodeActivity();
        }

    }

    private void startSchoolCodeActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openSchoolLoginActivity();
                finish();
            }
        }, 3000);

    }


    private void openSchoolLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, SchoolLoginActivity.class);
        SplashActivity.this.startActivity(intent);
    }


    private void openHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        SplashActivity.this.startActivity(intent);
    }

    @Override
    public void onLoginSuccess() {
        openHomeActivity();
    }

    @Override
    public void onLoginFailure(String message, int code) {
        // TODO: 11/14/2018 handle login failure in splash
        openSchoolLoginActivity();
    }
}
