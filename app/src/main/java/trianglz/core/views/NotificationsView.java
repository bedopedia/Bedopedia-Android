package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.NotificatoinsPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;

/**
 * This file is spawned by Gemy on 11/1/2018.
 */
public class NotificationsView {
    private Context context;
    private NotificatoinsPresenter presenter;

    public NotificationsView(Context context, NotificatoinsPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void getNotifications(String url) {
        UserManager.getNotifications(url,new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                presenter.onGetNotificationSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetNotificationFailure();
            }
        });
    }
}
