package trianglz.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class BehaviorNote implements Serializable, Parcelable {

    @SerializedName("id")
    public int id;
    @SerializedName("category")
    public String category;
    @SerializedName("note")
    public String note;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("behavior_note_category_id")
    public int behaviorNoteCategoryId;
    @SerializedName("type")
    public String type;
    @SerializedName("location")
    public String location;
    @SerializedName("consequence")
    public String consequence;
    @SerializedName("owner")
    public Owner owner;
    @SerializedName("receivers")
    public Receivers[] receivers;
    @SerializedName("student")
    public Student student;

    public BehaviorNote(String teacherName, String message) {
        this.category = teacherName;
        this.note = message;
    }
    public static BehaviorNote create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, BehaviorNote.class);
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
        dest.writeInt(this.id);
        dest.writeString(this.category);
        dest.writeString(this.note);
        dest.writeString(this.createdAt);
        dest.writeInt(this.behaviorNoteCategoryId);
        dest.writeString(this.type);
        dest.writeString(this.location);
        dest.writeString(this.consequence);
        dest.writeParcelable(this.owner, flags);
        dest.writeTypedArray(this.receivers, flags);
        dest.writeParcelable(this.student, flags);
    }

    protected BehaviorNote(Parcel in) {
        this.id = in.readInt();
        this.category = in.readString();
        this.note = in.readString();
        this.createdAt = in.readString();
        this.behaviorNoteCategoryId = in.readInt();
        this.type = in.readString();
        this.location = in.readString();
        this.consequence = in.readString();
        this.owner = in.readParcelable(Owner.class.getClassLoader());
        this.receivers = in.createTypedArray(Receivers.CREATOR);
        this.student = in.readParcelable(Student.class.getClassLoader());
    }

    public static final Parcelable.Creator<BehaviorNote> CREATOR = new Parcelable.Creator<BehaviorNote>() {
        @Override
        public BehaviorNote createFromParcel(Parcel source) {
            return new BehaviorNote(source);
        }

        @Override
        public BehaviorNote[] newArray(int size) {
            return new BehaviorNote[size];
        }
    };
}
