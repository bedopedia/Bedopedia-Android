package trianglz.core.presenters;

/**
 * Created by Farah A. Moniem on 25/09/2019.
 */
public interface SolveQuizPresenter {
    void onGetQuizQuestionsSuccess();
    void onGetQuizQuestionsFailure(String message,int errorCode);
}
