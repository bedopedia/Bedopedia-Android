package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.HomePresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class HomeView {
    private Context context;
    private HomePresenter presenter;

    public HomeView(Context context, HomePresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void getStudents(String url, String id){
        UserManager.getStudentsHome(url, id, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String message, int errorCode) {

            }
        });
    }
}
