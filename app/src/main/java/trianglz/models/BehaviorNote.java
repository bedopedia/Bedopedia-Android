package trianglz.models;

import java.io.Serializable;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class BehaviorNote implements Serializable {

    public String teacherName;
    public String title;
    public String message;
    public String category;
    public BehaviorNote(String teacherName, String title, String message,String category) {
        this.teacherName = teacherName;
        this.title = title;
        this.message = message;
        this.category = category;
    }
    public BehaviorNote() {
        this.teacherName = "";
        this.message = "";
    }

    public BehaviorNote(String teacherName, String message) {
        this.teacherName = teacherName;
        this.message = message;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
