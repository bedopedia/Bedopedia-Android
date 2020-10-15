package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GradeBook {

    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
//    @SerializedName("start_date")
//    public String startDate;
//    @SerializedName("end_date")
//    public String endDate;
//    @SerializedName("created_at")
//    public String createdAt;
//    @SerializedName("updated_at")
//    public String updatedAt;
//    @SerializedName("level_id")
//    public Object levelId;
//    @SerializedName("academic_term_id")
//    public Object academicTermId;
//    @SerializedName("deleted_at")
//    public String deletedAt;
//    @SerializedName("weight")
//    public double weight;
//    @SerializedName("parent_id")
//    public int parentId;
//    @SerializedName("lock")
//    public boolean lock;
//    @SerializedName("publish")
//    public boolean publish;
    @SerializedName("category_is_numeric")
    public boolean categoryIsNumeric;
//    @SerializedName("courses_grading_period_id")
//    public int coursesGradingPeriodId;
    @SerializedName("categories")
    public ArrayList<Category> categories;
//    @SerializedName("total")
//    public int total;
    @SerializedName("grade")
    public String grade;
//    @SerializedName("percentage")
//    public double percentage;
//    @SerializedName("grade_view")
//    public String gradeView;
    @SerializedName("letter_scale")
    public String letterScale;
    @SerializedName("gpa_scale")
    public String gpaScale;

    public static GradeBook create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, GradeBook.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
