package Models;

/**
 * Created by khaled on 2/27/17.
 */

public class BehaviourNote {
    String category;
    String text;
    String teacherName;

    public BehaviourNote() {
        this.category = "";
        this.text = "";
        this.teacherName = "";
    }

    public BehaviourNote(String category, String text, String teacherName) {
        this.category = category;
        this.text = text;
        this.teacherName = teacherName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
