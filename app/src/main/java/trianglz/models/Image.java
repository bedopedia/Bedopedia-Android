package trianglz.models;//
//  Image.java
//
//  Generated using https://jsonmaster.github.io
//  Created on July 30, 2019
//

import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Image {

	@SerializedName("url")
	private Object url;

	public void setUrl(Object url) {
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