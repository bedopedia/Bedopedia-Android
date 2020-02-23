package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class StudentGradeModel {
    @SerializedName("total")
    public double total;
    @SerializedName("grade")
    public double grade;
    @SerializedName("grade_view")
    public String gradeView;

    public static StudentGradeModel create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, StudentGradeModel.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}
