package Models;

/**
 * Created by mohamedkhaled on 2/13/17.
 */

public class Grade {
    private float grade;
    private Course course;
    private Student student;

    public Grade() {
        this.grade = 0;
        this.course = new Course();
        this.student = new Student();
    }

    public Grade(float grade, Course course, Student student) {
        this.grade = grade;
        this.course = course;
        this.student = student;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
