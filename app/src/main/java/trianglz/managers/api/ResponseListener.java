package trianglz.managers.api;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public interface ResponseListener {
    void onSuccess(JSONObject response);
    void onFailure(String message,int errorCode);
}
