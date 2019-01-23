package trianglz.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Announcement implements Serializable {
   public int id;
   public String title;
   public  String body;
   public String endAt;
   public String createdAt;
   public int adminId;
   public String imageUrl;
   ArrayList<AnnouncementReceiver> announcementReceiverArrayList;

    public Announcement(int id, String title, String body, String endAt, String createdAt,int adminId, String imageUrl, ArrayList<AnnouncementReceiver> announcementReceiverArrayList) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.announcementReceiverArrayList = announcementReceiverArrayList;
        this.adminId = adminId;
        this.imageUrl = imageUrl;
    }
}