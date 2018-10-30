package trianglz.managers.api;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import trianglz.managers.network.HandleResponseListener;
import trianglz.managers.network.NetworkManager;
import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class UserManager {

    public static void getSchoolUrl(String url,String code, final ResponseListener responseListener) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("access-token", "");
        hashMap.put("uid", "");
        hashMap.put("client", "");
        hashMap.put("token-type", "");
        NetworkManager.getWithParameter(url+"",code+"" ,hashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }


    public static void login(String url, String email,String password, final ResponseListener responseListener) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("access-token", "");
        hashMap.put("uid", "");
        hashMap.put("client", "");
        hashMap.put("token-type", "");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_EMAIL,email);
            jsonObject.put(Constants.KEY_PASSWORD,password);
            jsonObject.put(Constants.KEY_MOBILE,true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.post(url+"",jsonObject ,hashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                responseListener.onFailure(message, errorCode);
            }
        });
    }
}
