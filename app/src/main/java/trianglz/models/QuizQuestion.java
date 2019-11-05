package trianglz.models;

/**
 * Created by Farah A. Moniem on 25/09/2019.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuizQuestion {

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
    @SerializedName("course_groups")
    private ArrayList<CourseGroups> courseGroups;
    @SerializedName("category")
    private Category category;
    @SerializedName("lesson")
    private Lesson lesson;
    @SerializedName("unit")
    private Unit unit;
    @SerializedName("chapter")
    private Chapter chapter;
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
    @SerializedName("student_solved")
    private boolean studentSolved;
    @SerializedName("blooms")
    private String[] blooms;
    @SerializedName("grading_period_lock")
    private boolean gradingPeriodLock;
    @SerializedName("questions")
    private ArrayList<Questions> questions;
    @SerializedName("objectives")
    private ArrayList<Objective> objectives;
    @SerializedName("grouping_students")
    private GroupingStudents[] groupingStudents;
    @SerializedName("course_groups_quiz")
    private CourseGroupsQuiz[] courseGroupsQuiz;
    @SerializedName("hours")
    private int hours;
    @SerializedName("minutes")
    private int minutes;

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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setCourseGroups(ArrayList<CourseGroups> courseGroups) {
        this.courseGroups = courseGroups;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getHours() {
        return this.hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return this.minutes;
    }


    public ArrayList<CourseGroups> getCourseGroups() {
        return this.courseGroups;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public Chapter getChapter() {
        return this.chapter;
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

    public void setStudentSolved(boolean studentSolved) {
        this.studentSolved = studentSolved;
    }

    public boolean getStudentSolved() {
        return this.studentSolved;
    }

    public void setBlooms(String[] blooms) {
        this.blooms = blooms;
    }

    public String[] getBlooms() {
        return this.blooms;
    }

    public void setGradingPeriodLock(boolean gradingPeriodLock) {
        this.gradingPeriodLock = gradingPeriodLock;
    }

    public boolean getGradingPeriodLock() {
        return this.gradingPeriodLock;
    }

    public void setQuestions(ArrayList<Questions> questions) {
        this.questions = questions;
    }

    public ArrayList<Questions> getQuestions() {
        return this.questions;
    }

    public void setObjectives(ArrayList<Objective> objectives) {
        this.objectives = objectives;
    }

    public ArrayList<Objective> getObjectives() {
        return this.objectives;
    }

    public void setGroupingStudents(GroupingStudents[] groupingStudents) {
        this.groupingStudents = groupingStudents;
    }

    public GroupingStudents[] getGroupingStudents() {
        return this.groupingStudents;
    }

    public void setCourseGroupsQuiz(CourseGroupsQuiz[] courseGroupsQuiz) {
        this.courseGroupsQuiz = courseGroupsQuiz;
    }

    public CourseGroupsQuiz[] getCourseGroupsQuiz() {
        return this.courseGroupsQuiz;
    }


    public static QuizQuestion create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, QuizQuestion.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}