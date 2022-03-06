package trianglz.models;//
//  WeeklyPlannerResponse.java
//
//  Generated using https://jsonmaster.github.io
//  Created on August 05, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class GradeModel {

    @SerializedName("grade")
    private Double grade;
    @SerializedName("student_id")
    private Integer studentId;
    @SerializedName("assignment_id")
    private Integer assignmentId;
    @SerializedName("course_group_id")
    private Integer courseGroupId;
    @SerializedName("student_status")
    private String studentStatus;
    @SerializedName("course_id")
    private Integer courseId;

    // quizzes fields
    @SerializedName("score")
    private Double score;
    @SerializedName("quiz_id")
    private Integer quizId;

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Double getGrade() {
        return this.grade;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getStudentId() {
        return this.studentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Integer getAssignmentId() {
        return this.assignmentId;
    }

    public void setCourseGroupId(Integer courseGroupId) {
        this.courseGroupId = courseGroupId;
    }

    public Integer getCourseGroupId() {
        return this.courseGroupId;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getStudentStatus() {
        return this.studentStatus;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCourseId() {
        return this.courseId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public static GradeModel create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, GradeModel.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}