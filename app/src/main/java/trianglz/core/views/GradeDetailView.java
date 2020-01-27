package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.GradeDetailPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

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


    public void getGradesDetails(int studentId, int courseId, int courseGroupId) {
        UserManager.getGradesDetails(studentId, courseId, courseGroupId, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                presenter.onGetGradesDetailsSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetGradesDetailsFailure(message, errorCode);
            }
        });
    }
}
