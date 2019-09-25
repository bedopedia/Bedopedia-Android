package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.SolveQuizPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

/**
 * Created by Farah A. Moniem on 25/09/2019.
 */
public class SolveQuizView {
    private SolveQuizPresenter solveQuizPresenter;
    private Context context;

    public SolveQuizView(SolveQuizPresenter solveQuizPresenter, Context context) {
        this.solveQuizPresenter = solveQuizPresenter;
        this.context = context;
    }

    void getQuizQuestions(String url){
        UserManager.getQuizQuestions(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                solveQuizPresenter.onGetQuizQuestionsSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                solveQuizPresenter.onGetQuizQuestionsFailure(message, errorCode);
            }
        });
    }
}
