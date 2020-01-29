package trianglz.models;//
//  GradeItems.java
//
//  Generated using https://jsonmaster.github.io
//  Created on January 28, 2020
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class GradeItems {

	@SerializedName("id")
	public int id;
	@SerializedName("name")
	public String name;
	@SerializedName("grading_period_id")
	public int gradingPeriodId;
	@SerializedName("hide_grade")
	public boolean hideGrade;
	@SerializedName("type")
	public String type;
	@SerializedName("total")
	public int total;
	@SerializedName("grade")
	public double grade;
	@SerializedName("status")
	public int status;
	@SerializedName("grade_view")
	public String gradeView;




	public static GradeItems create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, GradeItems.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}