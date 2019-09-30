package trianglz.core.views;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.LoginPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ArrayResponseListener;
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
        JSONObject data = response.optJSONObject("data");
        String username = data.optString("username");
        String userId = data.optString("id");
        String id = data.optString("actable_id");
        int unSeenNotification = data.optInt("unseen_notifications");
        SessionManager.getInstance().createLoginSession(username, userId, id, unSeenNotification);
        String userType = data.optString(Constants.KEY_USER_TYPE);
        if (userType.equals("parent")) {
            SessionManager.getInstance().setUserType(SessionManager.Actor.PARENT);
            refreshFireBaseToken();
            loginPresenter.onLoginSuccess();
        } else if (userType.equals("student")) {
            SessionManager.getInstance().setUserType(SessionManager.Actor.STUDENT);
            int parentId = data.optInt(Constants.PARENT_ID);
            String url = SessionManager.getInstance().getBaseUrl() + "/api/parents/" + parentId + "/children";
            getStudents(url, id + "",id);
        } else if(userType.equals("teacher")) {
            SessionManager.getInstance().setUserType(SessionManager.Actor.TEACHER);
            String firstName = data.optString("firstname");
            String lastName = data.optString("lastname");
            String actableType = data.optString(Constants.KEY_ACTABLE_TYPE);
            String avtarUrl = data.optString(Constants.KEY_AVATER_URL);
            Actor actor = new Actor(firstName, lastName, actableType, avtarUrl);
            refreshFireBaseToken();
            loginPresenter.onLoginSuccess(actor);
        }else if(userType.equals("hod")){
            SessionManager.getInstance().setUserType(SessionManager.Actor.HOD);
            String firstName = data.optString("firstname");
            String lastName = data.optString("lastname");
            String actableType = data.optString(Constants.KEY_ACTABLE_TYPE);
            String avtarUrl = data.optString(Constants.KEY_AVATER_URL);
            Actor actor = new Actor(firstName, lastName, actableType, avtarUrl);
            refreshFireBaseToken();
            loginPresenter.onLoginSuccess(actor);
        }else if(userType.equals("admin")) {
            SessionManager.getInstance().setUserType(SessionManager.Actor.ADMIN);
            String firstName = data.optString("firstname");
            String lastName = data.optString("lastname");
            String actableType = data.optString(Constants.KEY_ACTABLE_TYPE);
            String avtarUrl = data.optString(Constants.KEY_AVATER_URL);
            Actor actor = new Actor(firstName, lastName, actableType, avtarUrl);
            refreshFireBaseToken();
            loginPresenter.onLoginSuccess(actor);
        }
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


    public void getStudents(String url, final String id, final String studentId) {
        UserManager.getStudentsHome(url, id, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                refreshFireBaseToken();
                loginPresenter.onGetStudentsHomeSuccess(parseStudentResponse(responseArray,id));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                loginPresenter.onGetStudentsHomeFailure(message, errorCode);
            }
        });
    }

    private ArrayList<Object> parseStudentResponse(JSONArray response,String id) {
        ArrayList<Object> objectArrayList = new ArrayList<>();
        ArrayList<JSONArray> kidsAttendances = new ArrayList<>();
        ArrayList<trianglz.models.Student> myKids = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject studentData = response.optJSONObject(i);
            JSONArray attenobdances = studentData.optJSONArray("attendances");
            String studentId = studentData.optString("id");
            if (id.equals(studentId)) {
                kidsAttendances.add(attenobdances);
                myKids.add(new Student(Integer.parseInt(studentData.optString("id")),
                        studentData.optString("firstname"),
                        studentData.optString("lastname"),
                        studentData.optString("gender"),
                        studentData.optString("email"),
                        studentData.optString("avatar_url"),
                        studentData.optString("user_type"),
                        studentData.optString("level_name"),
                        studentData.optString("section_name"),
                        studentData.optString("stage_name"),
                        studentData.optJSONObject("today_workload_status"),
                        0, studentData.optInt("user_id"),null, null));
                objectArrayList.add(kidsAttendances);
                objectArrayList.add(myKids);
            }

        }
        return objectArrayList;

    }

}
