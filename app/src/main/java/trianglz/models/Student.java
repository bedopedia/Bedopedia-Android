package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

import Models.Parent;
import gradeBook.Course;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class Student extends trianglz.models.User {
    @SerializedName("level")
    public String level;
    @SerializedName("section")
    public String section;
    @SerializedName("stage")
    public String stage;
    @SerializedName("today_attendance")
    public String todayAttendance;
    @SerializedName("today_assignments_count")
    public int todayAssignmentsCount;
    @SerializedName("today_quizzes_count")
    public int todayQuizzesCount;
    @SerializedName("today_events_count")
    public int todayEventsCount;
    @SerializedName("bedo_points")
    public int bedoPoints;
    @SerializedName("parent")
    public Parent parent;
    public int userId;
    @SerializedName("courses")
    public ArrayList<Course> courses;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private boolean expanded;

    public Student() {
        super();
        this.level = "";
        this.section = "";
        this.stage = "";
        this.todayAttendance = "";
        this.todayAssignmentsCount = 0;
        this.todayQuizzesCount = 0;
        this.todayEventsCount = 0;
        this.bedoPoints = 0;
        this.userId = 0;
        this.parent = new Parent();
        courses = new ArrayList<Course>();
    }

    public Student(int id, String firstName, String lastName, String gender, String email, String avatar, String userType, String level, String section, String stage, JSONObject todayWorkLoad, int bedoPoints, int userId, Parent parent, ArrayList<Course> courses) {
        super(id, firstName, lastName, gender, email, avatar, userType);
        this.level = level;
        this.section = section;
        this.stage = stage;
        this.bedoPoints = bedoPoints;
        this.userId = userId;
        this.parent = parent;
        this.courses = courses;
        this.setTodayWorkLoad(todayWorkLoad);

    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getBedoPoints() {
        return bedoPoints;
    }

    public void setBedoPoints(int bedoPoints) {
        this.bedoPoints = bedoPoints;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public int getTodayAssignmentsCount() {
        return this.todayAssignmentsCount;
    }

    public int getTodayEventsCount() {
        return this.todayEventsCount;
    }

    public int getTodayQuizzesCount() {
        return this.todayQuizzesCount;
    }

    public String getTodayAttendance() {
        return this.todayAttendance;
    }

    public void setTodayWorkLoad(JSONObject todayWorkLoad) {
        this.todayAttendance = todayWorkLoad.optString("attendance_status");
        this.todayAssignmentsCount = todayWorkLoad.optInt("assignments_count");
        this.todayQuizzesCount = todayWorkLoad.optInt("quizzes_count");
        this.todayEventsCount = todayWorkLoad.optInt("events_count");
    }

    public static Student create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Student.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}


