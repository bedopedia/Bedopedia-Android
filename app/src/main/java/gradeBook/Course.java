package gradeBook;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by mohamed on 2/9/17.
 */

public class Course {
    private String name;
    private String discription;
    private Date createdAt;
    private Date updatedAt;
    private float totalGrade;
    private float passLimit;
    private ArrayList<Pair<String, String>> classWorks;
    private ArrayList<Pair<String, String>> assignments;
    private String grade;
    private String icon;

    public Course() {
        this.name = "";
        this.discription = "";
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.totalGrade = 0;
        this.passLimit = 0;
        this.grade = "";
        this.icon = "";
    }

    public Course(String name, String discription, float passLimit, String grade, String icon) {
        this.name = name;
        this.discription = discription;
        this.passLimit = passLimit;
        this.grade = grade;
        this.icon = icon;
        updateIconName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public float getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(float totalGrade) {
        this.totalGrade = totalGrade;
    }

    public float getPassLimit() {
        return passLimit;
    }

    public void setPassLimit(float passLimit) {
        this.passLimit = passLimit;
    }

    public void setClassWorks(ArrayList<Pair<String, String>> classWorks ) {
        this.classWorks = classWorks;
    }

    public void setAssignments(ArrayList<Pair<String, String>> assignments ) {
        this.assignments = assignments;
    }

    public ArrayList<Pair<String, String>> getClassWorks() {
        return classWorks;
    }

    public ArrayList<Pair<String, String>> getAssignments() {
        return assignments;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        updateIconName();
    }

    public void updateIconName() {
        icon = icon.replace('-' , '_');
    }
}

