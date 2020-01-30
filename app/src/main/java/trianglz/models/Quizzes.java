package trianglz.models;//
//  Quizzes.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 25, 2019
//

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Quizzes implements Parcelable {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("start_date")
	private String startDate;
	@SerializedName("end_date")
	private String endDate;
	@SerializedName("description")
	private String description;
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
	private boolean studentSolved;
	@SerializedName("blooms")
	private String[] blooms;
	@SerializedName("questions")
	private Questions[] questions;
	@SerializedName("objectives")
	private Object[] objectives;
    @SerializedName("status")
    private String status;
    @SerializedName("grading_period_id")
    private int gradingPeriodId;
    @SerializedName("type")
    private String type;
    @SerializedName("total")
    private int total;
    @SerializedName("grade")
    private double grade;
    @SerializedName("hide_grade")
    private int hideGrade;
    @SerializedName("grade_view")
    private String gradeView;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setGradingPeriodId(int gradingPeriodId) {
        this.gradingPeriodId = gradingPeriodId;
    }

    public int getGradingPeriodId() {
        return this.gradingPeriodId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getGrade() {
        return this.grade;
    }

    public void setHideGrade(int hideGrade) {
        this.hideGrade = hideGrade;
    }

    public int getHideGrade() {
        return this.hideGrade;
    }

    public void setGradeView(String gradeView) {
        this.gradeView = gradeView;
    }

    public String getGradeView() {
        return this.gradeView;
    }


    protected Quizzes(Parcel in) {
        id = in.readInt();
        name = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        duration = in.readInt();
        isQuestionsRandomized = in.readByte() != 0;
        numOfQuestionsPerPage = in.readInt();
        state = in.readString();
        totalScore = in.readDouble();
        lessonId = in.readInt();
        gradingPeriodLock = in.readByte() != 0;
        blooms = in.createStringArray();
    }

    public static final Creator<Quizzes> CREATOR = new Creator<Quizzes>() {
        @Override
        public Quizzes createFromParcel(Parcel in) {
            return new Quizzes(in);
        }

        @Override
        public Quizzes[] newArray(int size) {
            return new Quizzes[size];
        }
    };

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

    public void setDescription(String description) {
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

    public boolean isStudentSolved() {
        return studentSolved;
    }

    public void setStudentSolved(boolean studentSolved) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeInt(duration);
        parcel.writeByte((byte) (isQuestionsRandomized ? 1 : 0));
        parcel.writeInt(numOfQuestionsPerPage);
        parcel.writeString(state);
        parcel.writeDouble(totalScore);
        parcel.writeInt(lessonId);
        parcel.writeByte((byte) (gradingPeriodLock ? 1 : 0));
        parcel.writeStringArray(blooms);
    }
}