package trianglz.models;

/**
 * Created by ${Aly} on 11/13/2018.
 */
public class GradeHeader {
    public String header;
    public HeaderType headerType;
    public String gradeText;
    public Double sumOfStudentMarks;
    public Double totalSummtion;
    public boolean publish;

    public enum HeaderType {
        CATEGORY, SUBCATEGORY, GRADE, SUBCATEGORY_TOTAL,  CATEGORY_TOTAL, TOTAL
    }
}
