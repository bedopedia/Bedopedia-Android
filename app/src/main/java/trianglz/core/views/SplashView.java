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
import trianglz.models.Actor;
import trianglz.utils.Constants;
import trianglz.utils.Util;

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
                parseLoginResponse(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                splashPresenter.onLoginFailure(message,errorCode);
            }
        });
    }



    private void parseLoginResponse(JSONObject response) {
        JSONObject data = response.optJSONObject("data");
        String username = data.optString("username");
        String userId = data.optString("id");
        String id = data.optString("actable_id");
        int unSeenNotification = data.optInt("unseen_notifications");
        SessionManager.getInstance().createLoginSession(username, userId, id,unSeenNotification);
        String userType = data.optString(Constants.KEY_USER_TYPE);
        if(userType.equals("parent")){
            SessionManager.getInstance().setUserType(true);
            refreshFireBaseToken();
            splashPresenter.onLoginSuccess();
        }else {
            SessionManager.getInstance().setUserType(false);
            String firstName =  data.optString("firstname");
            String lastName = data.optString("lastname");
            String actableType = data.optString(Constants.KEY_ACTABLE_TYPE);
            String avtarUrl = data.optString(Constants.KEY_AVATER_URL);
            Actor actor = new Actor(firstName,lastName,actableType,avtarUrl);
            refreshFireBaseToken();
            splashPresenter.onLoginSuccess(actor);
        }

    }


    public void refreshFireBaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                SessionManager.getInstance().setFireBaseToken(token);
                updateToken();

            }

        });
    }



    public void updateToken() {
        String url = SessionManager.getInstance().getBaseUrl() + "/api/users/"
                + SessionManager.getInstance().getUserId();
        String token = SessionManager.getInstance().getTokenKey();
        String locale = "";
        if(Util.getLocale(context).equals("ar")){
            locale = "ar";
        }else {
            locale = "en";
        }
        UserManager.updateToken(url, token,locale, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String message, int errorCode) {

            }
        });


    }


}
