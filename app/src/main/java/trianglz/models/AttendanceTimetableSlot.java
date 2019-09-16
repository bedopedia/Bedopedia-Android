package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Farah A. Moniem on 16/09/2019.
 */
public class AttendanceTimetableSlot {

    @SerializedName("id")
    private int id;
    @SerializedName("day")
    private String day;
    @SerializedName("slot_no")
    private int slotNo;
    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;
    @SerializedName("section_name")
    private String sectionName;
    @SerializedName("course_name")
    private String courseName;
    @SerializedName("course_group_name")
    private String courseGroupName;
    @SerializedName("school_unit")
    private String schoolUnit;
    @SerializedName("day_number")
    private int dayNumber;
    @SerializedName("course_group_id")
    private int courseGroupId;
    @SerializedName("teachers")
    private ArrayList<Teachers> teachers;
    @SerializedName("course_id")
    private int courseId;
    @SerializedName("level")
    private ArrayList<Level> level;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return this.day;
    }

    public void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }

    public int getSlotNo() {
        return this.slotNo;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return this.from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return this.to;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseGroupName(String courseGroupName) {
        this.courseGroupName = courseGroupName;
    }

    public String getCourseGroupName() {
        return this.courseGroupName;
    }

    public void setSchoolUnit(String schoolUnit) {
        this.schoolUnit = schoolUnit;
    }

    public String getSchoolUnit() {
        return this.schoolUnit;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getDayNumber() {
        return this.dayNumber;
    }


    public void setCourseGroupId(int courseGroupId) {
        this.courseGroupId = courseGroupId;
    }

    public int getCourseGroupId() {
        return this.courseGroupId;
    }

    public void setTeachers(ArrayList<Teachers> teachers) {
        this.teachers = teachers;
    }

    public ArrayList<Teachers> getTeachers() {
        return this.teachers;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setLevel(ArrayList<Level> level) {
        this.level = level;
    }

    public ArrayList<Level> getLevel() {
        return this.level;
    }


    public static AttendanceTimetableSlot create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, AttendanceTimetableSlot.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}