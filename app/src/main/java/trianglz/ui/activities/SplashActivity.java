package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.skolera.skolera_android.R;

import trianglz.core.presenters.SplashPresenter;
import trianglz.core.views.SplashView;
import trianglz.managers.SessionManager;
import trianglz.models.Actor;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class SplashActivity extends SuperActivity implements SplashPresenter {
    private SplashView splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashView = new SplashView(this, this);
        if (SessionManager.getInstance().getIsLoggedIn()) {
            if(Util.isNetworkAvailable(this)){
                splashView.login();
            }else {
                startSchoolCodeActivity();
            }
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

    private void openStudentDetailActivity(Actor actor) {
        Intent intent = new Intent(this,StudentDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_ACTOR,actor);
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        this.startActivity(intent);
    }

    @Override
    public void onLoginSuccess() {
        if(SessionManager.getInstance().getUserType()){
            openHomeActivity();
        }
        finish();
    }

    @Override
    public void onLoginSuccess(Actor actor) {
        openStudentDetailActivity(actor);
        finish();
    }

    @Override
    public void onLoginFailure(String message, int code) {
        openSchoolLoginActivity();
        finish();
    }
}
