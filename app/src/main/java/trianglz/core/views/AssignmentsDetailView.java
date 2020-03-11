package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.core.presenters.AssignmentsDetailPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.AssignmentsDetail;
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
    public void getAssignmentDetail(String url, final CourseAssignment courseAssignment) {
        UserManager.getAssignmentDetail(url, new ArrayResponseListener() {
            @Override

            public void onSuccess(JSONArray response) {
                ArrayList<AssignmentsDetail> assignmentsDetailArrayList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    AssignmentsDetail assignmentsDetail = gson.fromJson(response.optJSONObject(i).toString(), AssignmentsDetail.class);
                    assignmentsDetailArrayList.add(assignmentsDetail);
                }
                presenter.onGetAssignmentDetailSuccess(assignmentsDetailArrayList,courseAssignment);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetAssignmentDetailFailure(message, errorCode);
            }
        });
    }

}
