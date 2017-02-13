package Models;

import java.util.Date;

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
    private float grade;

    public Course() {
        this.name = "";
        this.discription = "";
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.totalGrade = 0;
        this.passLimit = 0;
        this.grade = 0;
    }

    public Course(String name, String discription, float passLimit, float grade) {
        this.name = name;
        this.discription = discription;
        this.passLimit = passLimit;
        this.grade = grade;
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

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}
