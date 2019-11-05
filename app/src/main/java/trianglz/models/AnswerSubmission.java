package trianglz.models;

//
//  RootClass.java
//
//  Generated using https://jsonmaster.github.io
//  Created on October 21, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AnswerSubmission {

    @SerializedName("answer_submission")
    public ArrayList<Answers> answers;
    @SerializedName("question_id")
    private int questionId;

    public void setAnswerSubmission(ArrayList<Answers> answers) {
        this.answers = answers;
    }

    public ArrayList<Answers> getAnswerSubmission() {
        return this.answers;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuestionId() {
        return this.questionId;
    }


    public static RootClass create(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, RootClass.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}