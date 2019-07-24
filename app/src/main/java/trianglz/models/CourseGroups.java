package trianglz.models;//
//  CourseGroups.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 24, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CourseGroups {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("icon_name")
	private Object iconName;
	@SerializedName("course_name")
	private String courseName;
	@SerializedName("course_id")
	private int courseId;

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

	public void setIconName(Object iconName) {
		this.iconName = iconName;
	}

	public Object getIconName() {
		return this.iconName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseName() {
		return this.courseName;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCourseId() {
		return this.courseId;
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