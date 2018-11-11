package trianglz.models;

import java.util.ArrayList;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class Subject {
    public int completedLessonCount;
    public Course course;
    public int courseID;
    public String courseName;
    public String iconName;
    public int id;
    public String lastCompletedLessonName;
    public int lessCount;
    public String name;
    public Stage stage;
    public ArrayList<Teacher> teacherArrayList;

    public Subject(int completedLessonCount, Course course, int courseID, String courseName,
                   String iconName, int id, String lastCompletedLessonName,
                   int lessCount, String name, Stage stage, ArrayList<Teacher> teacherArrayList) {
        this.completedLessonCount = completedLessonCount;
        this.course = course;
        this.courseID = courseID;
        this.courseName = courseName;
        this.iconName = iconName;
        this.id = id;
        this.lastCompletedLessonName = lastCompletedLessonName;
        this.lessCount = lessCount;
        this.name = name;
        this.stage = stage;
        this.teacherArrayList = teacherArrayList;
    }
}

