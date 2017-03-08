package com.example.bedopedia.bedopedia_android;

import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import Services.ApiClient;
import Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class schoolCode extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private final String schoolApiUrl = "http://192.168.6.150:3000/";
    private final String path = "schools/get_by_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_code);

        sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);



        if(checkAuthenticate()) {
            Intent i =  new Intent(getApplicationContext(), MyKidsActivity.class);
            startActivity(i);
            finish();
        }

        ((Button) findViewById(R.id.codeSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSchoolCode();

            }
        });

        ((EditText) findViewById(R.id.code)).setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    getSchoolCode();
                }
                return false;
            }
        });
    }

    private void getSchoolCode () {
        String code = ((AutoCompleteTextView)findViewById(R.id.code)).getText().toString();
        if(code.length() < 1){
            ((AutoCompleteTextView)findViewById(R.id.code)).setError("Enter a valid school code");
            return;
        }

        ApiClient.BASE_URL = schoolApiUrl;
        SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);

        Map<String,String> params = new HashMap<>();
        params.put("code", code);
        Call<JsonObject> call = apiService.getServise(path, params);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int statusCode = response.code();
                if(statusCode == 401) {
                    String errorText = "Not correct School Code";
                    Toast.makeText(getApplicationContext(),errorText,Toast.LENGTH_SHORT).show();
                } else if (statusCode == 200) {
                    String url = response.body().get("url").getAsString();
                    setSchool(url);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"connection failed",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private  void setSchool (final String schoolUrl) {

        ApiClient.BASE_URL = schoolUrl;
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);

        Map<String,String> params = new HashMap();
        String url = "/api/schools/1";

        Call<JsonObject> call = apiService.getServise(url, params);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int statusCode = response.code();
                if(statusCode == 401) {
                    String errorText = "wrong username or password";
                    Toast.makeText(getApplicationContext(),errorText,Toast.LENGTH_SHORT).show();
                } else if (statusCode == 200) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("school_data", response.body().toString());
                    editor.putString("Base_Url", schoolUrl);
                    editor.commit();

                    Intent i =  new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"connection failed",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private Boolean checkAuthenticate() {

        String baseUrl = sharedPreferences.getString("Base_Url", "");
        String authToken = sharedPreferences.getString("header_access-token", "");
        String userData = sharedPreferences.getString("user_data", "");
        String uid = sharedPreferences.getString("header_uid", "");

        return !(authToken.equals("") || userData.equals("") || uid.equals("") || baseUrl.equals(""));
    }

}
