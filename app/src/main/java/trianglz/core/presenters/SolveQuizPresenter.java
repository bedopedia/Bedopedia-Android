package trianglz.core.presenters;

import trianglz.models.StudentSubmission;

/**
 * Created by Farah A. Moniem on 17/10/2019.
 */
public interface SolveQuizPresenter {

    void onCreateSubmissionSuccess(StudentSubmission studentSubmission);
    void onCreateSubmissionFailure(String message, int errorCode);
}
