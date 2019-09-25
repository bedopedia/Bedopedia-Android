package trianglz.models;//
//  Category.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 24, 2019
//

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

public class Category implements Serializable {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("weight")
	private int weight;
	@SerializedName("course_id")
	private int courseId;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("grading_category_id")
	private int gradingCategoryId;
	@SerializedName("deleted_at")
	private String deletedAt;
	@SerializedName("parent_id")
	private int parentId;
	@SerializedName("numeric")
	private boolean numeric;

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

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return this.weight;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCourseId() {
		return this.courseId;
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

	public void setGradingCategoryId(int gradingCategoryId) {
		this.gradingCategoryId = gradingCategoryId;
	}

	public int getGradingCategoryId() {
		return this.gradingCategoryId;
	}

	public void setDeletedAt(String deletedAt) {
		this.deletedAt = deletedAt;
	}

	public String getDeletedAt() {
		return this.deletedAt;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getParentId() {
		return this.parentId;
	}

	public void setNumeric(boolean numeric) {
		this.numeric = numeric;
	}

	public boolean getNumeric() {
		return this.numeric;
	}


	public static Category create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Category.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}