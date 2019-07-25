package trianglz.models;

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StudentSubmissions {

    @SerializedName("id")
    private int id;
    @SerializedName("score")
    private double score;
    @SerializedName("student_id")
    private int studentId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("quiz_id")
    private int quizId;
    @SerializedName("course_group_id")
    private int courseGroupId;
    @SerializedName("feedback")
    private String feedback;
    @SerializedName("is_submitted")
    private boolean isSubmitted;
    @SerializedName("deleted_at")
    private String deletedAt;
    @SerializedName("student_status")
    private String studentStatus;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return this.score;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getQuizId() {
        return this.quizId;
    }

    public void setCourseGroupId(int courseGroupId) {
        this.courseGroupId = courseGroupId;
    }

    public int getCourseGroupId() {
        return this.courseGroupId;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public void setIsSubmitted(boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public boolean getIsSubmitted() {
        return this.isSubmitted;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedAt() {
        return this.deletedAt;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getStudentStatus() {
        return this.studentStatus;
    }


    public static StudentSubmissions create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, StudentSubmissions.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}