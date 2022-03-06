package trianglz.models;//
//  SingleAssignment.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 24, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SingleAssignment {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("description")
	private String description;
	@SerializedName("state")
	private String state;
	@SerializedName("end_at")
	private String endAt;
	@SerializedName("start_at")
	private String startAt;
	@SerializedName("assignment_type")
	private String assignmentType;
	@SerializedName("blooms")
	private String[] blooms;
	@SerializedName("points")
	private int points;
	@SerializedName("late_submissions_date")
	private String lateSubmissionsDate;
	@SerializedName("late_submission_points")
	private Object lateSubmissionPoints;
	@SerializedName("content")
	private String content;
	@SerializedName("category")
	private Category category;
	@SerializedName("lesson_name")
	private String lessonName;
	@SerializedName("chapter_name")
	private String chapterName;
	@SerializedName("file_name")
	private Object fileName;
	@SerializedName("teacher_id")
	private int teacherId;
	@SerializedName("online_text")
	private boolean onlineText;
	@SerializedName("file_upload")
	private boolean fileUpload;
	@SerializedName("hard_copy")
	private boolean hardCopy;
	@SerializedName("unit_name")
	private String unitName;
	@SerializedName("late_submission")
	private boolean lateSubmission;
	@SerializedName("submissions_student_ids")
	private Object[] submissionsStudentIds;
	@SerializedName("admin_id")
	private Object adminId;
	@SerializedName("file_url")
	private Object fileUrl;
	@SerializedName("content_type")
	private Object contentType;
	@SerializedName("assignments_course_groups")
	private AssignmentsCourseGroups[] assignmentsCourseGroups;
	@SerializedName("assignments_objectives")
	private Object[] assignmentsObjectives;
	@SerializedName("grading_period_lock")
	private boolean gradingPeriodLock;
	@SerializedName("course_groups")
	private CourseGroups[] courseGroups;
	@SerializedName("objectives")
	private Object[] objectives;
	@SerializedName("uploaded_files")
	private UploadedObject[] uploadedFiles;

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

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public String getEndAt() {
		return this.endAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public String getStartAt() {
		return this.startAt;
	}

	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}

	public String getAssignmentType() {
		return this.assignmentType;
	}

	public void setBlooms(String[] blooms) {
		this.blooms = blooms;
	}

	public String[] getBlooms() {
		return this.blooms;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getPoints() {
		return this.points;
	}

	public void setLateSubmissionsDate(String lateSubmissionsDate) {
		this.lateSubmissionsDate = lateSubmissionsDate;
	}

	public String getLateSubmissionsDate() {
		return this.lateSubmissionsDate;
	}

	public void setLateSubmissionPoints(Object lateSubmissionPoints) {
		this.lateSubmissionPoints = lateSubmissionPoints;
	}

	public Object getLateSubmissionPoints() {
		return this.lateSubmissionPoints;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public String getLessonName() {
		return this.lessonName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getChapterName() {
		return this.chapterName;
	}

	public void setFileName(Object fileName) {
		this.fileName = fileName;
	}

	public Object getFileName() {
		return this.fileName;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public int getTeacherId() {
		return this.teacherId;
	}

	public void setOnlineText(boolean onlineText) {
		this.onlineText = onlineText;
	}

	public boolean getOnlineText() {
		return this.onlineText;
	}

	public void setFileUpload(boolean fileUpload) {
		this.fileUpload = fileUpload;
	}

	public boolean getFileUpload() {
		return this.fileUpload;
	}

	public void setHardCopy(boolean hardCopy) {
		this.hardCopy = hardCopy;
	}

	public boolean getHardCopy() {
		return this.hardCopy;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setLateSubmission(boolean lateSubmission) {
		this.lateSubmission = lateSubmission;
	}

	public boolean getLateSubmission() {
		return this.lateSubmission;
	}

	public void setSubmissionsStudentIds(Object[] submissionsStudentIds) {
		this.submissionsStudentIds = submissionsStudentIds;
	}

	public Object[] getSubmissionsStudentIds() {
		return this.submissionsStudentIds;
	}

	public void setAdminId(Object adminId) {
		this.adminId = adminId;
	}

	public Object getAdminId() {
		return this.adminId;
	}

	public void setFileUrl(Object fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Object getFileUrl() {
		return this.fileUrl;
	}

	public void setContentType(Object contentType) {
		this.contentType = contentType;
	}

	public Object getContentType() {
		return this.contentType;
	}

	public void setAssignmentsCourseGroups(AssignmentsCourseGroups[] assignmentsCourseGroups) {
		this.assignmentsCourseGroups = assignmentsCourseGroups;
	}

	public AssignmentsCourseGroups[] getAssignmentsCourseGroups() {
		return this.assignmentsCourseGroups;
	}

	public void setAssignmentsObjectives(Object[] assignmentsObjectives) {
		this.assignmentsObjectives = assignmentsObjectives;
	}

	public Object[] getAssignmentsObjectives() {
		return this.assignmentsObjectives;
	}

	public void setGradingPeriodLock(boolean gradingPeriodLock) {
		this.gradingPeriodLock = gradingPeriodLock;
	}

	public boolean getGradingPeriodLock() {
		return this.gradingPeriodLock;
	}

	public void setCourseGroups(CourseGroups[] courseGroups) {
		this.courseGroups = courseGroups;
	}

	public CourseGroups[] getCourseGroups() {
		return this.courseGroups;
	}

	public void setObjectives(Object[] objectives) {
		this.objectives = objectives;
	}

	public Object[] getObjectives() {
		return this.objectives;
	}

	public void setUploadedFiles(UploadedObject[] uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public UploadedObject[] getUploadedFiles() {
		return this.uploadedFiles;
	}


	public static SingleAssignment create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, SingleAssignment.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}