package trianglz.models;

import java.io.Serializable;

public class AnnoucementReceiver  implements Serializable {
    int id;
    int announcementId;
    String userType;

    public AnnoucementReceiver(int id, int announcementId, String userType) {
        this.id = id;
        this.announcementId = announcementId;
        this.userType = userType;
    }
}
