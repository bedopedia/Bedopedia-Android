package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class BehaviorNote implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("category")
    public String category;
    @SerializedName("note")
    public String note;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("behavior_note_category_id")
    public int behaviorNoteCategoryId;
    @SerializedName("type")
    public String type;
    @SerializedName("location")
    public String location;
    @SerializedName("consequence")
    public String consequence;
    @SerializedName("owner")
    public Owner owner;
    @SerializedName("receivers")
    public Receivers[] receivers;
    @SerializedName("student")
    public Student student;

    public BehaviorNote(String teacherName, String message) {
        this.category = teacherName;
        this.note = message;
    }
    public static BehaviorNote create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, BehaviorNote.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
