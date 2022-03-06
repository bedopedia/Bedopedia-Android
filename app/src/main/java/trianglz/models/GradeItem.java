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
    public String gradeView;
    public String feedBack;
    public String endDate;
    public String averageGrade = "";
    public int gradingPeriodId;
    public boolean hideGrade = false;

    public GradeItem(int id, String name, int maxGrade, double total, double grade,
                     String gradeView, String feedBack, String endDate,
                     int gradingPeriodId,boolean hideGrade) {
        this.id = id;
        this.name = name;
        this.maxGrade = maxGrade;
        this.total = total;
        this.grade = grade;
        this.gradeView = gradeView;
        this.feedBack = feedBack;
        this.endDate = endDate;
        this.gradingPeriodId = gradingPeriodId;
        this.hideGrade = hideGrade;
    }
}
