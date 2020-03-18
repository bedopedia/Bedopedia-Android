package trianglz.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Receivers implements Parcelable {
    @SerializedName("id")
    public int id;
    @SerializedName("behavior_note_id")
    public int behaviorNoteId;
    @SerializedName("user_type")
    public String userType;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("deleted_at")
    public String deletedAt;
    public static Receivers create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Receivers.class);
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
        dest.writeInt(this.behaviorNoteId);
        dest.writeString(this.userType);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.deletedAt);
    }

    public Receivers() {
    }

    protected Receivers(Parcel in) {
        this.id = in.readInt();
        this.behaviorNoteId = in.readInt();
        this.userType = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readString();
    }

    public static final Parcelable.Creator<Receivers> CREATOR = new Parcelable.Creator<Receivers>() {
        @Override
        public Receivers createFromParcel(Parcel source) {
            return new Receivers(source);
        }

        @Override
        public Receivers[] newArray(int size) {
            return new Receivers[size];
        }
    };
}
