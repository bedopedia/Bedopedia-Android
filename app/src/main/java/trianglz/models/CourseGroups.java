package trianglz.models;//
//  CourseGroups.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 24, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CourseGroups implements Serializable {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("icon_name")
	private String iconName;
	@SerializedName("course_name")
	private String courseName;
	@SerializedName("course_id")
	private int courseId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public static CourseGroups create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, CourseGroups.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}