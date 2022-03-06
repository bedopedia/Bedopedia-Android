package trianglz.models;//
//  ScaleGrades.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 30, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ScaleGrades {

	@SerializedName("id")
	private int id;
	@SerializedName("high")
	private int high;
	@SerializedName("low")
	private int low;
	@SerializedName("grade")
	private String grade;
	@SerializedName("scale_id")
	private int scaleId;
	@SerializedName("deleted_at")
	private Object deletedAt;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public int getHigh() {
		return this.high;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getLow() {
		return this.low;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setScaleId(int scaleId) {
		this.scaleId = scaleId;
	}

	public int getScaleId() {
		return this.scaleId;
	}

	public void setDeletedAt(Object deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt() {
		return this.deletedAt;
	}


	public static ScaleGrades create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, ScaleGrades.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}