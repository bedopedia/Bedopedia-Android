package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.skolera.skolera_android.R;

import trianglz.managers.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startSchoolCodeActivity();
    }

    private void startSchoolCodeActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SessionManager.getInstance().getIsLoggedIn()){
                    openHomeActivity();
                }else {
                    openSchoolLoginActivity();
                }

            }
        }, 3000);
    }


    private void openSchoolLoginActivity(){
        Intent intent = new Intent(SplashActivity.this, SchoolLoginActivity.class);
        SplashActivity.this.startActivity(intent);
    }


    private void openHomeActivity(){
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        SplashActivity.this.startActivity(intent);
    }
}
