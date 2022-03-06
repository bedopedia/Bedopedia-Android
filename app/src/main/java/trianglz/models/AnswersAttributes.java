package trianglz.models;//
//  AnswersAttributes.java
//
//  Generated using https://jsonmaster.github.io
//  Created on August 01, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class AnswersAttributes {

	@SerializedName("id")
	private int id;
	@SerializedName("body")
	private String body;
	@SerializedName("is_correct")
	private boolean isCorrect;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("question_id")
	private int questionId;
	@SerializedName("match")
	private String match;
	@SerializedName("deleted_at")
	private String deletedAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
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

	public static AnswersAttributes create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, AnswersAttributes.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}