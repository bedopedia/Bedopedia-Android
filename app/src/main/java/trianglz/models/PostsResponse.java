package trianglz.models;//
//  PostsResponse.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 17, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

public class PostsResponse implements Serializable {

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
	public static ArrayList<PostsResponse> getArrayList(String string) {
		if (string == null || string.isEmpty()) return new ArrayList<PostsResponse>();
		ArrayList<PostsResponse> arrayList = new ArrayList<>();
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.length(); i++) {
				arrayList.add(PostsResponse.create(jsonArray.opt(i).toString()));
			}
		}
		return arrayList;
	}
}