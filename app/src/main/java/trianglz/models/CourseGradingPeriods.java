package trianglz.models;

/**
 * Created by ${Aly} on 11/9/2018.
 */
public class CourseGradingPeriods {
    public String endDate;
    int id;
    boolean lock;
    String name;
    boolean publish;
    public String startDate;
    public CourseGradingPeriods subGradingPeriodsAttributes;
    int weight;
    boolean isChild;

    public CourseGradingPeriods(String endDate, int id, boolean lock, String name, boolean publish,
                                String startDate, CourseGradingPeriods subGradingPeriodsAttributes, int weight, boolean isChild) {
        this.endDate = endDate;
        this.id = id;
        this.lock = lock;
        this.name = name;
        this.publish = publish;
        this.startDate = startDate;
        this.subGradingPeriodsAttributes = subGradingPeriodsAttributes;
        this.weight = weight;
        this.isChild = isChild;
    }
}
