package trianglz.models;//
//  PostsResponse.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 17, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import trianglz.models.Posts;

public class PostsResponse {

	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("course_id")
	private int courseId;
	@SerializedName("course_name")
	private String courseName;
	@SerializedName("posts_count")
	private int postsCount;
	@SerializedName("posts")
	private Posts posts;

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

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCourseId() {
		return this.courseId;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseName() {
		return this.courseName;
	}

	public void setPostsCount(int postsCount) {
		this.postsCount = postsCount;
	}

	public int getPostsCount() {
		return this.postsCount;
	}

	public void setPosts(Posts posts) {
		this.posts = posts;
	}

	public Posts getPosts() {
		return this.posts;
	}


	public static PostsResponse create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, PostsResponse.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}