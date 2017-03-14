package Models;

/**
 * Created by khaled on 2/27/17.
 */

public class BehaviorNote {
    String category;
    String text;

    public BehaviorNote() {
        this.category = "";
        this.text = "";
    }

    public BehaviorNote(String category, String text) {
        this.category = category;
        this.text = text;
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

}
