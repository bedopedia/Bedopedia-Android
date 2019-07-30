package trianglz.models;//
//  Scales.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 30, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Scales {

	@SerializedName("id")
	private int id;
	@SerializedName("title")
	private String title;
	@SerializedName("teacher_id")
	private Object teacherId;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("course_id")
	private int courseId;
	@SerializedName("grading_scale_id")
	private Object gradingScaleId;
	@SerializedName("deleted_at")
	private Object deletedAt;
	@SerializedName("scale_grades")
	private ScaleGrades[] scaleGrades;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTeacherId(Object teacherId) {
		this.teacherId = teacherId;
	}

	public Object getTeacherId() {
		return this.teacherId;
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

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCourseId() {
		return this.courseId;
	}

	public void setGradingScaleId(Object gradingScaleId) {
		this.gradingScaleId = gradingScaleId;
	}

	public Object getGradingScaleId() {
		return this.gradingScaleId;
	}

	public void setDeletedAt(Object deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt() {
		return this.deletedAt;
	}

	public void setScaleGrades(ScaleGrades[] scaleGrades) {
		this.scaleGrades = scaleGrades;
	}

	public ScaleGrades[] getScaleGrades() {
		return this.scaleGrades;
	}


	public static Scales create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Scales.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}