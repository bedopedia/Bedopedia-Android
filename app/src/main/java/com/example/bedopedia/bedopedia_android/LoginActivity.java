package com.example.bedopedia.bedopedia_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import Services.ApiClient;
import Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

    }


    public void loginService (View view) {

        String email = ((AutoCompleteTextView)findViewById(R.id.email)).getText().toString();
        String password = ((AutoCompleteTextView)findViewById(R.id.password)).getText().toString();

        Map<String,String> params = new HashMap();
        params.put("email",email);
        params.put("password",password);
        String url = "api/auth/sign_in";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.postServise(url, params);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int statusCode = response.code();
                if(statusCode == 401) {
                    String errorText = "wrong username or password";
                    Toast.makeText(getApplicationContext(),errorText,Toast.LENGTH_SHORT).show();
                } else if (statusCode == 200) {
                    String token = response.headers().get("access-token").toString();
                    JsonElement body = response.body().get("data");
                    String username = body.getAsJsonObject().get("username").toString();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}
