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
	private ArrayList<Options> options;

	public void setMatches(ArrayList<String> matches) {
		this.matches = matches;
	}

	public ArrayList<String> getMatches() {
		return this.matches;
	}

	public void setOptions(ArrayList<Options> options) {
		this.options = options;
	}

	public ArrayList<Options> getOptions() {
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