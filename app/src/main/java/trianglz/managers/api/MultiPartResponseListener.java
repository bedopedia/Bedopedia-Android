package trianglz.managers.api;

import org.json.JSONObject;

/**
 * Created by Farah A. Moniem on 08/08/2019.
 */
public interface MultiPartResponseListener {
    void onProgress(long uploaded, long total);
    void onSuccess(JSONObject response);
    void onFailure(String message,int errorCode);
}