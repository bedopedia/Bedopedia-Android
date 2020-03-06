package trianglz.models;

/**
 * Created by Farah A. Moniem on 16/09/2019.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

public class Attendances implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("date")
    private String date;
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
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("deleted_at")
    public String deletedAt;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
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

    public int getTimetableSlotId() {
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
    public static JSONArray getJsonArray(ArrayList<Attendances> arrayList) {
        JSONArray jsonArray = new JSONArray();
        for (Attendances object : arrayList) {
            jsonArray.put(object.toString());
        }
        return jsonArray;
    }

    public static ArrayList<Attendances> getArrayList(String string) {
        ArrayList<Attendances> arrayList = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(Attendances.create(jsonArray.opt(i).toString()));
            }
        }
        return arrayList;
    }
}