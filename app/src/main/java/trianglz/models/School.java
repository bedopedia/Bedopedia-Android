package trianglz.models;

import java.io.Serializable;

/**
 * This file is spawned by Gemy on 10/29/2018.
 */
public class School implements Serializable {
    int id;
    String name;
    String schoolDescription;
    String AvatarUrl;
    String gaTrackingId;
    String attendanceAllowSlot;
    String attendanceAllowFullDay;
    String defaultConfigSlot;
    String defaultConfigFullDay;

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

}
