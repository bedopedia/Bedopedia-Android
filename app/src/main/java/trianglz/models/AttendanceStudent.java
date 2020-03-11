package trianglz.models;

/**
 * Created by Farah A. Moniem on 16/09/2019.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class AttendanceStudent {

    @SerializedName("child_id")
    private int childId;
    @SerializedName("name")
    private String name;
    @SerializedName("avatar_url")
    private String avatarUrl;

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public int getChildId() {
        return this.childId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }


    public static AttendanceStudent create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, AttendanceStudent.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}