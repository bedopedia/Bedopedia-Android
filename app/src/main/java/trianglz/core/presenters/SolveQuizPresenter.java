package trianglz.core.presenters;

import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.models.Answers;
import trianglz.models.QuizQuestion;
import trianglz.models.StudentSubmission;

/**
 * Created by Farah A. Moniem on 17/10/2019.
 */
public interface SolveQuizPresenter {

    void onCreateSubmissionSuccess(StudentSubmission studentSubmission);
    void onCreateSubmissionFailure(String message, int errorCode);
    void onGetQuizSolveDetailsSuccess(QuizQuestion quizQuestion);
    void onGetQuizSolveDetailsFailure(String message, int errorCode);
    void onPostAnswerSubmissionSuccess(ArrayList<Answers> answers);
    void onPostAnswerSubmissionFailure(String message, int errorCode);
    void onGetAnswerSubmissionSuccess(JSONObject jsonObject);
    void onGetAnswerSubmissionFailure(String message, int errorCode);
    void onDeleteAnswerSubmissionSuccess();
    void onDeleteAnswerSubmissionFailure(String message, int errorCode);
}
