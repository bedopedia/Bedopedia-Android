package Models;

/**
 * Created by mo2men on 02/04/17.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageAttributes implements Serializable {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("body")
    private String body;

    @SerializedName("attachment")
    private String attachment;

    public MessageAttributes() {
        this.userId = 0;
        this.body = "";
        this.attachment = "";
    }

    public MessageAttributes(int id , String mbody, String att){
        this.userId = id;
        this.body = mbody;
        this.attachment = att;
    }

    void setUserId(int id) {
        this.userId = id;
    }
    void setBody (String mbody){
        this.body = mbody;
    }

    void setAttachment(String att){
        this.attachment = att;
    }

    public int getUserId () {
        return this.userId;
    }
}
