package Models;

import java.util.Date;

/**
 * Created by khaled on 3/2/17.
 */

public class TimetableEvent {
    private Date date;
    private String subject;
    private String group;
    private String classRoom;

    public TimetableEvent() {
        this.date = null;
        this.subject = "";
        this.group = "";
        this.classRoom = "";
    }

    public TimetableEvent(Date date, String subject, String group, String classRoom) {
        this.date = date;
        this.subject = subject;
        this.group = group;
        this.classRoom = classRoom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
}
