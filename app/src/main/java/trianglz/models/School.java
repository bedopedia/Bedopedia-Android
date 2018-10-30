package trianglz.models;

import java.io.Serializable;

/**
 * This file is spawned by Gemy on 10/29/2018.
 */
public class School implements Serializable {
    private int id;
    private String name;
    private String schoolDescription;
    private String AvatarUrl;
    private String gaTrackingId;
    private String attendanceAllowSlot;
    private String attendanceAllowFullDay;
    private String defaultConfigSlot;
    private String defaultConfigFullDay;

    public School(int id, String name, String schoolDescription, String avatarUrl, String gaTrackingId, String attendanceAllowSlot, String attendanceAllowFullDay, String defaultConfigSlot, String defaultConfigFullDay) {
        this.id = id;
        this.name = name;
        this.schoolDescription = schoolDescription;
        AvatarUrl = avatarUrl;
        this.gaTrackingId = gaTrackingId;
        this.attendanceAllowSlot = attendanceAllowSlot;
        this.attendanceAllowFullDay = attendanceAllowFullDay;
        this.defaultConfigSlot = defaultConfigSlot;
        this.defaultConfigFullDay = defaultConfigFullDay;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSchoolDescription() {
        return schoolDescription;
    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public String getGaTrackingId() {
        return gaTrackingId;
    }

    public String getAttendanceAllowSlot() {
        return attendanceAllowSlot;
    }

    public String getAttendanceAllowFullDay() {
        return attendanceAllowFullDay;
    }

    public String getDefaultConfigSlot() {
        return defaultConfigSlot;
    }

    public String getDefaultConfigFullDay() {
        return defaultConfigFullDay;
    }


}
