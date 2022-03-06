package trianglz.managers.network;

import org.json.JSONObject;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public interface HandleResponseListener {
    void onSuccess(JSONObject response);
    void onFailure(String message,int errorCode);
}
