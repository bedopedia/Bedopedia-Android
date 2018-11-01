package trianglz.models;

import java.io.Serializable;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class BehaviorNote  implements Serializable {
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
