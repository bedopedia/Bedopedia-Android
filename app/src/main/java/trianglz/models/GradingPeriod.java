package trianglz.models;//
//  GradingPeriod.java
//
//  Generated using https://jsonmaster.github.io
//  Created on January 28, 2020
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

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
	public GradeItems[] gradeItems;
	@SerializedName("quizzes")
	public Quizzes[] quizzes;
	@SerializedName("assignments")
	public Assignments[] assignments;
	@SerializedName("sub_grading_periods")
	public SubGradingPeriod subGradingPeriod;
	

	public static GradingPeriod create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, GradingPeriod.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}