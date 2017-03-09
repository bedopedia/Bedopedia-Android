package Models;

import java.util.Date;

/**
 * Created by khaled on 3/2/17.
 */

public class TimetableSlot {
    private Date from;
    private Date to;
    private String day;
    private String courseName;
    private String classRoom;

    public TimetableSlot() {
        this.from = null;
        this.to = null;
        this.day = "";
        this.courseName = "";
        this.classRoom = "";
    }

    public TimetableSlot(Date from, Date to, String day, String courseName, String classRoom) {
        this.from = from;
        this.to = to;
        this.day = day;
        this.courseName = courseName;
        this.classRoom = classRoom;
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

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
}
