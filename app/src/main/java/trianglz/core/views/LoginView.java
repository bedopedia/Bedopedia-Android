package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.LoginPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 10/30/2018.
 */
public class LoginView {
    private Context context;
    private LoginPresenter loginPresenter;

    public LoginView(Context context, LoginPresenter loginPresenter) {
        this.context = context;
        this.loginPresenter = loginPresenter;
    }
    public void login(String url,String email, String password){
        UserManager.login(url,email,password, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                loginPresenter.onLoginSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                loginPresenter.onLoginFailure(message,errorCode);
            }
        });
    }
}
