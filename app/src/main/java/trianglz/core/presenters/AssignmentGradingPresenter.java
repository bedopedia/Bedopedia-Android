package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.StudentAssignmentSubmission;
import trianglz.models.StudentSubmissions;

public interface AssignmentGradingPresenter {
    void onGetAssignmentSubmissionsSuccess(ArrayList<StudentAssignmentSubmission> submissions);
    void onGetAssignmentSubmissionsFailure(String message, int errorCode);

    void onPostAssignmentGradeSuccess();
    void onPostAssignmentGradeFailure(String message, int errorCode);
}
