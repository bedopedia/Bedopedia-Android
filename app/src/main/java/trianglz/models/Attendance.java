package trianglz.models;

/**
 * Created by Farah A. Moniem on 16/09/2019.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Attendance {

    @SerializedName("attendances")
    private Attendances[] attendances;
    @SerializedName("students")
    private AttendanceStudent[] students;

    public void setAttendances(Attendances[] attendances) {
        this.attendances = attendances;
    }

    public Attendances[] getAttendances() {
        return this.attendances;
    }

    public void setStudents(AttendanceStudent[] students) {
        this.students = students;
    }

    public AttendanceStudent[] getStudents() {
        return this.students;
    }


    public static RootClass create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, RootClass.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}