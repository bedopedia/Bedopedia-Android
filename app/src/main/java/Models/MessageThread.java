package Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mohamed on 2/9/17.
 */

public class MessageThread {
    private String name;
    private User creator;
    private Date lastAddedDate;
    private Date createdAt;
    private Date updatedAt;
    private String tag;
    private ArrayList<Message> messages;

    public MessageThread() {
        this.name = "";
        this.creator = new User();
        this.lastAddedDate = new Date();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.tag = "";
        messages = new ArrayList<Message>();
    }

    public MessageThread(String name, User creator, Date lastAddedDate, Date createdAt, Date updatedAt, String tag) {
        this.name = name;
        this.creator = creator;
        this.lastAddedDate = lastAddedDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tag = tag;
        messages = new ArrayList<Message>();
    }

    public void addMesssage(Message message){
        messages.add(message);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getLastAddedDate() {
        return lastAddedDate;
    }

    public void setLastAddedDate(Date lastAddedDate) {
        this.lastAddedDate = lastAddedDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
