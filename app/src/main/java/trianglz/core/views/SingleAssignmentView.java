package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import trianglz.core.presenters.SingleAssignmentPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.SingleAssignment;

/**
 * Created by gemy
 * */
public class SingleAssignmentView {
    private Context context;
    private Gson gson;
    private SingleAssignmentPresenter presenter;

    public SingleAssignmentView(Context context, SingleAssignmentPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        gson = new Gson();
    }
    public void showAssignment (int courseId, int assignmentId) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.showAssignment(courseId, assignmentId);
        UserManager.showAssignment(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                SingleAssignment singleAssignment = SingleAssignment.create(response.toString());
                presenter.onShowAssignmentSuccess(singleAssignment);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onShowAssignmentFailure(message, errorCode);
            }
        });
    }
}
