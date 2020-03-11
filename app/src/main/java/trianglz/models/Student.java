package trianglz.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import Models.Parent;
import gradeBook.Course;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class Student extends trianglz.models.User implements Parcelable {
    @SerializedName("level_name")
    public String level;
    @SerializedName("section")
    public String section;
    @SerializedName("stage")
    public String stage;
    @SerializedName("today_attendance")
    public String todayAttendance;
    @SerializedName("today_assignments_count")
    public int todayAssignmentsCount;
    @SerializedName("today_quizzes_count")
    public int todayQuizzesCount;
    @SerializedName("today_events_count")
    public int todayEventsCount;
    @SerializedName("bedo_points")
    public int bedoPoints;
    @SerializedName("parent")
    public Parent parent;
    public int userId;
    @SerializedName("courses")
    public ArrayList<Course> courses;
    @SerializedName("username")
    public String username;
    @SerializedName("thumb_url")
    public String thumbUrl;
    @SerializedName("dateofbirth")
    public String dateofbirth;
    @SerializedName("country")
    public String country;
    @SerializedName("city")
    public String city;
    @SerializedName("home_address")
    public String homeAddress;
    @SerializedName("password")
    public String password;
    @SerializedName("is_active")
    public boolean isActive;
    @SerializedName("last_sign_in_at")
    public String lastSignInAt;
    @SerializedName("name")
    public String name;
    @SerializedName("child_id")
    public int childId;
    @SerializedName("middlename")
    public String middlename;
    @SerializedName("secondary_phone")
    public String secondaryPhone;
    @SerializedName("secondary_address")
    public String secondaryAddress;
    @SerializedName("locale")
    public String locale;
    @SerializedName("actable_id")
    public int actableId;
    @SerializedName("actable_type")
    public String actableType;
    @SerializedName("unseen_notifications")
    public int unseenNotifications;
    @SerializedName("school_name")
    public String schoolName;
    @SerializedName("realtime_ip")
    public String realtimeIp;
    @SerializedName("password_changed")
    public boolean passwordChanged;
    @SerializedName("parent_id")
    public int parentId;
    @SerializedName("section_name")
    public String sectionName;
    @SerializedName("level_id")
    public int levelId;
    @SerializedName("stage_name")
    public String stageName;
    @SerializedName("code")
    public String code;
    @SerializedName("parent_name")
    public String parentName;
    @SerializedName("parent_user_id")
    public int parentUserId;
    @SerializedName("attendances")
    public ArrayList<Attendances> attendances;
    @SerializedName("today_workload_status")
    public TodayWorkLoadStatus todayWorkLoadStatus;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private boolean expanded;

    public Student() {
        super();
        this.level = "";
        this.section = "";
        this.stage = "";
        this.todayAttendance = "";
        this.todayAssignmentsCount = 0;
        this.todayQuizzesCount = 0;
        this.todayEventsCount = 0;
        this.bedoPoints = 0;
        this.userId = 0;
        this.parent = new Parent();
        courses = new ArrayList<Course>();
    }

    public Student(int id, String firstName, String lastName, String gender, String email, String avatar,
                   String userType, String level, String section, String stage, JSONObject todayWorkLoad, int bedoPoints, int userId, Parent parent, ArrayList<Course> courses,int actableId) {
        super(id, firstName, lastName, gender, email, avatar,userType);
        this.level = level;
        this.section = section;
        this.stage = stage;
        this.bedoPoints = bedoPoints;
        this.userId = userId;
        this.parent = parent;
        this.courses = courses;
        this.actableId = actableId;
        this.setTodayWorkLoad(todayWorkLoad);

    }

    public void setTodayWorkLoad(JSONObject todayWorkLoad) {
        if (todayWorkLoad == null) return;
        this.todayAttendance = todayWorkLoad.optString("attendance_status");
        this.todayAssignmentsCount = todayWorkLoad.optInt("assignments_count");
        this.todayQuizzesCount = todayWorkLoad.optInt("quizzes_count");
        this.todayEventsCount = todayWorkLoad.optInt("events_count");
    }

    public static Student create(String json) {
        Gson gson = new GsonBuilder().create();
        Student student = gson.fromJson(json, Student.class);
        student.userId = student.id;
        if (student.actableId == 0 ) student.actableId = student.childId;
        if (student.todayWorkLoadStatus != null) {
            student.todayAttendance = student.todayWorkLoadStatus.attendanceStatus;
            student.todayAssignmentsCount = student.todayWorkLoadStatus.assignmentsCount;
            student.todayQuizzesCount = student.todayWorkLoadStatus.quizzesCount;
            student.todayEventsCount = student.todayWorkLoadStatus.eventsCount;
        }
        return student;
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
    public static JSONArray getJsonArray(ArrayList<Student> arrayList) {
        JSONArray jsonArray = new JSONArray();
        for (Student object : arrayList) {
            jsonArray.put(object.toString());
        }
        return jsonArray;
    }

    public static ArrayList<Student> getArrayList(String string) {
        ArrayList<Student> arrayList = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(Student.create(jsonArray.opt(i).toString()));
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
        dest.writeString(this.level);
        dest.writeString(this.section);
        dest.writeString(this.stage);
        dest.writeString(this.todayAttendance);
        dest.writeInt(this.todayAssignmentsCount);
        dest.writeInt(this.todayQuizzesCount);
        dest.writeInt(this.todayEventsCount);
        dest.writeInt(this.bedoPoints);
        dest.writeSerializable(this.parent);
        dest.writeInt(this.userId);
        dest.writeList(this.courses);
        dest.writeString(this.username);
        dest.writeString(this.thumbUrl);
        dest.writeString(this.dateofbirth);
        dest.writeString(this.country);
        dest.writeString(this.city);
        dest.writeString(this.homeAddress);
        dest.writeString(this.password);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeString(this.lastSignInAt);
        dest.writeString(this.name);
        dest.writeInt(this.childId);
        dest.writeString(this.middlename);
        dest.writeString(this.secondaryPhone);
        dest.writeString(this.secondaryAddress);
        dest.writeString(this.locale);
        dest.writeInt(this.actableId);
        dest.writeString(this.actableType);
        dest.writeInt(this.unseenNotifications);
        dest.writeString(this.schoolName);
        dest.writeString(this.realtimeIp);
        dest.writeByte(this.passwordChanged ? (byte) 1 : (byte) 0);
        dest.writeInt(this.parentId);
        dest.writeString(this.sectionName);
        dest.writeInt(this.levelId);
        dest.writeString(this.stageName);
        dest.writeString(this.code);
        dest.writeString(this.parentName);
        dest.writeInt(this.parentUserId);
        dest.writeTypedList(this.attendances);
        dest.writeParcelable(this.todayWorkLoadStatus, flags);
        dest.writeByte(this.expanded ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.gender);
        dest.writeString(this.userType);
        dest.writeString(this.avatar);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeLong(this.dateOfBirth != null ? this.dateOfBirth.getTime() : -1);
        dest.writeString(this.mobile);
        dest.writeString(this.middleName);
    }

    protected Student(Parcel in) {
        this.level = in.readString();
        this.section = in.readString();
        this.stage = in.readString();
        this.todayAttendance = in.readString();
        this.todayAssignmentsCount = in.readInt();
        this.todayQuizzesCount = in.readInt();
        this.todayEventsCount = in.readInt();
        this.bedoPoints = in.readInt();
        this.parent = (Parent) in.readSerializable();
        this.userId = in.readInt();
        this.courses = new ArrayList<Course>();
        in.readList(this.courses, Course.class.getClassLoader());
        this.username = in.readString();
        this.thumbUrl = in.readString();
        this.dateofbirth = in.readString();
        this.country = in.readString();
        this.city = in.readString();
        this.homeAddress = in.readString();
        this.password = in.readString();
        this.isActive = in.readByte() != 0;
        this.lastSignInAt = in.readString();
        this.name = in.readString();
        this.childId = in.readInt();
        this.middlename = in.readString();
        this.secondaryPhone = in.readString();
        this.secondaryAddress = in.readString();
        this.locale = in.readString();
        this.actableId = in.readInt();
        this.actableType = in.readString();
        this.unseenNotifications = in.readInt();
        this.schoolName = in.readString();
        this.realtimeIp = in.readString();
        this.passwordChanged = in.readByte() != 0;
        this.parentId = in.readInt();
        this.sectionName = in.readString();
        this.levelId = in.readInt();
        this.stageName = in.readString();
        this.code = in.readString();
        this.parentName = in.readString();
        this.parentUserId = in.readInt();
        this.attendances = in.createTypedArrayList(Attendances.CREATOR);
        this.todayWorkLoadStatus = in.readParcelable(TodayWorkLoadStatus.class.getClassLoader());
        this.expanded = in.readByte() != 0;
        this.id = in.readInt();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.gender = in.readString();
        this.userType = in.readString();
        this.avatar = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        long tmpDateOfBirth = in.readLong();
        this.dateOfBirth = tmpDateOfBirth == -1 ? null : new Date(tmpDateOfBirth);
        this.mobile = in.readString();
        this.middleName = in.readString();
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}


