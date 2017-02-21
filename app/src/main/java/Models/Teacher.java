package Models;

import java.util.Date;

/**
 * Created by mohamed on 2/9/17.
 */

public class Teacher extends User{
    public Teacher() {
        super();
    }

    public Teacher(int id, String firstName, String lastName, String gender, String email, String avatar, String userType) {
        super(id, firstName, lastName, gender, email, avatar, userType);
    }
}
