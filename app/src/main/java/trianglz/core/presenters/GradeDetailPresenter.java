package trianglz.core.presenters;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public interface GradeDetailPresenter {
    void onGetGradesDetailsSuccess();
    void onGetGradesDetailsFailure (String message, int errorCode);
}
