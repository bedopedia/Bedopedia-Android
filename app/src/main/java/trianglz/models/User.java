package trianglz.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class User  implements Serializable {
    @SerializedName("id")
    public int id;

    @SerializedName("firstname")
    public String firstName;

    @SerializedName("lastname")
    public String lastName;

    @SerializedName("gender")
    public String gender;

    @SerializedName("user_type")
    public String userType;

    @SerializedName("avatar_url")
    public String avatar;

    @SerializedName("email")
    public String email;
    @SerializedName("phone")
    public String phone;

    public Date createdAt;
    public Date updatedAt;
    public Date dateOfBirth;
    public String mobile;
    public String middleName;


    public User() {
        this.id = 0;
        this.firstName = "";
        this.middleName = "";
        this.lastName = "";
        this.dateOfBirth = new Date();
        this.gender = "";
        this.email = "";
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.avatar = "";
        this.userType = "";
        this.phone = "";
        this.mobile = "";
    }

    public User(int id, String firstName, String lastName, String gender, String email, String avatar, String userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.avatar = avatar;
        this.userType = userType;
    }
}