package trianglz.models;

/**
 * Created by ${Aly} on 11/8/2018.
 */
public class Quiz {

    public  int id;
    public  String name;
    public double totalScore;
    public  double total;
    public  double grade;
    public  double gradeView;
    public String feedBack;
    public  String endDate;
    public double averageGrade = 0;

    public Quiz(int id, String name, double totalScore, double total, double grade, double gradeView, String feedBack, String endDate) {
        this.id = id;
        this.name = name;
        this.totalScore = totalScore;
        this.total = total;
        this.grade = grade;
        this.gradeView = gradeView;
        this.feedBack = feedBack;
        this.endDate = endDate;
    }
}