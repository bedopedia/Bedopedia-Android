package trianglz.models;

import java.io.Serializable;

/**
 * This file is spawned by Gemy on 10/29/2018.
 */
public class School implements Serializable {
    public int id;
    public String name;
    public String avatarUrl;
    public String schoolUrl;
    public String code;
    public String headerUrl;

    public School(int id, String name, String code,String schoolUrl,String avatarUrl, String headerUrl ) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.schoolUrl = schoolUrl;
        this.code = code;
        this.headerUrl = headerUrl;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    public String getAvatarUrl() {
        return avatarUrl;
    }




}
