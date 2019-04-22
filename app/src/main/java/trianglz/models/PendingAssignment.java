package trianglz.models;
/**
 * Created by ${Aly} on 4/22/2019.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ${Aly} on 4/22/2019.
 */


public class PendingAssignment {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private Object description;
    @SerializedName("state")
    private String state;
    @SerializedName("end_at")
    private String endAt;
    @SerializedName("start_at")
    private String startAt;
    @SerializedName("lesson_id")
    private int lessonId;
    @SerializedName("file_name")
    private Object fileName;
    @SerializedName("unit_id")
    private int unitId;
    @SerializedName("student_submitted")
    private boolean studentSubmitted;
    @SerializedName("grading_period_lock")
    private boolean gradingPeriodLock;
    @SerializedName("student_submissions")
    private StudentSubmissions studentSubmissions;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getDescription() {
        return this.description;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getEndAt() {
        return this.endAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getStartAt() {
        return this.startAt;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getLessonId() {
        return this.lessonId;
    }

    public void setFileName(Object fileName) {
        this.fileName = fileName;
    }

    public Object getFileName() {
        return this.fileName;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getUnitId() {
        return this.unitId;
    }

    public void setStudentSubmitted(boolean studentSubmitted) {
        this.studentSubmitted = studentSubmitted;
    }

    public boolean getStudentSubmitted() {
        return this.studentSubmitted;
    }

    public void setGradingPeriodLock(boolean gradingPeriodLock) {
        this.gradingPeriodLock = gradingPeriodLock;
    }

    public boolean getGradingPeriodLock() {
        return this.gradingPeriodLock;
    }

    public void setStudentSubmissions(StudentSubmissions studentSubmissions) {
        this.studentSubmissions = studentSubmissions;
    }

    public StudentSubmissions getStudentSubmissions() {
        return this.studentSubmissions;
    }


    public static PendingAssignment create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, PendingAssignment.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}
