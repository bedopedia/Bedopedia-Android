package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.ShowQuizPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.QuizQuestion;

/**
 * Created by Farah A. Moniem on 25/09/2019.
 */
public class ShowQuizView {
    private ShowQuizPresenter showQuizPresenter;
    private Context context;

    public ShowQuizView(ShowQuizPresenter showQuizPresenter, Context context) {
        this.showQuizPresenter = showQuizPresenter;
        this.context = context;
    }

    public void getQuizQuestions(String url){
        UserManager.getQuizQuestions(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                QuizQuestion quizQuestion = QuizQuestion.create(response.toString());
                showQuizPresenter.onGetQuizQuestionsSuccess(quizQuestion);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                showQuizPresenter.onGetQuizQuestionsFailure(message, errorCode);
            }
        });
    }
}
