package trianglz.models;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import Models.Parent;
import Models.User;
import gradeBook.Course;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class Student extends trianglz.models.User{

        public String level;
        public String section;
        public String stage;
        public String todayAttendance;
        public int todayAssignmentsCount;
        public int todayQuizzesCount;
        public int todayEventsCount;
        public int bedoPoints;
        public Parent parent;
        public ArrayList<Course> courses;

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

        public Student(int id, String firstName, String lastName, String gender, String email, String avatar, String userType, String level, String section, String stage, JSONObject todayWorkLoad, int bedoPoints, Parent parent, ArrayList<Course> courses) {
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

        public void setTodayWorkLoad (JSONObject todayWorkLoad) {
            this.todayAttendance = todayWorkLoad.optString("attendance_status");
            this.todayAssignmentsCount = todayWorkLoad.optInt("assignments_count");
            this.todayQuizzesCount = todayWorkLoad.optInt("quizzes_count");
            this.todayEventsCount = todayWorkLoad.optInt("events_count");
        }


    }


