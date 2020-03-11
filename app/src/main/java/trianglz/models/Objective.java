package trianglz.models;//
//  Objective.java
//
//  Generated using https://jsonmaster.github.io
//  Created on September 29, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Objective implements Serializable {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;

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


	public static Objective create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Objective.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}