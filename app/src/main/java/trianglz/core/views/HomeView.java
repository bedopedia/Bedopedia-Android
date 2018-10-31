package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.HomePresenter;
import trianglz.managers.api.ArrayResponseListener;
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

    private ArrayList<Student> parseHomeResponse(JSONArray response) {
        ArrayList<JSONArray> kidsAttendances = new ArrayList<>();
        ArrayList<trianglz.models.Student> myKids = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject studentData = response.optJSONObject(i);
            JSONArray attenobdances = studentData.optJSONArray("attendances");
            kidsAttendances.add(attenobdances);
            myKids.add(new Student(Integer.parseInt(studentData.optString("id")),
                    studentData.optString("firstname").substring(1, studentData.optString("firstname").length() - 1),
                    studentData.optString("lastname").substring(1, studentData.optString("lastname").length() - 1),
                    studentData.optString("gender").substring(1, studentData.optString("gender").length() - 1),
                    studentData.optString("email").substring(1, studentData.optString("email").length() - 1),
                    studentData.optString("avatar_url").substring(1, studentData.optString("avatar_url").length() - 1),
                    studentData.optString("user_type").substring(1, studentData.optString("user_type").length() - 1),
                    studentData.optString("level_name").substring(1, studentData.optString("level_name").length() - 1),
                    studentData.optString("section_name").substring(1, studentData.optString("section_name").length() - 1),
                   "",
                    studentData.optJSONObject("today_workload_status"),
                    0, null, null));
        }

        return myKids;

    }

}
