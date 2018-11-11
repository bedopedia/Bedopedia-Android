package trianglz.models;

import java.io.Serializable;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class Stage implements Serializable {
    public String createdAt;
    public Object deletedAt;
    public int id;
    public boolean isDeleted;
    public boolean isPreSchool;
    public String name;
    public int sectionId;
    public String updatedAt;

    public Stage(String createdAt, Object deletedAt, int id, boolean isDeleted, boolean isPreSchool, String name, int sectionId, String updatedAt) {
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.id = id;
        this.isDeleted = isDeleted;
        this.isPreSchool = isPreSchool;
        this.name = name;
        this.sectionId = sectionId;
        this.updatedAt = updatedAt;
    }
}
