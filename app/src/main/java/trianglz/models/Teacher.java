package trianglz.models;

import java.io.Serializable;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class Teacher implements Serializable {
    public int actableId;
    public String avatarUrl;
    public String firstName;
    public String gender;
    public int id;
    public String lastName;
    public String name;
    public String nameWithTitle;
    public String thumbUrl;
    public String userType;

    public Teacher(int actableId, String avatarUrl, String firstName, String gender,
                   int id, String lastName, String name, String nameWithTitle,
                   String thumbUrl, String userType) {
        this.actableId = actableId;
        this.avatarUrl = avatarUrl;
        this.firstName = firstName;
        this.gender = gender;
        this.id = id;
        this.lastName = lastName;
        this.name = name;
        this.nameWithTitle = nameWithTitle;
        this.thumbUrl = thumbUrl;
        this.userType = userType;
    }
}
