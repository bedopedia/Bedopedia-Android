package trianglz.models;//
//  Image.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 30, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Image {

	@SerializedName("url")
	private String url;

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getUrl() {
		return this.url;
	}


	public static Image create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Image.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}