package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Receivers {
    @SerializedName("id")
    public int id;
    @SerializedName("behavior_note_id")
    public int behaviorNoteId;
    @SerializedName("user_type")
    public String userType;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("deleted_at")
    public String deletedAt;
    public static Receivers create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Receivers.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
