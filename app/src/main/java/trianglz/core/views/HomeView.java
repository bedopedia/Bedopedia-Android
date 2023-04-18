package trianglz.core.views;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import trianglz.managers.SessionManager;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.utils.Util;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class HomeView {
    private Context context;

    public HomeView(Context context) {
        this.context = context;
    }

    public void refreshFireBaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                        Log.d("token", "onSuccess: " + token);
                        SessionManager.getInstance().setFireBaseToken(token);
                        updateToken();

                    }
                });


    }

    public void updateToken() {

        String url = SessionManager.getInstance().getBaseUrl() + "/api/users/"
                + SessionManager.getInstance().getUserId();
        String token = SessionManager.getInstance().getTokenKey();
        String locale = "";
        if(Util.getLocale(context).equals("ar")){
            locale = "ar";
        }else {
            locale = "en";
        }
        UserManager.updateToken(url, token,locale, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String message, int errorCode) {
            }
        });


    }


}
