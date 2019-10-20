package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.SolveQuizPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.QuizQuestion;
import trianglz.models.StudentSubmission;

/**
 * Created by Farah A. Moniem on 17/10/2019.
 */
public class SolveQuizView {
    Context context;
    SolveQuizPresenter solveQuizPresenter;

    public SolveQuizView(Context context, SolveQuizPresenter solveQuizPresenter) {
        this.context = context;
        this.solveQuizPresenter = solveQuizPresenter;
    }


    public void createQuizSubmission(String url, int quizId, int studentId, int courseGroupId, int score) {
        UserManager.createQuizSubmission(url, quizId, studentId, courseGroupId, score, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                StudentSubmission studentSubmission = StudentSubmission.create(response.toString());
                solveQuizPresenter.onCreateSubmissionSuccess(studentSubmission);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                solveQuizPresenter.onCreateSubmissionFailure(message,errorCode);
            }
        });
    }
    public void getQuizSolveDetails(String url){
        UserManager.getQuizSolveDetails(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                QuizQuestion quizQuestion = QuizQuestion.create(response.toString());
                solveQuizPresenter.onGetQuizSolveDetailsSuccess(quizQuestion);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                solveQuizPresenter.onGetQuizSolveDetailsFailure(message,errorCode);
            }
        });
    }
}
