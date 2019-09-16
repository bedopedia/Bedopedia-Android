package trianglz.models;

/**
 * Created by Farah A. Moniem on 16/09/2019.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Attendance {

    @SerializedName("attendances")
    private ArrayList<Attendances> attendances;
    @SerializedName("students")
    private ArrayList<AttendanceStudent> students;

    public void setAttendances(ArrayList<Attendances> attendances) {
        this.attendances = attendances;
    }

    public ArrayList<Attendances> getAttendances() {
        return this.attendances;
    }

    public void setStudents(ArrayList<AttendanceStudent> students) {
        this.students = students;
    }

    public ArrayList<AttendanceStudent> getStudents() {
        return this.students;
    }


    public static Attendance create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Attendance.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}