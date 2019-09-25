package trianglz.core.presenters;

import org.json.JSONObject;

/**
 * Created by Farah A. Moniem on 25/09/2019.
 */
public interface SolveQuizPresenter {
    void onGetQuizQuestionsSuccess(JSONObject response);
    void onGetQuizQuestionsFailure(String message,int errorCode);
}
