package trianglz.models;//
//  Image.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 30, 2019
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Serializable {

	@SerializedName("url")
	public String url;


	public static Image create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, Image.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}