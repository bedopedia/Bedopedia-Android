package trianglz.core.presenters;

import trianglz.models.GradesDetailsResponse;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public interface GradeDetailPresenter {
    void onGetGradesDetailsSuccess(GradesDetailsResponse gradesDetailsResponse);
    void onGetGradesDetailsFailure (String message, int errorCode);
}
