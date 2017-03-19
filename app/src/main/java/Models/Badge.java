package Models;

/**
 * Created by khaled on 3/14/17.
 */

public class Badge {
    private String name;
    private String icon;
    private String reason;
    private String courseName;

    public Badge() {
        this.name = "";
        this.icon = "";
        this.reason = "";
        this.courseName = "";
    }

    public Badge(String name, String icon, String reason, String courseName) {
        this.name = name;
        this.icon = icon;
        this.reason = reason;
        this.courseName = courseName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
