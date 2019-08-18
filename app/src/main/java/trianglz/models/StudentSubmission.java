package trianglz.models;//
//  StudentSubmission.java
//
//  Generated using https://jsonmaster.github.io
//  Created on August 05, 2019
//

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StudentSubmission {

	@SerializedName("student_name")
	private String studentName;
	@SerializedName("avatar_url")
	private String avatarUrl;
	@SerializedName("student_id")
	private int studentId;
	@SerializedName("feedback")
	private Feedback feedback;
	@SerializedName("id")
	private int id;
	@SerializedName("graded")
	private boolean graded;
	@SerializedName("grade")
	private Double grade;
	@SerializedName("assignment_id")
	private int assignmentId;
	@SerializedName("course_group_id")
	private int courseGroupId;
	@SerializedName("student_status")
	private String studentStatus;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("file_name")
	private Object fileName;
	@SerializedName("max_grade")
	private double maxGrade;
	@SerializedName("answers")
	private Object answers;
	@SerializedName("old_grade")
	private double oldGrade;
	@SerializedName("grade_view")
	private double gradeView;
	@SerializedName("is_hidden")
	private boolean isHidden;

	// quizzes submissions new fields
	@SerializedName("score")
	private Double score;
	@SerializedName("student_avatar_url")
	private String studentAvatarUrl;
	@SerializedName("is_submitted")
	private boolean isSubmitted;

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentName() {
		return this.studentName;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getAvatarUrl() {
		return this.avatarUrl;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getStudentId() {
		return this.studentId;
	}

	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}

	public Feedback getFeedback() {
		return this.feedback;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setGraded(boolean graded) {
		this.graded = graded;
	}

	public boolean getGraded() {
		return this.graded;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public Double getGrade() {
		return this.grade;
	}

	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}

	public int getAssignmentId() {
		return this.assignmentId;
	}

	public void setCourseGroupId(int courseGroupId) {
		this.courseGroupId = courseGroupId;
	}

	public int getCourseGroupId() {
		return this.courseGroupId;
	}

	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}

	public String getStudentStatus() {
		return this.studentStatus;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedAt() {
		return this.createdAt;
	}

	public void setFileName(Object fileName) {
		this.fileName = fileName;
	}

	public Object getFileName() {
		return this.fileName;
	}

	public void setMaxGrade(double maxGrade) {
		this.maxGrade = maxGrade;
	}

	public double getMaxGrade() {
		return this.maxGrade;
	}

	public void setAnswers(Object answers) {
		this.answers = answers;
	}

	public Object getAnswers() {
		return this.answers;
	}

	public void setOldGrade(double oldGrade) {
		this.oldGrade = oldGrade;
	}

	public double getOldGrade() {
		return this.oldGrade;
	}

	public void setGradeView(double gradeView) {
		this.gradeView = gradeView;
	}

	public double getGradeView() {
		return this.gradeView;
	}

	public void setIsHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean getIsHidden() {
		return this.isHidden;
	}

	public boolean isGraded() {
		return graded;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean hidden) {
		isHidden = hidden;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getStudentAvatarUrl() {
		return studentAvatarUrl;
	}

	public void setStudentAvatarUrl(String studentAvatarUrl) {
		this.studentAvatarUrl = studentAvatarUrl;
	}

	public boolean isSubmitted() {
		return isSubmitted;
	}

	public void setSubmitted(boolean submitted) {
		isSubmitted = submitted;
	}

	public static StudentSubmission create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, StudentSubmission.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}