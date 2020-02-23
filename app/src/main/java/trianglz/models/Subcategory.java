package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Subcategory {
    @SerializedName("id")
    public int id;
    @SerializedName("weight")
    public int weight;
    @SerializedName("name")
    public String name;
    @SerializedName("parent_id")
    public int parentId;
    @SerializedName("total")
    public double total;
    @SerializedName("grade")
    public String grade;
    @SerializedName("quizzes_total")
    public double quizzesTotal;
    @SerializedName("quizzes_grade")
    public int quizzesGrade;
    @SerializedName("quizzes")
    public ArrayList<Quizzes> quizzes;
    @SerializedName("assignments_total")
    public double assignmentsTotal;
    @SerializedName("assignments_grade")
    public int assignmentsGrade;
    @SerializedName("assignments")
    public ArrayList<Assignments> assignments;
    @SerializedName("grade_items_total")
    public double gradeItemsTotal;
    @SerializedName("grade_items_grade")
    public int gradeItemsGrade;
    @SerializedName("grade_items")
    public ArrayList<GradeItems> gradeItems;
    @SerializedName("percentage")
    public double percentage;
    @SerializedName("grade_view")
    public String gradeView;

    public static Subcategory create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Subcategory.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }


}
