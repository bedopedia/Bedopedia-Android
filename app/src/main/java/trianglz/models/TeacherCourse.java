package trianglz.models;//
//  TeacherCourse.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 30, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import trianglz.models.CourseGradingPeriods;
import trianglz.models.CourseGroups;
import trianglz.models.ScaleGrades;

public class TeacherCourse {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("code")
	private String code;
	@SerializedName("course_groups")
	private CourseGroups[] courseGroups;
	@SerializedName("course_group_ids")
	private int[] courseGroupIds;
	@SerializedName("scale_grades")
	private ScaleGrades scaleGrades;
	@SerializedName("section_name")
	private String sectionName;
	@SerializedName("level_name")
	private String levelName;
	@SerializedName("stage_name")
	private String stageName;
	@SerializedName("total_grade")
	private int totalGrade;
	@SerializedName("pass_limit")
	private int passLimit;
	@SerializedName("show_final_grade")
	private boolean showFinalGrade;
	@SerializedName("icon_name")
	private Object iconName;
	@SerializedName("hod_id")
	private int hodId;
	@SerializedName("section_id")
	private int sectionId;
	@SerializedName("week_lessons")
	private Object[] weekLessons;
	@SerializedName("course_grading_periods")
	private CourseGradingPeriods[] courseGradingPeriods;
	@SerializedName("teachers")
	private Teachers[] teachers;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setCourseGroups(CourseGroups[] courseGroups) {
		this.courseGroups = courseGroups;
	}

	public CourseGroups[] getCourseGroups() {
		return this.courseGroups;
	}

	public void setCourseGroupIds(int[] courseGroupIds) {
		this.courseGroupIds = courseGroupIds;
	}

	public int[] getCourseGroupIds() {
		return this.courseGroupIds;
	}

	public void setScaleGrades(ScaleGrades scaleGrades) {
		this.scaleGrades = scaleGrades;
	}

	public ScaleGrades getScaleGrades() {
		return this.scaleGrades;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionName() {
		return this.sectionName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLevelName() {
		return this.levelName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getStageName() {
		return this.stageName;
	}

	public void setTotalGrade(int totalGrade) {
		this.totalGrade = totalGrade;
	}

	public int getTotalGrade() {
		return this.totalGrade;
	}

	public void setPassLimit(int passLimit) {
		this.passLimit = passLimit;
	}

	public int getPassLimit() {
		return this.passLimit;
	}

	public void setShowFinalGrade(boolean showFinalGrade) {
		this.showFinalGrade = showFinalGrade;
	}

	public boolean getShowFinalGrade() {
		return this.showFinalGrade;
	}

	public void setIconName(Object iconName) {
		this.iconName = iconName;
	}

	public Object getIconName() {
		return this.iconName;
	}

	public void setHodId(int hodId) {
		this.hodId = hodId;
	}

	public int getHodId() {
		return this.hodId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public int getSectionId() {
		return this.sectionId;
	}

	public void setWeekLessons(Object[] weekLessons) {
		this.weekLessons = weekLessons;
	}

	public Object[] getWeekLessons() {
		return this.weekLessons;
	}

	public void setCourseGradingPeriods(CourseGradingPeriods[] courseGradingPeriods) {
		this.courseGradingPeriods = courseGradingPeriods;
	}

	public CourseGradingPeriods[] getCourseGradingPeriods() {
		return this.courseGradingPeriods;
	}

	public void setTeachers(Teachers[] teachers) {
		this.teachers = teachers;
	}

	public Teachers[] getTeachers() {
		return this.teachers;
	}


	public static TeacherCourse create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, TeacherCourse.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}