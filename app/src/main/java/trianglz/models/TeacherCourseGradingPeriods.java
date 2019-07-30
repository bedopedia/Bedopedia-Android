package trianglz.models;//
//  CourseGradingPeriods.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 30, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import trianglz.models.CourseGradingPeriods;

public class TeacherCourseGradingPeriods {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("start_date")
	private String startDate;
	@SerializedName("end_date")
	private String endDate;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("level_id")
	private Object levelId;
	@SerializedName("academic_term_id")
	private Object academicTermId;
	@SerializedName("deleted_at")
	private Object deletedAt;
	@SerializedName("weight")
	private int weight;
	@SerializedName("parent_id")
	private Object parentId;
	@SerializedName("lock")
	private boolean lock;
	@SerializedName("publish")
	private boolean publish;
	@SerializedName("sub_grading_periods")
	private Object[] subGradingPeriods;

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

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedAt() {
		return this.createdAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt() {
		return this.updatedAt;
	}

	public void setLevelId(Object levelId) {
		this.levelId = levelId;
	}

	public Object getLevelId() {
		return this.levelId;
	}

	public void setAcademicTermId(Object academicTermId) {
		this.academicTermId = academicTermId;
	}

	public Object getAcademicTermId() {
		return this.academicTermId;
	}

	public void setDeletedAt(Object deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt() {
		return this.deletedAt;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return this.weight;
	}

	public void setParentId(Object parentId) {
		this.parentId = parentId;
	}

	public Object getParentId() {
		return this.parentId;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public boolean getLock() {
		return this.lock;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public boolean getPublish() {
		return this.publish;
	}

	public void setSubGradingPeriods(Object[] subGradingPeriods) {
		this.subGradingPeriods = subGradingPeriods;
	}

	public Object[] getSubGradingPeriods() {
		return this.subGradingPeriods;
	}


	public static CourseGradingPeriods create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, CourseGradingPeriods.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}