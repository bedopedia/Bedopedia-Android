package trianglz.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TodayWorkLoadStatus implements Serializable, Parcelable {

    @SerializedName("attendance_status")
    public String attendanceStatus;
    @SerializedName("assignments_count")
    public int assignmentsCount;
    @SerializedName("quizzes_count")
    public int quizzesCount;
    @SerializedName("events_count")
    public int eventsCount;
    public static TodayWorkLoadStatus create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, TodayWorkLoadStatus.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.attendanceStatus);
        dest.writeInt(this.assignmentsCount);
        dest.writeInt(this.quizzesCount);
        dest.writeInt(this.eventsCount);
    }

    public TodayWorkLoadStatus() {
    }

    protected TodayWorkLoadStatus(Parcel in) {
        this.attendanceStatus = in.readString();
        this.assignmentsCount = in.readInt();
        this.quizzesCount = in.readInt();
        this.eventsCount = in.readInt();
    }

    public static final Parcelable.Creator<TodayWorkLoadStatus> CREATOR = new Parcelable.Creator<TodayWorkLoadStatus>() {
        @Override
        public TodayWorkLoadStatus createFromParcel(Parcel source) {
            return new TodayWorkLoadStatus(source);
        }

        @Override
        public TodayWorkLoadStatus[] newArray(int size) {
            return new TodayWorkLoadStatus[size];
        }
    };
}
