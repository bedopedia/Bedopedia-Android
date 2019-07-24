package trianglz.models;//
//  Category.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 24, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Category {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("parent_id")
	private Object parentId;

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

	public void setParentId(Object parentId) {
		this.parentId = parentId;
	}

	public Object getParentId() {
		return this.parentId;
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