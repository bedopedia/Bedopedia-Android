package trianglz.models;//
//  QuizzCourse.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 24, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class QuizzCourse {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("course_name")
	private String courseName;
	@SerializedName("course_id")
	private int courseId;
	@SerializedName("quizzes_count")
	private int quizzesCount;
	@SerializedName("next_quiz_date")
	private String nextQuizDate;
	@SerializedName("quiz_name")
	private String quizName;
	@SerializedName("quiz_state")
	private String quizState;
	@SerializedName("next_quiz_start_date")
	private String nextQuizStartDate;
	@SerializedName("running_quizzes_count")
	private int runningQuizzesCount;

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

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseName() {
		return this.courseName;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCourseId() {
		return this.courseId;
	}

	public void setQuizzesCount(int quizzesCount) {
		this.quizzesCount = quizzesCount;
	}

	public int getQuizzesCount() {
		return this.quizzesCount;
	}

	public void setNextQuizDate(String nextQuizDate) {
		this.nextQuizDate = nextQuizDate;
	}

	public String getNextQuizDate() {
		return this.nextQuizDate;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public String getQuizName() {
		return this.quizName;
	}

	public void setQuizState(String quizState) {
		this.quizState = quizState;
	}

	public Object getQuizState() {
		return this.quizState;
	}

	public void setNextQuizStartDate(String nextQuizStartDate) {
		this.nextQuizStartDate = nextQuizStartDate;
	}

	public String getNextQuizStartDate() {
		return this.nextQuizStartDate;
	}

	public int getRunningQuizzesCount() {
		return runningQuizzesCount;
	}

	public void setRunningQuizzesCount(int runningQuizzesCount) {
		this.runningQuizzesCount = runningQuizzesCount;
	}

	public static QuizzCourse create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, QuizzCourse.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}