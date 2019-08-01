package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Quizzes;

public interface SingleCourseGroupPresenter {
    void onGetTeacherQuizzesSuccess(ArrayList<Quizzes> quizzes);
    void onGetTeacherQuizzesFailure(String message, int errorCode);
}
