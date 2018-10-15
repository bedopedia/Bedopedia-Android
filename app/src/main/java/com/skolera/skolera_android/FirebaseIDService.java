package com.skolera.skolera_android;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ali on 27/02/17.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    String id;
    Context context;
    String token;
    String tokenKey = "token";
    String token_changedKey = "token_changed";
    String TrueKey = "True";


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        token = refreshedToken;
//        SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(tokenKey,token);
//        editor.putString(token_changedKey,TrueKey);
//        editor.commit();



        // TODO: Implement this method to send any registration to your app's servers.

//        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}