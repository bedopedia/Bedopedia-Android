package trianglz.models;//
//  AssignmentsCourseGroups.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 24, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AssignmentsCourseGroups {

	@SerializedName("id")
	private int id;
	@SerializedName("course_group_id")
	private int courseGroupId;
	@SerializedName("assignment_id")
	private int assignmentId;
	@SerializedName("deleted_at")
	private Object deletedAt;
	@SerializedName("hide_grade")
	private boolean hideGrade;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setCourseGroupId(int courseGroupId) {
		this.courseGroupId = courseGroupId;
	}

	public int getCourseGroupId() {
		return this.courseGroupId;
	}

	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}

	public int getAssignmentId() {
		return this.assignmentId;
	}

	public void setDeletedAt(Object deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt() {
		return this.deletedAt;
	}

	public void setHideGrade(boolean hideGrade) {
		this.hideGrade = hideGrade;
	}

	public boolean getHideGrade() {
		return this.hideGrade;
	}


	public static AssignmentsCourseGroups create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, AssignmentsCourseGroups.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}