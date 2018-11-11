package trianglz.models;

/**
 * Created by ${Aly} on 11/8/2018.
 */
public class GradeItem {
    public int id;
    public String name;
    public int maxGrade;
    public double total;
    public double grade;
    public double gradeView;
    public String feedBack;
    public String endDate;

    public GradeItem(int id, String name, int maxGrade, double total, double grade, double gradeView, String feedBack, String endDate) {
        this.id = id;
        this.name = name;
        this.maxGrade = maxGrade;
        this.total = total;
        this.grade = grade;
        this.gradeView = gradeView;
        this.feedBack = feedBack;
        this.endDate = endDate;
    }
}
