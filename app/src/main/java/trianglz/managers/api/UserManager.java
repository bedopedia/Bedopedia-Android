package trianglz.managers.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import trianglz.managers.SessionManager;
import trianglz.managers.network.HandleArrayResponseListener;
import trianglz.managers.network.HandleResponseListener;
import trianglz.managers.network.NetworkManager;
import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class UserManager {

    public static void getSchoolUrl(String url, String code, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.KEY_CODE, code);
        NetworkManager.getWithParameter(url + "", paramsHashMap, headerHashMap, new HandleResponseListener() {
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


    public static void login(String url, String email, String password, final ResponseListener responseListener) {
        HashMap<String, String> hashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.KEY_EMAIL, email);
            jsonObject.put(Constants.KEY_PASSWORD, password);
            jsonObject.put(Constants.KEY_MOBILE, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkManager.post(url + "", jsonObject, hashMap, new HandleResponseListener() {
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

    public static void updateToken(String url, String token, final ResponseListener responseListener) {
        HashMap<String, String> hashMap = SessionManager.getInstance().getHeaderHashMap();
        JSONObject params = new JSONObject();
        JSONObject tokenJson = new JSONObject();
        try {
            tokenJson.put(Constants.MOBILE_DEVICE_TOKEN, token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            params.put(Constants.USER, tokenJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.put("", params, hashMap, new HandleResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                SessionManager.getInstance().setTokenChangedValue(false);
                responseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                SessionManager.getInstance().setTokenChangedValue(true);
                responseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void getStudentsHome(String url, String id, final ArrayResponseListener arrayResponseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> paramsHashMap = new HashMap<>();
        paramsHashMap.put(Constants.PARENT_ID, id);
        NetworkManager.getJsonArray(url, paramsHashMap, headerHashMap, new HandleArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                arrayResponseListener.onSuccess(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                arrayResponseListener.onFailure(message, errorCode);
            }
        });
    }

    public static void getNotifications(String url, final ResponseListener responseListener) {
        HashMap<String, String> headerHashMap = SessionManager.getInstance().getHeaderHashMap();
        HashMap<String, String> params = new HashMap<>();
        params.put("page" , "1");
        params.put("per_page" , "20");
        NetworkManager.getWithParameter(url, params, headerHashMap, new HandleResponseListener() {
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