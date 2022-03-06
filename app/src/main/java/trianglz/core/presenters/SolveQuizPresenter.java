package trianglz.core.presenters;

import java.util.ArrayList;
import java.util.HashMap;

import trianglz.models.Answers;
import trianglz.models.QuizQuestion;
import trianglz.models.StudentSubmissions;

/**
 * Created by Farah A. Moniem on 17/10/2019.
 */
public interface SolveQuizPresenter {

    void onCreateSubmissionSuccess(StudentSubmissions studentSubmission);

    void onCreateSubmissionFailure(String message, int errorCode);

    void onGetQuizSolveDetailsSuccess(QuizQuestion quizQuestion);

    void onGetQuizSolveDetailsFailure(String message, int errorCode);

    void onPostAnswerSubmissionSuccess(ArrayList<Answers> answers);

    void onPostAnswerSubmissionFailure(String message, int errorCode);

    void onGetAnswerSubmissionSuccess(HashMap<Integer, ArrayList<Answers>> answerSubmissionsHashMap);

    void onGetAnswerSubmissionFailure(String message, int errorCode);

    void onDeleteAnswerSubmissionSuccess();

    void onDeleteAnswerSubmissionFailure(String message, int errorCode);

    void onSubmitQuizSuccess();

    void onSubmitQuizFailure(String message, int errorCode);
}
