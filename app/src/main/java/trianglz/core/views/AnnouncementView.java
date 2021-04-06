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
        ArrayList<Announcement> announcements = new ArrayList<>();
        for (int i = 0; i < ((announcementJsonArray != null) ? announcementJsonArray.length() : 0); i++) {
            Announcement announcement = Announcement.create(announcementJsonArray.optString(i));
            announcements.add(announcement);
        }
        Collections.sort(announcements,new SortByDate());
        Collections.reverse(announcements);
        return announcements;
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
            if (a.getEndAt() == null || b.getEndAt() == null) return 1;
            return Util.convertIsoToDate(a.getEndAt()).compareTo(Util.convertIsoToDate(b.getEndAt()));
        }
    }
}
