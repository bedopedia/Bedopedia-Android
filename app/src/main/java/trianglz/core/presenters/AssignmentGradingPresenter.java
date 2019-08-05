package trianglz.core.presenters;

public interface AssignmentGradingPresenter {
    void onGetAssignmentSubmissionsSuccess();
    void onGetAssignmentSubmissionsFailure(String message, int errorCode);
}
