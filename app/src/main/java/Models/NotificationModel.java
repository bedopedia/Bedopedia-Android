package Models;

/**
 * Created by ali on 12/02/17.
 */

public class NotificationModel {

    private String content;
    private String Date;
    private String logoUrl;


    public NotificationModel(String content, String date, String logoUrl) {
        this.content = content;
        Date = date;
        this.logoUrl = logoUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
