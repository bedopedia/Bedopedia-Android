package Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mohamed on 2/9/17.
 */

public class Message {
    private String body;
    private Date createdAt;
    private Date updatedAt;
    private User creator;
    private MessageThread messageThread;

    public Message() {
        this.body = "";
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.creator = new User();
        this.messageThread = new MessageThread();
    }

    public Message(String body, Date createdAt, Date updatedAt, User creatorUser, MessageThread messageThread) {
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.creator = creatorUser;
        this.messageThread = messageThread;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public MessageThread getMessageThread() {
        return messageThread;
    }

    public void setMessageThread(MessageThread messageThread) {
        this.messageThread = messageThread;
    }
}
