package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Models.Parent;
import gradeBook.Course;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class Student extends trianglz.models.User {
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


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getBedoPoints() {
        return bedoPoints;
    }

    public void setBedoPoints(int bedoPoints) {
        this.bedoPoints = bedoPoints;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public int getTodayAssignmentsCount() {
        return this.todayAssignmentsCount;
    }

    public int getTodayEventsCount() {
        return this.todayEventsCount;
    }

    public int getTodayQuizzesCount() {
        return this.todayQuizzesCount;
    }

    public String getTodayAttendance() {
        return this.todayAttendance;
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
        student.userId = student.getId();
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
}


