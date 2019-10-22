package trianglz.models;//
//  Answers.java
//
//  Generated using https://jsonmaster.github.io
//  Created on October 20, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Answers {

	@SerializedName("id")
	private int id;
	@SerializedName("question_id")
	private int questionId;
	@SerializedName("body")
	private String body;
	@SerializedName("matches")
	private ArrayList<String> matches;
	@SerializedName("options")
	private ArrayList<Answers> options;
	@SerializedName("is_correct")
	private boolean isCorrect;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("match")
	private String match;
	@SerializedName("deleted_at")
	private String deletedAt;
	@SerializedName("answer_id")
	private int answerId;
	@SerializedName("quiz_submission_id")
	private int quizSubmissionId;
    private int matchIndex;

    public int getMatchIndex() {
        return matchIndex;
    }

    public void setMatchIndex(int matchIndex) {
        this.matchIndex = matchIndex;
    }


	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean correct) {
		isCorrect = correct;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}



	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(String deletedAt) {
		this.deletedAt = deletedAt;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public int getAnswerId() {
		return this.answerId;
	}

	public void setQuizSubmissionId(int quizSubmissionId) {
		this.quizSubmissionId = quizSubmissionId;
	}

	public int getQuizSubmissionId() {
		return this.quizSubmissionId;
	}
	public void setMatches(ArrayList<String> matches) {
		this.matches = matches;
	}

	public ArrayList<String> getMatches() {
		return this.matches;
	}

	public void setOptions(ArrayList<Answers> options) {
		this.options = options;
	}

	public ArrayList<Answers> getOptions() {
		return this.options;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getQuestionId() {
		return this.questionId;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return this.body;
	}
	public static Answers create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Answers.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}