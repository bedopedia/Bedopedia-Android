package trianglz.core.views;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import trianglz.core.presenters.SplashPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

/**
 * Created by ${Aly} on 11/14/2018.
 */
public class SplashView {
    Context context;
    SplashPresenter splashPresenter;

    public SplashView(Context context, SplashPresenter splashPresenter) {
        this.context = context;
        this.splashPresenter = splashPresenter;
    }

    public void login() {
        String schoolUrl = SessionManager.getInstance().getSchoolUrl();
        String email = SessionManager.getInstance().getEmail();
        String password = SessionManager.getInstance().getPassword();
        UserManager.login(schoolUrl, email, password, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                splashPresenter.onLoginSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                splashPresenter.onLoginFailure(message,errorCode);
            }
        });
    }


}
