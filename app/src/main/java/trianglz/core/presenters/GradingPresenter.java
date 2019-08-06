package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.StudentSubmission;

public interface GradingPresenter {
    void onGetAssignmentSubmissionsSuccess(ArrayList<StudentSubmission> submissions);
    void onGetAssignmentSubmissionsFailure(String message, int errorCode);

    void onPostAssignmentGradeSuccess();
    void onPostAssignmentGradeFailure(String message, int errorCode);

    void onGetQuizzesSubmissionsSuccess(ArrayList<StudentSubmission> studentSubmissions);
    void onGetQuizzesSubmissionsFailure(String message, int errorCode);
}
