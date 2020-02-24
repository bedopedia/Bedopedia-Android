package trianglz.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GeneralNote implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("description")
    public String description;
    @SerializedName("weekly_plan_id")
    public int weeklyPlanId;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("image")
    public Image image;

}
