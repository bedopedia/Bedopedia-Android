package trianglz.core.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.http.Url;
import trianglz.core.presenters.NotificationsPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Notification;
import trianglz.models.User;
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

    public void getNotifications(String url, int pageNumber, int numberPerPage) {
        UserManager.getNotifications(url, pageNumber, numberPerPage+"",new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                presenter.onGetNotificationSuccess(parseNotificationResponse(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetNotificationFailure(message,errorCode);
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
            String time = notificationObj.optString("created_at");
            notifications.add(new Notification(notificationObj.optString("text"),
                    formatDate(time),
                    notificationObj.optString("logo"),
                    to.optString("firstname"),
                    notificationObj.optString("message")));

        }
        return notifications;
    }

    private String formatDate(String time) {
        ISO8601DateFormat iso = new ISO8601DateFormat();
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd,' 'hh:mm a");
        Date date = null;
        try {
            date = iso.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String finalDate = dateFormat.format(date);
        if (DateUtils.isToday(Objects.requireNonNull(date).getTime())) {
            return "Today";
        }
        return finalDate;
    }


    public void setAsSeen(String url){
        UserManager.setAsSeen(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String message, int errorCode) {

            }
        });

    }
}
