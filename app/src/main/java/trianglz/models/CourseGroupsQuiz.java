package trianglz.models;//
//  CourseGroupsQuiz.java
//
//  Generated using https://jsonmaster.github.io
//  Created on September 25, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CourseGroupsQuiz implements Serializable {

	@SerializedName("course_group_id")
	private int courseGroupId;
	@SerializedName("quiz_id")
	private int quizId;
	@SerializedName("deleted_at")
	private Object deletedAt;
	@SerializedName("hide_grade")
	private boolean hideGrade;
	@SerializedName("id")
	private int id;
	@SerializedName("select_all")
	private boolean selectAll;

	public void setCourseGroupId(int courseGroupId) {
		this.courseGroupId = courseGroupId;
	}

	public int getCourseGroupId() {
		return this.courseGroupId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getQuizId() {
		return this.quizId;
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

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}

	public boolean getSelectAll() {
		return this.selectAll;
	}


	public static CourseGroupsQuiz create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, CourseGroupsQuiz.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}