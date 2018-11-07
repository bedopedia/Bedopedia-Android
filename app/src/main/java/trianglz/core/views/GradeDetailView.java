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

    public GradeDetailView(Context context, GradeDetailPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }


    public void getAverageGrade(String url,String id){
        UserManager.getAverageGrades(url,id ,new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                presenter.onGetAverageGradesSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetAverageGradeFailure();
            }
        });
    }




}
