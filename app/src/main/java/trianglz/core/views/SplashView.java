package trianglz.core.views;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.SplashPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Actor;
import trianglz.models.Student;
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

    private void parseLoginResponse(JSONObject response) {
        String username = response.optString("username");
        String userId = response.optString("id");
        String id = response.optString("child_id");
        int unSeenNotification = response.optInt("unseen_notifications");
        int runningAnnouncements = response.optInt("running_announcements_count");
        Boolean showFees = response.optBoolean("show_school_fees");
        SessionManager.getInstance().createLoginSession(username, userId, id, unSeenNotification, runningAnnouncements, showFees);
        String userType = response.optString(Constants.KEY_USER_TYPE);
        switch (userType) {
            case "parent":
                SessionManager.getInstance().setUserType(SessionManager.Actor.PARENT);
                splashPresenter.onParentLoginSuccess(Student.getArrayList(response.optJSONArray(Constants.CHILDREN).toString()));
                break;
            case "student":
                SessionManager.getInstance().setUserType(SessionManager.Actor.STUDENT);
                splashPresenter.onStudentLoginSuccess(Student.create(response.toString()),
                        response.optJSONArray(Constants.KEY_ATTENDANCES));
                break;
            case "teacher": {
                SessionManager.getInstance().setUserType(SessionManager.Actor.TEACHER);
                String firstName = response.optString("firstname");
                String lastName = response.optString("lastname");
                String actableType = response.optString(Constants.KEY_ACTABLE_TYPE);
                String avtarUrl = response.optString(Constants.KEY_AVATER_URL);
                Actor actor = new Actor(firstName, lastName, actableType, avtarUrl);
                splashPresenter.onLoginSuccess(actor);
                break;
            }
            case "hod": {
                SessionManager.getInstance().setUserType(SessionManager.Actor.HOD);
                String firstName = response.optString("firstname");
                String lastName = response.optString("lastname");
                String actableType = response.optString(Constants.KEY_ACTABLE_TYPE);
                String avtarUrl = response.optString(Constants.KEY_AVATER_URL);
                Actor actor = new Actor(firstName, lastName, actableType, avtarUrl);
                splashPresenter.onLoginSuccess(actor);
                break;
            }
            case "admin": {
                SessionManager.getInstance().setUserType(SessionManager.Actor.ADMIN);
                String firstName = response.optString("firstname");
                String lastName = response.optString("lastname");
                String actableType = response.optString(Constants.KEY_ACTABLE_TYPE);
                String avtarUrl = response.optString(Constants.KEY_AVATER_URL);
                Actor actor = new Actor(firstName, lastName, actableType, avtarUrl);
                splashPresenter.onLoginSuccess(actor);
                break;
            }
        }

    }


    public void refreshFireBaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.d("token", "onSuccess: " + token);
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
        if (Util.getLocale(context).equals("ar")) {
            locale = "ar";
        } else {
            locale = "en";
        }
        UserManager.updateToken(url, token, locale, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                getProfile();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                splashPresenter.updateTokenFailure();
            }
        });


    }

    private void getProfile() {
        UserManager.getProfile(Integer.parseInt(SessionManager.getInstance().getUserId()), new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                parseLoginResponse(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                splashPresenter.onLoginFailure(message, errorCode);
            }
        });
    }

    public void getStudents(String url, final String id, final String studentId) {
        UserManager.getStudentsHome(url, id, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                // refreshFireBaseToken();
                splashPresenter.onStudentLoginSuccess(null, null);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                splashPresenter.onGetStudentsHomeFailure(message, errorCode);
            }
        });
    }

    private ArrayList<Object> parseStudentResponse(JSONArray response, String id) {
        ArrayList<Object> objectArrayList = new ArrayList<>();
        ArrayList<JSONArray> kidsAttendances = new ArrayList<>();
        ArrayList<trianglz.models.Student> myKids = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject studentData = response.optJSONObject(i);
            JSONArray attenobdances = studentData.optJSONArray("attendances");
            String studentId = studentData.optString("id");
            if (id.equals(studentId)) {
                kidsAttendances.add(attenobdances);
                myKids.add(Student.create(studentData.toString()));
                objectArrayList.add(kidsAttendances);
                objectArrayList.add(myKids);
            }

        }
        return objectArrayList;

    }


}
