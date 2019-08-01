package trianglz.core.presenters;

public interface SingleCourseGroupPresenter {
    void onGetTeacherQuizzesSuccess();
    void onGetTeacherQuizzesFailure(String message, int errorCode);
}
