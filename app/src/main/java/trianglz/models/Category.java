package trianglz.models;//
//  Category.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 24, 2019
//

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Category {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("parent_id")
	private int parentId;

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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
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