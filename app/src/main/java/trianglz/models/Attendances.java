package trianglz.models;

/**
 * Created by Farah A. Moniem on 16/09/2019.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Attendances {

    @SerializedName("id")
    private int id;
    @SerializedName("date")
    private int date;
    @SerializedName("comment")
    private String comment;
    @SerializedName("status")
    private String status;
    @SerializedName("timetable_slot_id")
    private int timetableSlotId;
    @SerializedName("student_id")
    private int studentId;
    @SerializedName("student")
    private Student student;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDate() {
        return this.date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Object getComment() {
        return this.comment;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setTimetableSlotId(int timetableSlotId) {
        this.timetableSlotId = timetableSlotId;
    }

    public Object getTimetableSlotId() {
        return this.timetableSlotId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return this.student;
    }


    public static Attendances create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Attendances.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}