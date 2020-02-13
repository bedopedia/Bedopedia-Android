package trianglz.models;

import com.google.gson.annotations.SerializedName;

public class GeneralNote {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("weekly_plan_id")
    private int weeklyPlanId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("image")
    private Image image;

}
