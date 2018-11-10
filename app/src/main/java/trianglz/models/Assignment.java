package trianglz.models;

/**
 * Created by ${Aly} on 11/8/2018.
 */


public class Assignment {
     int id = 0;
     String name  = "";
     double total = 0;
     double grade = 0;
     double gradeView = 0;
     public String feedBack;
     public String endDate;

    public Assignment(int id, String name, double total, double grade, double gradeView,String feedBack,String endDate) {
        this.id = id;
        this.name = name;
        this.total = total;
        this.grade = grade;
        this.gradeView = gradeView;
        this.feedBack = feedBack;
        this.endDate = endDate;
    }
}
