package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import trianglz.core.presenters.NewMessagePresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class NewMessageView {
    private Context context;
    private NewMessagePresenter newMessagePresenter;

    public NewMessageView(Context context, NewMessagePresenter newMessagePresenter) {
        this.context = context;
        this.newMessagePresenter = newMessagePresenter;
    }

    public void getCourseGroups(String url){
        UserManager.getCourseGroups(url, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                newMessagePresenter.onGetCourseGroupsSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                newMessagePresenter.onGetCourseGroupsFailure(message,errorCode);
            }
        });
    }
}
