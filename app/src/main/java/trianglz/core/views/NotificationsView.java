package trianglz.core.views;

import android.content.Context;
import android.text.format.DateUtils;

import com.skolera.skolera_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import trianglz.core.presenters.NotificationsPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Meta;
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

    public void getNotifications(String url, int pageNumber, int numberPerPage) {
        UserManager.getNotifications(url, pageNumber, numberPerPage + "", new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                Meta meta = new Meta(response.optJSONObject(Constants.KEY_META));
                presenter.onGetNotificationSuccess(parseNotificationResponse(response), meta);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetNotificationFailure(message, errorCode);
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
            String studentsNames = "";
            if(to != null){
              studentsNames=  to.optString("firstname");
            }
            String time = notificationObj.optString("created_at");
            notifications.add(new Notification(notificationObj.optString("text"),
                    formatDate(time),
                    notificationObj.optString("logo"),
                    studentsNames,
                    notificationObj.optString("message")));
        }
        return notifications;
    }

    private String formatDate(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat finalFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"));

//        if (DateUtils.isToday(Objects.requireNonNull(date).getTime())) {
//            return context.getResources().getString(R.string.today);
//        }
        return finalFormat.format(date);
    }


    public void setAsSeen(String url) {
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
