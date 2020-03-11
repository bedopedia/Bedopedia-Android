package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * This file is spawned by Gemy on 1/20/2019.
 */

public class PlannerSubject implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("class_work")
    public String classWork;
    @SerializedName("homework")
    public String homework;
    @SerializedName("activities")
    public String activities;
    @SerializedName("date")
    public String date;
    @SerializedName("weekly_plan_id")
    public int weeklyPlanId;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("course_id")
    public int courseId;

    public static PlannerSubject create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, PlannerSubject.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}


