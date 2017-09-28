package myKids;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import gradeBook.Course;
import Models.Parent;
import Models.User;

/**
 * Created by mohamed on 2/9/17.
 */

public class Student extends User {
    private String level;
    private String section;
    private String stage;
    private String todayAttendance;
    private int todayAssignmentsCount;
    private int todayQuizzesCount;
    private int todayEventsCount;
    private int bedoPoints;
    private Parent parent;
    private ArrayList<Course> courses;

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
        this.parent = new Parent();
        courses = new ArrayList<Course>();
    }

    public Student(int id, String firstName, String lastName, String gender, String email, String avatar, String userType, String level, String section, String stage, JsonObject todayWorkLoad, int bedoPoints, Parent parent, ArrayList<Course> courses) {
        super(id, firstName, lastName, gender, email, avatar, userType);
        this.level = level;
        this.section = section;
        this.stage = stage;
        this.bedoPoints = bedoPoints;
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

    public void addCourse(Course course){
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

    public void setTodayWorkLoad (JsonObject todayWorkLoad) {
        this.todayAttendance = todayWorkLoad.get("attendance_status").getAsString();
        this.todayAssignmentsCount = todayWorkLoad.get("assignments_count").getAsInt();
        this.todayQuizzesCount = todayWorkLoad.get("quizzes_count").getAsInt();
        this.todayEventsCount = todayWorkLoad.get("events_count").getAsInt();
    }


}
