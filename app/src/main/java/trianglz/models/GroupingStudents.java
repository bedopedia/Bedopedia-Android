package trianglz.models;//
//  GroupingStudents.java
//
//  Generated using https://jsonmaster.github.io
//  Created on September 25, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GroupingStudents implements Serializable {

	@SerializedName("id")
	private int id;
	@SerializedName("assignable_id")
	private int assignableId;
	@SerializedName("assignable_type")
	private String assignableType;
	@SerializedName("course_group_id")
	private int courseGroupId;
	@SerializedName("student_id")
	private int studentId;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("updated_at")
	private String updatedAt;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setAssignableId(int assignableId) {
		this.assignableId = assignableId;
	}

	public int getAssignableId() {
		return this.assignableId;
	}

	public void setAssignableType(String assignableType) {
		this.assignableType = assignableType;
	}

	public String getAssignableType() {
		return this.assignableType;
	}

	public void setCourseGroupId(int courseGroupId) {
		this.courseGroupId = courseGroupId;
	}

	public int getCourseGroupId() {
		return this.courseGroupId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getStudentId() {
		return this.studentId;
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


	public static GroupingStudents create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, GroupingStudents.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}