package trianglz.core.presenters;

import trianglz.models.QuizQuestion;

/**
 * Created by Farah A. Moniem on 25/09/2019.
 */
public interface ShowQuizPresenter {
    void onGetQuizQuestionsSuccess(QuizQuestion quizQuestion);
    void onGetQuizQuestionsFailure(String message,int errorCode);
}
