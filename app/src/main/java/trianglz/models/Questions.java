package trianglz.models;//
//  Questions.java
//
//  Generated using https://jsonmaster.github.io
//  Created on August 01, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Questions {

    @SerializedName("id")
    private int id;
    @SerializedName("body")
    private String body;
    @SerializedName("difficulty")
    private String difficulty;
    @SerializedName("score")
    private int score;
    @SerializedName("answers_attributes")
    private ArrayList<AnswersAttributes> answersAttributes;
    @SerializedName("correction_style")
    private Object correctionStyle;
    @SerializedName("type")
    private String type;
    @SerializedName("bloom")
    private String[] bloom;
    @SerializedName("files")
    private Object files;
    @SerializedName("uploaded_file")
    private UploadedObject uploadedFile;
    @SerializedName("correct_answers_count")
    private int correctAnswersCount;


    @SerializedName("answers")
    private ArrayList<Answers> answers;
    @SerializedName("number_of_correct_answers")
    private int numberOfCorrectAnswers;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public void setAnswersAttributes(ArrayList<AnswersAttributes> answersAttributes) {
        this.answersAttributes = answersAttributes;
    }

    public ArrayList<AnswersAttributes> getAnswersAttributes() {
        return this.answersAttributes;
    }

    public void setCorrectionStyle(Object correctionStyle) {
        this.correctionStyle = correctionStyle;
    }

    public Object getCorrectionStyle() {
        return this.correctionStyle;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setBloom(String[] bloom) {
        this.bloom = bloom;
    }

    public String[] getBloom() {
        return this.bloom;
    }

    public void setFiles(Object files) {
        this.files = files;
    }

    public Object getFiles() {
        return this.files;
    }

    public void setUploadedFile(UploadedObject uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Object getUploadedFile() {
        return this.uploadedFile;
    }

    public void setCorrectAnswersCount(int correctAnswersCount) {
        this.correctAnswersCount = correctAnswersCount;
    }

    public int getCorrectAnswersCount() {
        return this.correctAnswersCount;
    }


    public void setAnswers(ArrayList<Answers> answers) {
        this.answers = answers;
    }

    public ArrayList<Answers> getAnswers() {
        return this.answers;
    }

    public void setNumberOfCorrectAnswers(int numberOfCorrectAnswers) {
        this.numberOfCorrectAnswers = numberOfCorrectAnswers;
    }

    public int getNumberOfCorrectAnswers() {
        return this.numberOfCorrectAnswers;
    }


    public static Questions create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, Questions.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}