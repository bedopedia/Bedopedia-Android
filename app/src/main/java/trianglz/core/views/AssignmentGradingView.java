package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;

import trianglz.core.presenters.AssignmentGradingPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;

public class AssignmentGradingView {
    Context context;
    AssignmentGradingPresenter assignmentGradingPresenter;

    public AssignmentGradingView(Context context, AssignmentGradingPresenter assignmentGradingPresenter) {
        this.context = context;
        this.assignmentGradingPresenter = assignmentGradingPresenter;
    }

    public void getAssignmentSubmissions(int courseId, int courseGroupId, int assignmentId) {
        UserManager.getAssignmentSubmissions(courseId, courseGroupId, assignmentId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                assignmentGradingPresenter.onGetAssignmentSubmissionsSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                assignmentGradingPresenter.onGetAssignmentSubmissionsFailure(message, errorCode);
            }
        });
    }
}
