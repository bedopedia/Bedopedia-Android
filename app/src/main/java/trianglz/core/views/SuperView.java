package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.SuperPresenter;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.utils.Util;

public class SuperView {
    private Context context;
    private SuperPresenter superPresenter;

    public SuperView(Context context, SuperPresenter superPresenter) {
        this.context = context;
        this.superPresenter = superPresenter;
    }

    public void updateToken() {
        String url = SessionManager.getInstance().getBaseUrl() + "/api/users/"
                + SessionManager.getInstance().getUserId();
        String token = "";
        String locale = "";
        if(Util.getLocale(context).equals("ar")){
            locale = "ar";
        }else {
            locale = "en";
        }
        UserManager.updateToken(url, token,locale, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String message, int errorCode) {

            }
        });


    }


    public void logout(String deviceId) {
        UserManager.logout(deviceId,new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
            }

            @Override
            public void onFailure(String message, int errorCode) {
            }
        });
    }
}
