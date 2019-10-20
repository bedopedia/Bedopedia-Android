package trianglz.models;//
//  Answers.java
//
//  Generated using https://jsonmaster.github.io
//  Created on October 20, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Answers {

	@SerializedName("id")
	private int id;
	@SerializedName("question_id")
	private int questionId;

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


	public static Answers create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Answers.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}