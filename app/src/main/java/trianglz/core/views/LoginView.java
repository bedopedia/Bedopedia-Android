package trianglz.core.views;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import trianglz.core.presenters.LoginPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

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

    public void login(String url, String email, String password, final String schoolUrl) {
        UserManager.login(url, email, password, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                parseLoginResponse(response);
                refreshFireBaseToken(schoolUrl);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                loginPresenter.onLoginFailure(message, errorCode);
            }
        });
    }

    public void refreshFireBaseToken(final String url) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                SessionManager.getInstance().setFireBaseToken(token);
                updateToken(url);
                loginPresenter.onTokenUpdatedSuccess();
            }

        });
    }

    private void parseLoginResponse(JSONObject response) {
        String accessToken = response.optString("access-token");
        String tokenType = response.optString("token-type");
        String clientCode = response.optString("client");
        String uid = response.optString("uid");
        JSONObject data = response.optJSONObject("data");
        String username = data.optString("username");
        String userId = data.optString("id");
        String id = data.optString("actable_id");
        SessionManager.getInstance().createLoginSession(accessToken, tokenType, clientCode, uid, username, userId, id);
    }
    public void updateToken(String url) {

        if (!SessionManager.getInstance().getTokenChangedValue()) {
            return;
        }
        String id = SessionManager.getInstance().getUserId();
        url = SessionManager.getInstance().getBaseUrl() + "/api/users/" + id;
        String token = SessionManager.getInstance().getTokenKey();
        UserManager.updateToken(url, token, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                loginPresenter.onTokenUpdatedSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                loginPresenter.onTokenUpdatedFailure();
            }
        });


    }


}
