package trianglz.managers.network;

import org.json.JSONArray;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public interface HandleArrayResponseListener {
    void onSuccess(JSONArray response);
    void onFailure(String message,int errorCode);
}
