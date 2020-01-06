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
    public  String gradeView;
    public String feedBack;
    public  String endDate;
    public String averageGrade = "";
    public int hideGrade ;

    public Quiz(int id, String name, double totalScore, double total, double grade, String gradeView, String feedBack, String endDate,int hideGrade) {
        this.id = id;
        this.name = name;
        this.totalScore = totalScore;
        this.total = total;
        this.grade = grade;
        this.gradeView = gradeView;
        this.feedBack = feedBack;
        this.endDate = endDate;
        this.hideGrade = hideGrade;
    }
}
