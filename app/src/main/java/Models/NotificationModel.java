package Models;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by ali on 12/02/17.
 */

public class NotificationModel {

    private String content;
    private String date;
    private String logoUrl;
    private String studentNames;
    private String type;

    public NotificationModel() {
    }
    public NotificationModel(String content, String date, String logoUrl, String studentNames, String type) throws ParseException {
        this.content = content;
        this.date = date;
        this.logoUrl = logoUrl;
        this.studentNames = studentNames;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getStudentNames() {
        return studentNames;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
