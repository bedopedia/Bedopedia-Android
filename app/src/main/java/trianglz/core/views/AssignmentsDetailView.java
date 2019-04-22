package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import trianglz.core.presenters.AssignmentsDetailPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.AssignmentsDetail;
import trianglz.utils.Constants;

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
    }

    public void getAssignmentDetails(String url, int pageNumber, String numberPerPage){
        UserManager.getAssignmentsDetail(url, pageNumber, numberPerPage,new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                final AssignmentsDetail assignmentsDetail = gson.fromJson(((JSONObject) response).optJSONObject(Constants.KEY_ASSIGNMENTS).toString(), AssignmentsDetail.class);
                presenter.onGetAssignmentDetailSuccess(assignmentsDetail.getAssignments());
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetAssignmentDetailFailure(message, errorCode);
            }
        });
    }
}
