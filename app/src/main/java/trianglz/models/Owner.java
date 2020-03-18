package trianglz.models;//
//  Owner.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 17, 2019
//

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Owner implements Serializable, Parcelable {

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.firstname);
		dest.writeString(this.lastname);
		dest.writeString(this.avatarUrl);
		dest.writeString(this.thumbUrl);
		dest.writeString(this.userType);
		dest.writeString(this.name);
		dest.writeString(this.gender);
		dest.writeString(this.nameWithTitle);
		dest.writeInt(this.actableId);
		dest.writeInt(this.userId);
		dest.writeByte(this.passwordChanged ? (byte) 1 : (byte) 0);
	}

	public Owner() {
	}

	protected Owner(Parcel in) {
		this.id = in.readInt();
		this.firstname = in.readString();
		this.lastname = in.readString();
		this.avatarUrl = in.readString();
		this.thumbUrl = in.readString();
		this.userType = in.readString();
		this.name = in.readString();
		this.gender = in.readString();
		this.nameWithTitle = in.readString();
		this.actableId = in.readInt();
		this.userId = in.readInt();
		this.passwordChanged = in.readByte() != 0;
	}

	public static final Parcelable.Creator<Owner> CREATOR = new Parcelable.Creator<Owner>() {
		@Override
		public Owner createFromParcel(Parcel source) {
			return new Owner(source);
		}

		@Override
		public Owner[] newArray(int size) {
			return new Owner[size];
		}
	};
}