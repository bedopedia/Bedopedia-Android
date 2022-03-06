package trianglz.core.presenters;

import trianglz.models.GradeBook;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public interface GradeDetailPresenter {
    void onGetGradesDetailsSuccess(GradeBook gradeBook);
    void onGetGradesDetailsFailure (String message, int errorCode);
}
