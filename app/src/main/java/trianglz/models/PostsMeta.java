package trianglz.models;//
//  Meta.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 17, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import trianglz.models.Meta;

public class PostsMeta {

	@SerializedName("course_group_name")
	private String courseGroupName;
	@SerializedName("course_id")
	private int courseId;

	public void setCourseGroupName(String courseGroupName) {
		this.courseGroupName = courseGroupName;
	}

	public String getCourseGroupName() {
		return this.courseGroupName;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCourseId() {
		return this.courseId;
	}


	public static PostsMeta create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, PostsMeta.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}
