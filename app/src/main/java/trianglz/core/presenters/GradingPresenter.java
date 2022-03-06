package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Feedback;
import trianglz.models.StudentSubmission;

public interface GradingPresenter {
    void onGetAssignmentSubmissionsSuccess(ArrayList<StudentSubmission> submissions);
    void onGetAssignmentSubmissionsFailure(String message, int errorCode);

    void onPostAssignmentGradeSuccess(StudentSubmission studentSubmission);
    void onPostAssignmentGradeFailure(String message, int errorCode);

    void onGetQuizzesSubmissionsSuccess(ArrayList<StudentSubmission> studentSubmissions);
    void onGetQuizzesSubmissionsFailure(String message, int errorCode);

    void onPostFeedbackSuccess(Feedback feedback);
    void onPostFeedbackFailure(String message, int errorCode);

    void onPostQuizGradeSuccess(StudentSubmission studentSubmission);
    void onPostQuizGradeFailure(String message, int errorCode);
}
