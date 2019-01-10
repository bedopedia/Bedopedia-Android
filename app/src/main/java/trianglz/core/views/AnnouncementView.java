package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.AnnouncementInterface;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.AnnoucementReceiver;
import trianglz.models.Announcement;
import trianglz.utils.Constants;

public class AnnouncementView {
    private Context context;
    private AnnouncementInterface announcementInterface;

    public AnnouncementView(Context context, AnnouncementInterface announcementInterface) {
        this.context = context;
        this.announcementInterface = announcementInterface;
    }

    public void getAnnouncement(String url){
        UserManager.getAnnouncements(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                announcementInterface.onGetAnnouncementSuccess(parseAnnouncementResponse(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                announcementInterface.onGetAnnouncementFailure(message,errorCode);

            }
        });
    }

    private ArrayList<Announcement> parseAnnouncementResponse(JSONObject response){
        JSONArray announcementJsonArray  = response.optJSONArray(Constants.KEY_ANNOUNCEMENTS);
        ArrayList<Announcement> announcementArrayList = new ArrayList<>();
        for(int i = 0; i< announcementJsonArray.length(); i++){
            JSONObject announcementJsonObject = announcementJsonArray.optJSONObject(i);
            ArrayList<AnnoucementReceiver> annoucementReceiverArrayList = new ArrayList<>();
            int id = announcementJsonObject.optInt(Constants.KEY_ID);
            String title = announcementJsonObject.optString(Constants.KEY_TITLE);
            String body = announcementJsonObject.optString(Constants.KEY_BODY);
            String endAt = announcementJsonObject.optString(Constants.KEY_END_AT);
            String adminId = announcementJsonObject.optString(Constants.KEY_ADMIN_ID);
            String createdAt = announcementJsonObject.optString(Constants.KEY_CREATED_AT);
            JSONArray announcementReceiversJsonArray = announcementJsonObject.optJSONArray(Constants.KEY_ANNOUNCEMENT_RECEIVERS);
            for(int j= 0; j< announcementReceiversJsonArray.length(); j++){
                JSONObject announcementReceiverObject = announcementReceiversJsonArray.optJSONObject(j);
                int annoucementReceiverId = announcementReceiverObject.optInt(Constants.KEY_ID);
                int annoucenmentID = announcementReceiverObject.optInt(Constants.KEY_ANNOUCEMENT_ID);
                String userType =  announcementReceiverObject.optString(Constants.KEY_USER_TYPE);
                AnnoucementReceiver annoucementReceiver = new AnnoucementReceiver(annoucementReceiverId,annoucenmentID,userType);
                annoucementReceiverArrayList.add(annoucementReceiver);
            }
            announcementArrayList.add( new Announcement(id,title,body,endAt,createdAt,annoucementReceiverArrayList));
        }
        return announcementArrayList;
    }
}
