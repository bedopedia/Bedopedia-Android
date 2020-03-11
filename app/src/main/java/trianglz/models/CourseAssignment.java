package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${Aly} on 6/20/2019.
 */
public class CourseAssignment implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("course_name")
    private String courseName;
    @SerializedName("course_id")
    private int courseId;
    @SerializedName("assignments_count")
    private int assignmentsCount;
    @SerializedName("next_assignment_date")
    private String nextAssignmentDate;
    @SerializedName("assignment_name")
    private String assignmentName;
    @SerializedName("assignment_state")
    private String assignmentState;
    @SerializedName("next_assignment_start_date")
    private String nextAssignmentStartDate;

    public int getActiveAssignmentCount() {
        return activeAssignmentCount;
    }

    public void setActiveAssignmentCount(int activeAssignmentCount) {
        this.activeAssignmentCount = activeAssignmentCount;
    }

    @SerializedName("running_assignments_count")
    private int activeAssignmentCount;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setAssignmentsCount(int assignmentsCount) {
        this.assignmentsCount = assignmentsCount;
    }

    public int getAssignmentsCount() {
        return this.assignmentsCount;
    }

    public void setNextAssignmentDate(String nextAssignmentDate) {
        this.nextAssignmentDate = nextAssignmentDate;
    }

    public String getNextAssignmentDate() {
        return this.nextAssignmentDate;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentName() {
        return this.assignmentName;
    }

    public void setAssignmentState(String assignmentState) {
        this.assignmentState = assignmentState;
    }

    public String getAssignmentState() {
        return this.assignmentState;
    }

    public void setNextAssignmentStartDate(String nextAssignmentStartDate) {
        this.nextAssignmentStartDate = nextAssignmentStartDate;
    }

    public String getNextAssignmentStartDate() {
        return this.nextAssignmentStartDate;
    }


    public static CourseAssignment create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, CourseAssignment.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}
