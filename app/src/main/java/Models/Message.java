package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed on 2/9/17.
 */

public class Message implements Serializable {

    @SerializedName("body")
    private String body;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("user")
    private User creator;

    @SerializedName("messageThreadId")
    private int messageThreadId;


    public Message() {
        this.body = "";
        this.createdAt = "";
        this.updatedAt = "";
        this.creator = new User();
        this.messageThreadId = 0;
    }

    public Message(String body, String createdAt, String updatedAt, User creatorUser, int messageThreadId) {
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.creator = creatorUser;
        this.messageThreadId = messageThreadId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public int getMessageThreadId() {
        return messageThreadId;
    }

    public void setMessageThreadId(int messageThreadId) {
        this.messageThreadId = messageThreadId;
    }
}
