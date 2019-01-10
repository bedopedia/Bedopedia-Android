package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Announcement;

public interface AnnouncementInterface {
    void onGetAnnouncementSuccess(ArrayList<Announcement> announcementArrayList);
    void onGetAnnouncementFailure(String message, int errorCode);
}
