package trianglz.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class MessageThread implements  Serializable{
    public int courseId;
    public String courseName;
    public int id;
    public boolean isRead ;
    public String lastAddedDate;
    public ArrayList<Message> messageArrayList;
    public String name;
    public String otherAvatars;
    public  String otherNames;
    public ArrayList<Participant> participantArrayList;
    public String tag;

    public MessageThread(int courseId, String courseName, int id, boolean isRead,
                         String lastAddedDate, ArrayList<Message> messageArrayList,
                         String name, String otherAvatars, String otherNames,
                         ArrayList<Participant> participantArrayList, String tag) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.id = id;
        this.isRead = isRead;
        this.lastAddedDate = lastAddedDate;
        this.messageArrayList = messageArrayList;
        this.name = name;
        this.otherAvatars = otherAvatars;
        this.otherNames = otherNames;
        this.participantArrayList = participantArrayList;
        this.tag = tag;
    }
}

