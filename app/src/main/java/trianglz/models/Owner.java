package trianglz.models;//
//  Owner.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 17, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Owner implements Serializable {

	@SerializedName("id")
	public int id;
	@SerializedName("firstname")
	public String firstname;
	@SerializedName("lastname")
	public String lastname;
	@SerializedName("avatar_url")
	public String avatarUrl;
	@SerializedName("thumb_url")
	public String thumbUrl;
	@SerializedName("user_type")
	public String userType;
	@SerializedName("name")
	public String name;
	@SerializedName("gender")
	public String gender;
	@SerializedName("name_with_title")
	public String nameWithTitle;
	@SerializedName("actable_id")
	public int actableId;
	@SerializedName("user_id")
	public int userId;
	@SerializedName("password_changed")
	public boolean passwordChanged;

	public static Owner create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Owner.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}