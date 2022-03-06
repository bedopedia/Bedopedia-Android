package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GradesDetailsResponse {

    @SerializedName("grading_periods")
    public ArrayList<GradingPeriod> gradingPeriods;

    public static GradesDetailsResponse create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, GradesDetailsResponse.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
