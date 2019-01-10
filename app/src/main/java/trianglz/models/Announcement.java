package trianglz.models;

import java.util.ArrayList;

public class Announcement {
   public int id;
   public String title;
   public  String body;
   public String endAt;
   public String createdAt;
   ArrayList<AnnoucementReceiver> annoucementReceiverArrayList;

    public Announcement(int id, String title, String body, String endAt, String createdAt, ArrayList<AnnoucementReceiver> annoucementReceiverArrayList) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.annoucementReceiverArrayList = annoucementReceiverArrayList;
    }
}
