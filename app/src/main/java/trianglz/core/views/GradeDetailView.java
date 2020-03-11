package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.GradeDetailPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.GradeBook;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public class GradeDetailView {

    private Context context;
    private GradeDetailPresenter presenter;

    public GradeDetailView (Context context, GradeDetailPresenter gradeDetailPresenter) {
        this.context = context;
        this.presenter = gradeDetailPresenter;
    }


    public void getGradesDetails(int studentId, int courseId, int courseGroupId, int gradingPeriodId) {
        UserManager.getGradesDetails(studentId, courseId, courseGroupId, gradingPeriodId, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                presenter.onGetGradesDetailsSuccess(GradeBook.create(response.toString()));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetGradesDetailsFailure(message, errorCode);
            }
        });
    }
}
