package trianglz.models;

import java.io.Serializable;

public class Actor implements Serializable {
    public String firstName;
    public String lastName;
    public String actableType;
    public String imageUrl;

    public Actor(String firstName, String lastName, String actableType, String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.actableType = actableType;
        this.imageUrl = imageUrl;
    }
}
