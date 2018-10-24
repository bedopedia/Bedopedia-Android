package trianglz.core.views;

import android.content.Context;

import trianglz.core.presenters.SchoolLoginPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class SchoolLoginView {
    private Context context;
    private SchoolLoginPresenter presenter;

    public SchoolLoginView(Context context, SchoolLoginPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void getSchoolUrl(String url,String header){
        UserManager.getSchoolUrl(url,header, new ResponseListener() {
            @Override
            public void onSuccess(Object response) {
                presenter.onGetSchoolUrlSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetSchoolUrlFailure();
            }
        });
    }
}
