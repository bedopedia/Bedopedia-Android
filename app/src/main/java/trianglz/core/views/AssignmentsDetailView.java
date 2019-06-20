package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.AssignmentsDetailPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.CourseAssignment;


/**
 * Created by ${Aly} on 4/22/2019.
 */
public class AssignmentsDetailView {
    private Context context;
    private Gson gson;
    private AssignmentsDetailPresenter presenter;

    public AssignmentsDetailView(Context context, AssignmentsDetailPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        gson = new Gson();
    }

    public void getAssignmentDetails(String url){
        UserManager.getAssignmentDetail(url,new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                ArrayList<CourseAssignment> courseAssignmentArrayList = new ArrayList<>();
                for(int i = 0 ; i<response.length(); i++){
                    CourseAssignment courseAssignment = gson.fromJson(response.optJSONObject(i).toString(), CourseAssignment.class);
                    courseAssignmentArrayList.add(courseAssignment);
                }
                presenter.onGetAssignmentDetailSuccess(courseAssignmentArrayList);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetAssignmentDetailFailure(message, errorCode);
            }
        });
    }
}
