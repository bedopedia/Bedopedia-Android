package trianglz.models;

/**
 * Created by Farah A. Moniem on 16/09/2019.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

public class Attendances implements Serializable, Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.date);
        dest.writeString(this.comment);
        dest.writeString(this.status);
        dest.writeInt(this.timetableSlotId);
        dest.writeInt(this.studentId);
        dest.writeParcelable(this.student, flags);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.deletedAt);
    }

    public Attendances() {
    }

    protected Attendances(Parcel in) {
        this.id = in.readInt();
        this.date = in.readString();
        this.comment = in.readString();
        this.status = in.readString();
        this.timetableSlotId = in.readInt();
        this.studentId = in.readInt();
        this.student = in.readParcelable(Student.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readString();
    }

    public static final Parcelable.Creator<Attendances> CREATOR = new Parcelable.Creator<Attendances>() {
        @Override
        public Attendances createFromParcel(Parcel source) {
            return new Attendances(source);
        }

        @Override
        public Attendances[] newArray(int size) {
            return new Attendances[size];
        }
    };
}