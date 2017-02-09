package Models;

import java.util.Date;

/**
 * Created by mohamed on 2/9/17.
 */

public class Teacher extends User{
    public Teacher() {
        super();
    }

    public Teacher(String firstName, String middleName, String lastName, Date dateOfBirth, String gender, String email, Date createdAt, Date updatedAt, String avatar, String userType, String phone, String mobile) {
        super(firstName, middleName, lastName, dateOfBirth, gender, email, createdAt, updatedAt, avatar, userType, phone, mobile);
    }
}
