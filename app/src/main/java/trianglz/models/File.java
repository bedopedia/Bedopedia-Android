
package trianglz.models;
/**
 * Created by ${Aly} on 4/22/2019.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class File {

	@SerializedName("url")
	private Object url;

	public void setUrl(Object url) {
		this.url = url;
	}

	public Object getUrl() {
		return this.url;
	}


	public static File create(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, File.class);
	}

	public String toString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

}