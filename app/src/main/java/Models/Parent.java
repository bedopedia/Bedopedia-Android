package Models;

import java.util.ArrayList;

import myKids.Student;

/**
 * Created by mohamed on 2/9/17.
 */

public class Parent extends User{
    private ArrayList<Student> children = new ArrayList<Student>();

    public Parent() {
        super();
        this.children = new ArrayList<Student>();
    }

    public Parent(int id, String firstName, String lastName, String gender, String email, String avatar, String userType, ArrayList<Student> children) {
        super(id, firstName, lastName, gender, email, avatar, userType);
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
