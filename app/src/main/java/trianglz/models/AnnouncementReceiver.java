package trianglz.models;

import java.io.Serializable;

public class AnnouncementReceiver implements Serializable {
    int id;
    int announcementId;
    String userType;

    public AnnouncementReceiver(int id, int announcementId, String userType) {
        this.id = id;
        this.announcementId = announcementId;
        this.userType = userType;
    }
}
