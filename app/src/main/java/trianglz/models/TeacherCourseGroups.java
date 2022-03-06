package trianglz.models;//
//  CourseGroups.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 30, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TeacherCourseGroups {

	@SerializedName("id")
	private int id;
	@SerializedName("course_id")
	private int courseId;
	@SerializedName("name")
	private String name;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("course_group_type")
	private Object courseGroupType;
	@SerializedName("capacity")
	private int capacity;
	@SerializedName("image")
	private Image image;
	@SerializedName("policy")
	private Object policy;
	@SerializedName("deleted_at")
	private Object deletedAt;
	@SerializedName("order")
	private int order;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCourseId() {
		return this.courseId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
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

	public void setCourseGroupType(Object courseGroupType) {
		this.courseGroupType = courseGroupType;
	}

	public Object getCourseGroupType() {
		return this.courseGroupType;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return this.image;
	}

	public void setPolicy(Object policy) {
		this.policy = policy;
	}

	public Object getPolicy() {
		return this.policy;
	}

	public void setDeletedAt(Object deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt() {
		return this.deletedAt;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
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
