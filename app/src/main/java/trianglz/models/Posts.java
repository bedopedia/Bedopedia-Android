package trianglz.models;//
//  Posts.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 17, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Posts {

	@SerializedName("id")
	private int id;
	@SerializedName("content")
	private String content;
	@SerializedName("postable_type")
	private String postableType;
	@SerializedName("postable_id")
	private int postableId;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("meta")
	private Meta meta;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("video_preview")
	private String videoPreview;
	@SerializedName("owner")
	private Owner owner;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setPostableType(String postableType) {
		this.postableType = postableType;
	}

	public String getPostableType() {
		return this.postableType;
	}

	public void setPostableId(int postableId) {
		this.postableId = postableId;
	}

	public int getPostableId() {
		return this.postableId;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedAt() {
		return this.createdAt;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Meta getMeta() {
		return this.meta;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt() {
		return this.updatedAt;
	}

	public void setVideoPreview(String videoPreview) {
		this.videoPreview = videoPreview;
	}

	public String getVideoPreview() {
		return this.videoPreview;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Owner getOwner() {
		return this.owner;
	}


	public static Posts create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Posts.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}