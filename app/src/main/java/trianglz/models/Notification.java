package trianglz.models;

/**
 * Created by ali on 12/02/17.
 * Updated by Gemy on 1/11/2018
 */

public class Notification {

    private String message;
    private String date;
    private String logo;
    private String studentNames;
    private String type;

    public Notification(String message, String date, String logo, String studentNames, String type) {
        this.message = message;
        this.date = date;
        this.logo = logo;
        this.studentNames = studentNames;
        this.type = type;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(String studentNames) {
        this.studentNames = studentNames;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
