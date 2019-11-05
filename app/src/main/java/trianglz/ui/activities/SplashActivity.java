package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.skolera.skolera_android.R;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.core.presenters.SplashPresenter;
import trianglz.core.views.SplashView;
import trianglz.managers.SessionManager;
import trianglz.models.Actor;
import trianglz.models.Student;
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
            if (Util.isNetworkAvailable(this)) {
                splashView.refreshFireBaseToken();
            } else {
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
        Intent intent = new Intent(this, StudentMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_ACTOR, actor);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        this.startActivity(intent);
    }

    @Override
    public void onLoginSuccess() {
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
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

    @Override
    public void onGetStudentsHomeSuccess(ArrayList<Object> dataObjectArrayList) {
        if (dataObjectArrayList.size() > 1) {
            ArrayList<JSONArray> attendanceJsonArray = (ArrayList<JSONArray>) dataObjectArrayList.get(0);
            ArrayList<Student> studentArrayList = (ArrayList<Student>) dataObjectArrayList.get(1);
            if (studentArrayList.size() > 0 && attendanceJsonArray.size() > 0) {
                openStudentDetailActivity(studentArrayList.get(0), attendanceJsonArray.get(0));
            } else {
                openSchoolLoginActivity();
            }
        } else {
            openSchoolLoginActivity();
        }
        finish();
    }

    @Override
    public void onGetStudentsHomeFailure(String message, int errorCode) {
        openSchoolLoginActivity();
        finish();
    }

    @Override
    public void updateTokenSuccess() {

    }

    @Override
    public void updateTokenFailure() {
        startSchoolCodeActivity();
        finish();
    }


    private void openStudentDetailActivity(Student student, JSONArray studentAttendance) {
        Intent intent = new Intent(this, StudentMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        bundle.putSerializable(Constants.KEY_ATTENDANCE, studentAttendance.toString());
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);
    }
}
