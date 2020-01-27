package trianglz.core.presenters;

public interface GradesPresenter {
    void onGetGradesCoursesSuccess();
    void onGetGradesCoursesFailure (String message, int errorCode);
}
