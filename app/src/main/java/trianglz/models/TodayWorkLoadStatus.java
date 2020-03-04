package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TodayWorkLoadStatus implements Serializable {
    @SerializedName("attendance_status")
    public String attendanceStatus;
    @SerializedName("assignments_count")
    public int assignmentsCount;
    @SerializedName("quizzes_count")
    public int quizzesCount;
    @SerializedName("events_count")
    public int eventsCount;
    public static TodayWorkLoadStatus create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, TodayWorkLoadStatus.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
