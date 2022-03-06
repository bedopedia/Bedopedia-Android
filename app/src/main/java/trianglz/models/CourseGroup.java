package trianglz.models;

import java.io.Serializable;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class CourseGroup implements Serializable {
    int id;
    int courseId;
    String name;
    String courseName;
    String grade;
    String icon;
    String letter = "";
    public boolean publish = true;

    public CourseGroup() {
        this.id = 0;
        this.courseId = 0;
        this.name = "";
        this.courseName = "";
    }

    public CourseGroup(int id, int courseId, String name, String courseName) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.courseName = courseName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
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
