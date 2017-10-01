package Models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Collections;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 20/03/17.
 */

public class NewMessageThread implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("course_id")
    private String course_id;

    @SerializedName("tag")
    private String tag;


    @SerializedName("messages_attributes")
    private ArrayList <MessageAttributes> messageAttributes;

    public NewMessageThread() {

    }

    public NewMessageThread(String name, String course_id, String tag) {
        this.name = name;
        this.course_id = course_id;
        this.tag = tag;

    }

    public void sendMessage(MessageAttributes newMessage) {
        this.messageAttributes = new ArrayList<>();
        this.messageAttributes.add(newMessage);
    }

}

