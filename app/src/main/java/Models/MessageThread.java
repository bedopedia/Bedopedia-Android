package Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ali on 20/03/17.
 */

public class MessageThread implements Serializable {

    private int id;
    private String date;
    private String lastMessage;
    private String title;
    private Integer notSeenCnt;
    private String imagePath;
    String othersName;
    private ArrayList<Message> messages;

    public MessageThread() {
        messages = new ArrayList<Message>();
    }

    public MessageThread(String date, String lastMessage, String title, Integer notSeenCnt, String imagePath,
                         ArrayList<Message> messages, int id, String othersName) {
        this.date = date;
        this.lastMessage = lastMessage;
        this.title = title;
        this.notSeenCnt = notSeenCnt;
        this.imagePath = imagePath;
        this.messages = messages;
        this.id = id;
        this.othersName = othersName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNotSeenCnt() {
        return notSeenCnt;
    }

    public void setNotSeenCnt(Integer notSeenCnt) {
        this.notSeenCnt = notSeenCnt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getOthersName() {
        return othersName;
    }

    public void setOthersName(String othersName) {
        this.othersName = othersName;
    }
}
