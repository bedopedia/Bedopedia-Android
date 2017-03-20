package Models;

/**
 * Created by ali on 20/03/17.
 */

public class AskTeacherMessage {

    private String date;
    private String lastMessage;
    private String title;
    private Integer notSeenCnt;
    private String imagePath;

    public AskTeacherMessage() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNotSeenCnt() {
        return notSeenCnt;
    }

    public void setNotSeenCnt(Integer notSeenCnt) {
        this.notSeenCnt = notSeenCnt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }



}
