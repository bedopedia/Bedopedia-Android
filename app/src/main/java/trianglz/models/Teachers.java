//
//  Teachers.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 30, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Teachers {

	@SerializedName("id")
	private int id;
	@SerializedName("firstname")
	private String firstname;
	@SerializedName("lastname")
	private String lastname;
	@SerializedName("avatar_url")
	private String avatarUrl;
	@SerializedName("thumb_url")
	private String thumbUrl;
	@SerializedName("user_type")
	private String userType;
	@SerializedName("name")
	private String name;
	@SerializedName("gender")
	private String gender;
	@SerializedName("name_with_title")
	private String nameWithTitle;
	@SerializedName("actable_id")
	private int actableId;
	@SerializedName("user_id")
	private int userId;
	@SerializedName("password_changed")
	private boolean passwordChanged;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getAvatarUrl() {
		return this.avatarUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public String getThumbUrl() {
		return this.thumbUrl;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return this.gender;
	}

	public void setNameWithTitle(String nameWithTitle) {
		this.nameWithTitle = nameWithTitle;
	}

	public String getNameWithTitle() {
		return this.nameWithTitle;
	}

	public void setActableId(int actableId) {
		this.actableId = actableId;
	}

	public int getActableId() {
		return this.actableId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setPasswordChanged(boolean passwordChanged) {
		this.passwordChanged = passwordChanged;
	}

	public boolean getPasswordChanged() {
		return this.passwordChanged;
	}


	public static Teachers create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Teachers.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}