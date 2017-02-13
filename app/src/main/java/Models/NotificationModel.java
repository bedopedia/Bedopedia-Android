package Models;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by ali on 12/02/17.
 */

public class NotificationModel {

    private String content;
    private Date date;
    private String logoUrl;

    public NotificationModel() {
    }
    public NotificationModel(String content, Date date, String logoUrl) throws ParseException {
        this.content = content;
        this.date = date;
        this.logoUrl = logoUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
