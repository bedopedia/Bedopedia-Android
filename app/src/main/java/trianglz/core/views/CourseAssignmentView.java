package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.core.presenters.CourseAssignmentPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.CourseAssignment;

/**
 * Created by ${Aly} on 6/20/2019.
 */
public class CourseAssignmentView {
    private Context context;
    private Gson gson;
    private CourseAssignmentPresenter presenter;

    public CourseAssignmentView(Context context, CourseAssignmentPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        gson = new Gson();
    }

    public void getCourseAssignment(String url) {
        UserManager.getCourseAssignment(url, new ArrayResponseListener() {
            @Override

            public void onSuccess(JSONArray response) {
                ArrayList<CourseAssignment> courseAssignmentArrayList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    CourseAssignment courseAssignment = gson.fromJson(response.optJSONObject(i).toString(), CourseAssignment.class);
                    courseAssignmentArrayList.add(courseAssignment);
                }
                presenter.onGetCourseAssignmentSuccess(courseAssignmentArrayList);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetCourseAssignmentFailure(message, errorCode);
            }
        });
    }

    public void getAssinmentDetail(String url) {
        UserManager.getAssignmentDetail(url, new ArrayResponseListener() {
            @Override

            public void onSuccess(JSONArray response) {
                ArrayList<CourseAssignment> courseAssignmentArrayList = new ArrayList<>();
//                for (int i = 0; i < response.length(); i++) {
//                    CourseAssignment courseAssignment = gson.fromJson(response.optJSONObject(i).toString(), CourseAssignment.class);
//                    courseAssignmentArrayList.add(courseAssignment);
//                }
                presenter.onGetCourseAssignmentSuccess(courseAssignmentArrayList);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetCourseAssignmentFailure(message, errorCode);
            }
        });
    }
}
