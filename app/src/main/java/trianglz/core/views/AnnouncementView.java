package trianglz.core.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import trianglz.core.presenters.AnnouncementInterface;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Announcement;
import trianglz.models.AnnouncementReceiver;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class AnnouncementView {
    private Context context;
    private AnnouncementInterface announcementInterface;

    public AnnouncementView(Context context, AnnouncementInterface announcementInterface) {
        this.context = context;
        this.announcementInterface = announcementInterface;
    }

    public void getAnnouncement(String url) {
        UserManager.getAnnouncements(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                announcementInterface.onGetAnnouncementSuccess(parseAnnouncementResponse(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                announcementInterface.onGetAnnouncementFailure(message, errorCode);

            }
        });
    }

    private ArrayList<Announcement> parseAnnouncementResponse(JSONObject response) {
        JSONArray announcementJsonArray = response.optJSONArray(Constants.KEY_ANNOUNCEMENTS);
        ArrayList<Announcement> announcementArrayList = new ArrayList<>();
        for (int i = 0; i < announcementJsonArray.length(); i++) {
            JSONObject announcementJsonObject = announcementJsonArray.optJSONObject(i);
            ArrayList<AnnouncementReceiver> announcementReceiverArrayList = new ArrayList<>();
            int id = announcementJsonObject.optInt(Constants.KEY_ID);
            String title = announcementJsonObject.optString(Constants.KEY_TITLE);
            String body = announcementJsonObject.optString(Constants.KEY_BODY);
            String endAt = announcementJsonObject.optString(Constants.KEY_END_AT);
            int adminId = announcementJsonObject.optInt(Constants.KEY_ADMIN_ID);
            String createdAt = announcementJsonObject.optString(Constants.KEY_CREATED_AT);
            String imageUrl = announcementJsonObject.optString(Constants.KEY_IMAGE_URL);
            JSONArray announcementReceiversJsonArray = announcementJsonObject.optJSONArray(Constants.KEY_ANNOUNCEMENT_RECEIVERS);
            for (int j = 0; j < announcementReceiversJsonArray.length(); j++) {
                JSONObject announcementReceiverObject = announcementReceiversJsonArray.optJSONObject(j);
                int annoucementReceiverId = announcementReceiverObject.optInt(Constants.KEY_ID);
                int annoucenmentID = announcementReceiverObject.optInt(Constants.KEY_ANNOUCEMENT_ID);
                String userType = announcementReceiverObject.optString(Constants.KEY_USER_TYPE);
                AnnouncementReceiver announcementReceiver = new AnnouncementReceiver(annoucementReceiverId, annoucenmentID, userType);
                announcementReceiverArrayList.add(announcementReceiver);
            }
            announcementArrayList.add(new Announcement(id, title, body, Util.getAnnouncementDate(endAt, context), Util.getAnnouncementDate(createdAt, context), adminId, imageUrl, announcementReceiverArrayList));
        }
        //todo uncomment when date format is changed
//        Collections.sort(announcementArrayList,new SortByDate());
        Collections.reverse(announcementArrayList);
        return announcementArrayList;
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

    public static class SortByDate implements Comparator<Announcement> {
        @Override
        public int compare(Announcement a, Announcement b) {
            return Util.convertStringToDate(a.endAt).compareTo(Util.convertStringToDate(b.endAt));
        }
    }
}
