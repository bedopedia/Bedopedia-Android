package trianglz.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationParam {


    @SerializedName("zoomMeetingId")
    @Expose
    private String zoomMeetingId;

    private String name;


    public String getZoomMeetingId() {
        return zoomMeetingId;
    }

    public void setZoomMeetingId(String zoomMeetingId) {
        this.zoomMeetingId = zoomMeetingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}