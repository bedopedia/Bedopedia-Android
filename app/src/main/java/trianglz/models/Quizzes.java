package trianglz.models;//
//  Quizzes.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 25, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Quizzes {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("start_date")
	private String startDate;
	@SerializedName("end_date")
	private String endDate;
	@SerializedName("description")
	private Object description;
	@SerializedName("duration")
	private int duration;
	@SerializedName("is_questions_randomized")
	private boolean isQuestionsRandomized;
	@SerializedName("num_of_questions_per_page")
	private int numOfQuestionsPerPage;
	@SerializedName("state")
	private String state;
	@SerializedName("total_score")
	private int totalScore;
	@SerializedName("lesson_id")
	private int lessonId;
	@SerializedName("grading_period_lock")
	private boolean gradingPeriodLock;
	@SerializedName("student_submissions")
	private Object studentSubmissions;

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

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setDescription(Object description) {
		this.description = description;
	}

	public Object getDescription() {
		return this.description;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return this.duration;
	}

	public void setIsQuestionsRandomized(boolean isQuestionsRandomized) {
		this.isQuestionsRandomized = isQuestionsRandomized;
	}

	public boolean getIsQuestionsRandomized() {
		return this.isQuestionsRandomized;
	}

	public void setNumOfQuestionsPerPage(int numOfQuestionsPerPage) {
		this.numOfQuestionsPerPage = numOfQuestionsPerPage;
	}

	public int getNumOfQuestionsPerPage() {
		return this.numOfQuestionsPerPage;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getTotalScore() {
		return this.totalScore;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public int getLessonId() {
		return this.lessonId;
	}

	public void setGradingPeriodLock(boolean gradingPeriodLock) {
		this.gradingPeriodLock = gradingPeriodLock;
	}

	public boolean getGradingPeriodLock() {
		return this.gradingPeriodLock;
	}

	public void setStudentSubmissions(Object studentSubmissions) {
		this.studentSubmissions = studentSubmissions;
	}

	public Object getStudentSubmissions() {
		return this.studentSubmissions;
	}


	public static Quizzes create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Quizzes.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}