package Models;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ali on 20/03/17.
 */

public class MessageThread implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("date")
    private String date;

    @SerializedName("lastMessage")
    private String lastMessage;

    @SerializedName("title")
    private String title;

    @SerializedName("notSeenCnt")
    private Integer notSeenCnt;

    @SerializedName("imagePath")
    private String imagePath;

    @SerializedName("othersName")
    String othersName;

    @SerializedName("messages")
    private ArrayList<Message> messages;

    @SerializedName("messages_attributes")
    private ArrayList <MessageAttributes> messageAttributes;

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

    public void sendMessage(MessageAttributes newMessage) {
        if(this.messageAttributes == null){
            this.messageAttributes = new ArrayList<>();
        }
        this.messageAttributes.add(newMessage);
    }

    public void updateLastMessage(Message last){
        this.messages.add(last);
    }

    public void reverseMessagesOrder() {
        Collections.reverse(this.messages);
    }
}

