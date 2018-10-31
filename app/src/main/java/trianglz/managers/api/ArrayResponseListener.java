package trianglz.managers.api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public interface ArrayResponseListener {
    void onSuccess(JSONArray responseArray);
    void onFailure(String message,int errorCode);
}
