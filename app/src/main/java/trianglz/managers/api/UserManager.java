package trianglz.managers.api;

import org.json.JSONObject;

import java.util.HashMap;

import trianglz.managers.network.HandleResponseListener;
import trianglz.managers.network.NetworkManager;

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
}
