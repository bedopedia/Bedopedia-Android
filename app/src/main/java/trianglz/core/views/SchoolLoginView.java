package trianglz.core.views;

import android.content.Context;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.Serializable;

import trianglz.core.presenters.SchoolLoginPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.utils.Constants;

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

    public void getSchoolUrl(String url,String code){
        UserManager.getSchoolUrl(url,code, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                presenter.onGetSchoolUrlSuccess();
                
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetSchoolUrlFailure();
            }
        });
    }
}
