package trianglz.models;//
//  RootClass.java
//
//  Generated using https://jsonmaster.github.io
//  Created on August 05, 2019
//

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PostAssignmentGradeModel {

    @SerializedName("grade")
    private double grade;
    @SerializedName("student_id")
    private int studentId;
    @SerializedName("assignment_id")
    private int assignmentId;
    @SerializedName("course_group_id")
    private int courseGroupId;
    @SerializedName("student_status")
    private String studentStatus;
    @SerializedName("course_id")
    private int courseId;

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getGrade() {
        return this.grade;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getAssignmentId() {
        return this.assignmentId;
    }

    public void setCourseGroupId(int courseGroupId) {
        this.courseGroupId = courseGroupId;
    }

    public int getCourseGroupId() {
        return this.courseGroupId;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getStudentStatus() {
        return this.studentStatus;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return this.courseId;
    }


    public static PostAssignmentGradeModel create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, PostAssignmentGradeModel.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}