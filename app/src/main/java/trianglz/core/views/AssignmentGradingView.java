package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.core.presenters.AssignmentGradingPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.StudentAssignmentSubmission;
import trianglz.models.StudentSubmissions;

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
                ArrayList<StudentAssignmentSubmission> submissions = new ArrayList<>();
                for (int i = 0; i < responseArray.length(); i++) {
                    submissions.add(StudentAssignmentSubmission.create(responseArray.opt(i).toString()));
                }
                assignmentGradingPresenter.onGetAssignmentSubmissionsSuccess(submissions);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                assignmentGradingPresenter.onGetAssignmentSubmissionsFailure(message, errorCode);
            }
        });
    }
}
