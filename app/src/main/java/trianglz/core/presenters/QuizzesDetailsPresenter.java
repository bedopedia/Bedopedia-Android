package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Meta;
import trianglz.models.QuizQuestion;
import trianglz.models.Quizzes;

// created by gemy
public interface QuizzesDetailsPresenter {
    void onGetQuizzesDetailsSuccess(ArrayList<Quizzes> quizzes, Meta meta);
    void onGetQuizzesDetailsFailure(String message, int errorCode);
    void onSubmitQuizSuccess();
    void onSubmitQuizFailure(String message, int errorCode);

    void onGetQuizQuestionsSuccess(QuizQuestion quizQuestion);
    void onGetQuizQuestionsFailure(String message,int errorCode);
    void onGetTeacherQuizzesSuccess(ArrayList<Quizzes> quizzes);
    void onGetTeacherQuizzesFailure(String message, int errorCode);
}
