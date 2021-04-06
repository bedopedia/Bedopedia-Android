package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class AnnouncementReceivers {

	@SerializedName("id")
	private int id;
	@SerializedName("announcement_id")
	private int announcementId;
	@SerializedName("user_type")
	private String userType;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setAnnouncementId(int announcementId) {
		this.announcementId = announcementId;
	}

	public int getAnnouncementId() {
		return this.announcementId;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserType() {
		return this.userType;
	}


	public static AnnouncementReceivers create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, AnnouncementReceivers.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}