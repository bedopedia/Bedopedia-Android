package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubGradingPeriod {

    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("start_date")
    public String startDate;
    @SerializedName("end_date")
    public String endDate;
    @SerializedName("grade_items")
    public ArrayList<GradeItems> gradeItems;
    @SerializedName("quizzes")
    public ArrayList<Quizzes> quizzes;
    @SerializedName("assignments")
    public ArrayList<Assignments> assignments;
    
    public static SubGradingPeriod create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, SubGradingPeriod.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}
