package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class WeeklyPlannerResponse implements Serializable {

    @SerializedName("weekly_plan")
    public ArrayList<WeeklyPlan> weeklyPlans;

    public static WeeklyPlannerResponse create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, WeeklyPlannerResponse.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}