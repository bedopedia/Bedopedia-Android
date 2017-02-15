package Models;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mohamed on 2/9/17.
 */

public class Student extends User{
    private String level;
    private String section;
    private String stage;
    private int bedoPoints;
    private Parent parent;
    private ArrayList<Course> courses;

    public Student() {
        super();
        this.level = "";
        this.section = "";
        this.stage = "";
        this.bedoPoints = 0;
        this.parent = new Parent();
        courses = new ArrayList<Course>();
    }

    public Student(String firstName, String lastName, String gender, String email, String avatar, String userType, String level, String section, String stage, int bedoPoints, Parent parent, ArrayList<Course> courses) {
        super(firstName, lastName, gender, email, avatar, userType);
        this.level = level;
        this.section = section;
        this.stage = stage;
        this.bedoPoints = bedoPoints;
        this.parent = parent;
        this.courses = courses;
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

    public String getAverageGrade(){
        Double temp = 0.0, sum = 0.0;
        for (int i = 0; i< courses.size() ; i++) {
            temp += courses.get(i).getGrade();
            sum += courses.get(i).getTotalGrade();
        }
        temp = sum / temp;
        //TODO Average Grading
        return temp.toString();
    }

    public String getNext () {

        String next = "Math K1, Sunday 9:00 AM";
        return next;

    }

    public String getAttendanceData() {
        String data = "16 / 25 Days";
        return data;
    }

    public String getNumOfGoodBN() {
        String good = "3";
        return good;
    }

    public String getNumOfBadBN() {
        String bad = "3";
        return bad;
    }
}
