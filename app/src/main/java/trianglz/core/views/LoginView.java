package trianglz.core.views;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.HashMap;

import trianglz.core.presenters.LoginPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Actor;
import trianglz.models.Student;
import trianglz.utils.Constants;
import trianglz.utils.Util;

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
            }

            @Override
            public void onFailure(String message, int errorCode) {
                loginPresenter.onLoginFailure(message, errorCode);
            }
        });
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

    private void parseLoginResponse(JSONObject response) {
        String username = response.optString("username");
        String userId = response.optString("id");
        String id = response.optString("child_id");
        Boolean passwordChanged = response.optBoolean("password_changed");
        int unSeenNotification = response.optInt("unseen_notifications");
        SessionManager.getInstance().createLoginSession(username, userId, id, unSeenNotification);
//        if (passwordChanged) {
            String userType = response.optString(Constants.KEY_USER_TYPE);
            if (userType.equals("parent")) {
                SessionManager.getInstance().setUserType(SessionManager.Actor.PARENT);
                refreshFireBaseToken();
                loginPresenter.onLoginSuccess();
            } else if (userType.equals("student")) {
                SessionManager.getInstance().setUserType(SessionManager.Actor.STUDENT);
                int parentId = response.optInt(Constants.PARENT_ID);
                refreshFireBaseToken();
                String url = SessionManager.getInstance().getBaseUrl() + "/api/parents/" + parentId + "/children";
                loginPresenter.onGetStudentsHomeSuccess(Student.create(response.toString()),
                        response.optJSONArray(Constants.KEY_ATTENDANCES));
            } else if (userType.equals("teacher")) {
                SessionManager.getInstance().setUserType(SessionManager.Actor.TEACHER);
                String firstName = response.optString("firstname");
                String lastName = response.optString("lastname");
                String actableType = response.optString(Constants.KEY_ACTABLE_TYPE);
                String avtarUrl = response.optString(Constants.KEY_AVATER_URL);
                Actor actor = new Actor(firstName, lastName, actableType, avtarUrl);
                refreshFireBaseToken();
                loginPresenter.onLoginSuccess(actor);
            } else if (userType.equals("hod")) {
                SessionManager.getInstance().setUserType(SessionManager.Actor.HOD);
                String firstName = response.optString("firstname");
                String lastName = response.optString("lastname");
                String actableType = response.optString(Constants.KEY_ACTABLE_TYPE);
                String avtarUrl = response.optString(Constants.KEY_AVATER_URL);
                Actor actor = new Actor(firstName, lastName, actableType, avtarUrl);
                refreshFireBaseToken();
                loginPresenter.onLoginSuccess(actor);
            } else if (userType.equals("admin")) {
                SessionManager.getInstance().setUserType(SessionManager.Actor.ADMIN);
                String firstName = response.optString("firstname");
                String lastName = response.optString("lastname");
                String actableType = response.optString(Constants.KEY_ACTABLE_TYPE);
                String avtarUrl = response.optString(Constants.KEY_AVATER_URL);
                Actor actor = new Actor(firstName, lastName, actableType, avtarUrl);
                refreshFireBaseToken();
                loginPresenter.onLoginSuccess(actor);
            }
//        }
//        } else {
//            loginPresenter.onLoginFailure("", 406);
//        }
    }

    public void updateToken() {
        String url = SessionManager.getInstance().getBaseUrl() + "/api/users/"
                + SessionManager.getInstance().getUserId();
        String token = SessionManager.getInstance().getTokenKey();
        String locale = "";
        if (Util.getLocale(context).equals("ar")) {
            locale = "ar";
        } else {
            locale = "en";
        }
        UserManager.updateToken(url, token, locale, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String message, int errorCode) {

            }
        });


    }


    public void changePassword(String url, String currentPassword, int userId, String newPassword, HashMap<String, String> headerHashMap) {
        UserManager.changePassword(url, currentPassword, userId, newPassword, headerHashMap, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                loginPresenter.onPasswordChangedSuccess(newPassword);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                loginPresenter.onPasswordChangedFailure(message, errorCode);
            }
        });

    }

}
