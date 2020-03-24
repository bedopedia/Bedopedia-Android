package trianglz.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class TimeTableSlot implements Comparable<TimeTableSlot>, Serializable {
    private Date from;
    private Date to;
    private String day;
    private String sectionName;
    private String coureGroupName;
    private String courseName;
    private String classRoom;

    public TimeTableSlot() {
        this.from = null;
        this.to = null;
        this.day = "";
        this.courseName = "";
        this.classRoom = "";
        this.coureGroupName = "";
        this.sectionName = "";
    }

    public TimeTableSlot(Date from, Date to, String day, String courseName, String classRoom, String coureGroupName, String sectionName) {
        this.from = from;
        this.to = to;
        this.day = day;
        this.courseName = courseName;
        this.classRoom = classRoom;
        this.coureGroupName = coureGroupName;
        this.sectionName = sectionName;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getCoureGroupName() {
        return coureGroupName;
    }

    public void setCoureGroupName(String coureGroupName) {
        this.coureGroupName = coureGroupName;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
    @Override
    public int compareTo(@NonNull TimeTableSlot o) {
        if(this.getFrom().getHours() > o.getFrom().getHours())
            return 1;
        else if (this.getFrom().getHours() < o.getFrom().getHours())
            return -1;
        return 0;
    }
}
