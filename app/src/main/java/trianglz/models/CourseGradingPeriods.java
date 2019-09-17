package trianglz.models;

import java.util.ArrayList;

/**
 * Created by ${Aly} on 11/9/2018.
 */
public class CourseGradingPeriods {
    public String endDate;
    public int id;
    public boolean lock;
    public String name;
    public boolean publish;
    public String startDate;
    public ArrayList<CourseGradingPeriods> subGradingPeriodsAttributes;
    public boolean isChild;
    public boolean isParent;

    public String parentStartDate = "";
    public  String parentEndDate = "";

    public CourseGradingPeriods(String endDate, int id, boolean lock, String name, boolean publish,
                                String startDate, ArrayList<CourseGradingPeriods> subGradingPeriodsAttributes
                                , boolean isChild, boolean isParent) {
        this.endDate = endDate;
        this.id = id;
        this.lock = lock;
        this.name = name;
        this.publish = publish;
        this.startDate = startDate;
        this.subGradingPeriodsAttributes = subGradingPeriodsAttributes;
        this.isChild = isChild;
        this.isParent = isParent;
    }
}
