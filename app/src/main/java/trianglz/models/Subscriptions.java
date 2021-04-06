package trianglz.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Subscriptions {

	@SerializedName("id")
	private int id;
	@SerializedName("subscriber_id")
	private int subscriberId;
	@SerializedName("subscriber_type")
	private String subscriberType;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
	}

	public int getSubscriberId() {
		return this.subscriberId;
	}

	public void setSubscriberType(String subscriberType) {
		this.subscriberType = subscriberType;
	}

	public String getSubscriberType() {
		return this.subscriberType;
	}


	public static Subscriptions create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Subscriptions.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}