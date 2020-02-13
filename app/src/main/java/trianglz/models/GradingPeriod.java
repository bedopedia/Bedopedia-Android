package trianglz.models;//
//  GradingPeriod.java
//
//  Generated using https://jsonmaster.github.io
//  Created on January 28, 2020
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GradingPeriod {

	@SerializedName("id")
	public int id;
	@SerializedName("name")
	public String name;
	@SerializedName("start_date")
	public String startDate;
	@SerializedName("end_date")
	public String endDate;
	@SerializedName("grade_items")
	public ArrayList<GradeItems> gradeItems;
	@SerializedName("quizzes")
	public ArrayList<Quizzes> quizzes;
	@SerializedName("assignments")
	public ArrayList<Assignments> assignments;
	@SerializedName("sub_grading_periods_attributes")
	public ArrayList<SubGradingPeriod> subGradingPeriods;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("level_id")
	private String levelId;
	@SerializedName("academic_term_id")
	private String academicTermId;
	@SerializedName("deleted_at")
	private String deletedAt;
	@SerializedName("weight")
	private Object weight;
	@SerializedName("parent_id")
	private int parentId;
	@SerializedName("lock")
	private boolean lock;
	@SerializedName("publish")
	private boolean publish;
	

	public static GradingPeriod create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, GradingPeriod.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}