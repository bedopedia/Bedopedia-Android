package Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mohamed on 2/9/17.
 */

public class Parent extends Student{
    private ArrayList<Student> children = new ArrayList<Student>();

    public Parent() {
        super();
        this.children = new ArrayList<Student>();
    }

    public Parent(String firstName, String middleName, String lastName, Date dateOfBirth, String gender, String email, Date createdAt, Date updatedAt, String avatar, String userType, String phone, String mobile, String level, String section, String stage, int bedoPoints, Parent parent, ArrayList<Student> children) {
        super(firstName, middleName, lastName, dateOfBirth, gender, email, createdAt, updatedAt, avatar, userType, phone, mobile, level, section, stage, bedoPoints, parent);
        this.children = children;
    }

    public ArrayList<Student> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Student> children) {
        this.children = children;
    }

    public void addChild(Student child){
        children.add(child);
    }
}
