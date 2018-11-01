package trianglz.models;

import java.io.Serializable;

/**
 * This file is spawned by Gemy on 10/29/2018.
 */
public class School implements Serializable {
    public int id;
    public String name;
    public String schoolDescription;
    public String avatarUrl;
    public String gaTrackingId;
    public String attendanceAllowSlot;
    public String attendanceAllowFullDay;
    public String defaultConfigSlot;
    public String defaultConfigFullDay;
    public String schoolUrl;

    public School(int id, String name, String schoolDescription, String avatarUrl, String gaTrackingId, String attendanceAllowSlot, String attendanceAllowFullDay, String defaultConfigSlot, String defaultConfigFullDay,String schoolUrl) {
        this.id = id;
        this.name = name;
        this.schoolDescription = schoolDescription;
        this.avatarUrl = avatarUrl;
        this.gaTrackingId = gaTrackingId;
        this.attendanceAllowSlot = attendanceAllowSlot;
        this.attendanceAllowFullDay = attendanceAllowFullDay;
        this.defaultConfigSlot = defaultConfigSlot;
        this.defaultConfigFullDay = defaultConfigFullDay;
        this.schoolUrl = schoolUrl;

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
        return avatarUrl;
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