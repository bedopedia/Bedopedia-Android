package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.NotificationsPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Notification;
import trianglz.utils.Constants;

/**
 * This file is spawned by Gemy on 11/1/2018.
 */
public class NotificationsView {
    private Context context;
    private NotificationsPresenter presenter;

    public NotificationsView(Context context, NotificationsPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void getNotifications(String url) {
        UserManager.getNotifications(url,new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                presenter.onGetNotificationSuccess(parseNotificationResponse(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetNotificationFailure();
            }
        });
    }

    private ArrayList<Notification> parseNotificationResponse(JSONObject response) {
       ArrayList<Notification> notifications = new ArrayList<>();
        JSONArray notificationArray = new JSONArray();
        try {
            notificationArray = response.getJSONArray(Constants.KEY_NOTIFICATION);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < notificationArray.length(); i++) {

            JSONObject notificationObj = notificationArray.optJSONObject(i);
            JSONObject to = notificationObj.optJSONObject("to");
            String name = to.optString("firstname");
            notifications.add(new Notification(notificationObj.optString("text"),
                    notificationObj.optString("created_at"),
                    notificationObj.optString("logo"),
                    name,
                    notificationObj.optString("message")));

        }
        return notifications;
    }
}
