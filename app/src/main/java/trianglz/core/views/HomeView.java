package trianglz.core.views;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.HomePresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Student;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class HomeView {
    private Context context;
    private HomePresenter presenter;

    public HomeView(Context context, HomePresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void getStudents(String url, String id) {
        UserManager.getStudentsHome(url, id, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                presenter.onGetStudentsHomeSuccess(parseHomeResponse(responseArray));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetStudentsHomeFailure();
            }
        });
    }

    private ArrayList<Object> parseHomeResponse(JSONArray response) {
        ArrayList<Object> objectArrayList = new ArrayList<>();
        ArrayList<JSONArray> kidsAttendances = new ArrayList<>();
        ArrayList<trianglz.models.Student> myKids = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject studentData = response.optJSONObject(i);
            JSONArray attenobdances = studentData.optJSONArray("attendances");
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
                    0, null, null));
        }
        objectArrayList.add(kidsAttendances);
        objectArrayList.add(myKids);
        return objectArrayList;

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
        if (!SessionManager.getInstance().getTokenChangedValue()) {
            return;
        }
        String url = SessionManager.getInstance().getBaseUrl() + "/api/users/"
                + SessionManager.getInstance().getUserId();
        String token = SessionManager.getInstance().getTokenKey();
        UserManager.updateToken(url, token, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
            }

            @Override
            public void onFailure(String message, int errorCode) {
            }
        });


    }

}
