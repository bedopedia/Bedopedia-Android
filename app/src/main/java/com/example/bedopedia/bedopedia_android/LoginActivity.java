package com.example.bedopedia.bedopedia_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import Services.ApiClient;
import Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
        String authToken = sharedPreferences.getString("access-token", "");
        String userId = sharedPreferences.getString("user_id", "");
        String username = sharedPreferences.getString("username", "");
        if(!(authToken.equals("") || userId.equals("") || username.equals(""))) {
            Intent i =  new Intent(getApplicationContext(), MyKidsActivity.class);
//            startActivity(i);
        }
    }


    public void loginService (View view) {

        String email = ((AutoCompleteTextView)findViewById(R.id.email)).getText().toString();
        String password = ((AutoCompleteTextView)findViewById(R.id.password)).getText().toString();

        Map<String,String> params = new HashMap();
        params.put("email",email);
        params.put("password",password);
        String url = "api/auth/sign_in";

        SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        Call<JsonObject> call = apiService.postServise(url, params);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int statusCode = response.code();
                if(statusCode == 401) {
                    String errorText = "wrong username or password";
                    Toast.makeText(getApplicationContext(),errorText,Toast.LENGTH_SHORT).show();
                } else if (statusCode == 200) {

                    String accessToken = response.headers().get("access-token").toString();
                    String tokenType = response.headers().get("token-type").toString();
                    String clientCode = response.headers().get("client").toString();
                    String uid = response.headers().get("uid").toString();

                    JsonObject data = response.body().get("data").getAsJsonObject();
                    String username = data.get("username").toString();
                    String userId = data.get("id").toString();
                    String id = data.get("actable_id").toString();

                    SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("header_access-token", accessToken);
                    editor.putString("header_token-type", tokenType);
                    editor.putString("header_client", clientCode);
                    editor.putString("header_uid", uid);
                    editor.putString("user_id", userId);
                    editor.putString("id", id);
                    editor.putString("username", username);
                    editor.putString("user_data", data.toString());
                    editor.commit();

                    Intent i =  new Intent(getApplicationContext(), MyKidsActivity.class);
                    startActivity(i);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"connection failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
