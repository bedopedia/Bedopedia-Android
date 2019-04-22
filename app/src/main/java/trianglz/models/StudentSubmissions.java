package trianglz.models;

/**
 * Created by ${Aly} on 4/22/2019.
 */
import java.io.File;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StudentSubmissions {

    @SerializedName("id")
    private int id;
    @SerializedName("grade")
    private int grade;
    @SerializedName("graded")
    private boolean graded;
    @SerializedName("assignment_id")
    private int assignmentId;
    @SerializedName("student_id")
    private int studentId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("answers")
    private Object answers;
    @SerializedName("file")
    private File file;
    @SerializedName("feedback")
    private Object feedback;
    @SerializedName("course_group_id")
    private int courseGroupId;
    @SerializedName("downloads_number")
    private int downloadsNumber;
    @SerializedName("deleted_at")
    private Object deletedAt;
    @SerializedName("student_status")
    private String studentStatus;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    public boolean getGraded() {
        return this.graded;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getAssignmentId() {
        return this.assignmentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setAnswers(Object answers) {
        this.answers = answers;
    }

    public Object getAnswers() {
        return this.answers;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void setFeedback(Object feedback) {
        this.feedback = feedback;
    }

    public Object getFeedback() {
        return this.feedback;
    }

    public void setCourseGroupId(int courseGroupId) {
        this.courseGroupId = courseGroupId;
    }

    public int getCourseGroupId() {
        return this.courseGroupId;
    }

    public void setDownloadsNumber(int downloadsNumber) {
        this.downloadsNumber = downloadsNumber;
    }

    public int getDownloadsNumber() {
        return this.downloadsNumber;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Object getDeletedAt() {
        return this.deletedAt;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getStudentStatus() {
        return this.studentStatus;
    }


    public static StudentSubmissions create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, StudentSubmissions.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}
