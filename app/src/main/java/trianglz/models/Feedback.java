package trianglz.models;//
//  Feedback.java
//
//  Generated using https://jsonmaster.github.io
//  Created on August 05, 2019
//

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Feedback {

	@SerializedName("id")
	private Integer id;
	@SerializedName("content")
	private String content;
	@SerializedName("owner_id")
	private Integer ownerId;
	@SerializedName("on_id")
	private int onId;
	@SerializedName("on_type")
	private String onType;
	@SerializedName("to_id")
	private int toId;
	@SerializedName("to_type")
	private String toType;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("deleted_at")
	private String deletedAt;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getOwnerId() {
		return this.ownerId;
	}

	public void setOnId(int onId) {
		this.onId = onId;
	}

	public int getOnId() {
		return this.onId;
	}

	public void setOnType(String onType) {
		this.onType = onType;
	}

	public String getOnType() {
		return this.onType;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public int getToId() {
		return this.toId;
	}

	public void setToType(String toType) {
		this.toType = toType;
	}

	public String getToType() {
		return this.toType;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedAt() {
		return this.createdAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt() {
		return this.updatedAt;
	}

	public void setDeletedAt(String deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt() {
		return this.deletedAt;
	}


	public static Feedback create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Feedback.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}