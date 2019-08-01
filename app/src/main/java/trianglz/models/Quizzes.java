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
	private double totalScore;
	@SerializedName("lesson_id")
	private int lessonId;
	@SerializedName("grading_period_lock")
	private boolean gradingPeriodLock;
	@SerializedName("student_submissions")
	private StudentSubmissions studentSubmissions;
	@SerializedName("course_groups")
	private CourseGroups[] courseGroups;
	@SerializedName("category")
	private Category category;
	@SerializedName("lesson")
	private Lesson lesson;
	@SerializedName("unit")
	private Unit unit;
	@SerializedName("chapter")
	private Chapter chapter;
	@SerializedName("student_solved")
	private Object studentSolved;
	@SerializedName("blooms")
	private String[] blooms;
	@SerializedName("questions")
	private Questions[] questions;
	@SerializedName("objectives")
	private Object[] objectives;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isQuestionsRandomized() {
        return isQuestionsRandomized;
    }

    public void setQuestionsRandomized(boolean questionsRandomized) {
        isQuestionsRandomized = questionsRandomized;
    }

    public int getNumOfQuestionsPerPage() {
        return numOfQuestionsPerPage;
    }

    public void setNumOfQuestionsPerPage(int numOfQuestionsPerPage) {
        this.numOfQuestionsPerPage = numOfQuestionsPerPage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public boolean isGradingPeriodLock() {
        return gradingPeriodLock;
    }

    public void setGradingPeriodLock(boolean gradingPeriodLock) {
        this.gradingPeriodLock = gradingPeriodLock;
    }

    public void setStudentSubmissions(StudentSubmissions studentSubmissions) {
        this.studentSubmissions = studentSubmissions;
    }

    public CourseGroups[] getCourseGroups() {
        return courseGroups;
    }

    public void setCourseGroups(CourseGroups[] courseGroups) {
        this.courseGroups = courseGroups;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public Object getStudentSolved() {
        return studentSolved;
    }

    public void setStudentSolved(Object studentSolved) {
        this.studentSolved = studentSolved;
    }

    public String[] getBlooms() {
        return blooms;
    }

    public void setBlooms(String[] blooms) {
        this.blooms = blooms;
    }

    public Questions[] getQuestions() {
        return questions;
    }

    public void setQuestions(Questions[] questions) {
        this.questions = questions;
    }

    public Object[] getObjectives() {
        return objectives;
    }

    public void setObjectives(Object[] objectives) {
        this.objectives = objectives;
    }

    public StudentSubmissions getStudentSubmissions() {
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