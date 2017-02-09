package com.example.bedopedia.bedopedia_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Services.BackEnd;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        JSONObject params = new JSONObject();
        try {
            params.put("email","student@gmail.com");
            params.put("password","asdasd123");
            Log.v( "Params", params.toString());
            Toast.makeText(this, BackEnd.getInstance().post("http://192.168.6.184:3000/api/auth/sign_in", params.toString()),
                    Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
