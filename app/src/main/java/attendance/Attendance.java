package attendance;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by khaled on 2/27/17.
 */

public class Attendance {
    private Date date;
    private String comment;

    public Attendance() {
        this.comment = "";
        this.date = null;
    }

    public Attendance(Date date, String comment) {
        this.date = date;
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
