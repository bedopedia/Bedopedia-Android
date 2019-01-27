package trianglz.models;

/**
 * Created by ${Aly} on 11/8/2018.
 */


public class Assignment {
     public int id = 0;
     public String name  = "";
     public double total = 0;
     public double grade = 0;
     public String gradeView = "";
     public String feedBack;
     public String endDate;
     public String averageGrade = "";

    public Assignment(int id, String name, double total, double grade, String gradeView,String feedBack,String endDate) {
        this.id = id;
        this.name = name;
        this.total = total;
        this.grade = grade;
        this.gradeView = gradeView;
        this.feedBack = feedBack;
        this.endDate = endDate;
    }
}
