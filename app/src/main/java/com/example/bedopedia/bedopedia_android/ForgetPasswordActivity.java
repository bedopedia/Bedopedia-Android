package com.example.bedopedia.bedopedia_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.ImageButton;
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


public class ForgetPasswordActivity extends AppCompatActivity {


    private SharedPreferences sharedPreferences;
    private AutoCompleteTextView mEmailView;
    private Button requestBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Forget password");
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });




        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        requestBtn = (Button) findViewById(R.id.request_btn);
        sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);

        requestBtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       resetPasswordRequest();
                   }
               }
        );
    }

    private void resetPasswordRequest() {

        if(!validate()) {
            onFailed();
            return ;
        }





        String email = mEmailView.getText().toString();
        Map<String,String> params = new HashMap();
        params.put("email",email);
        params.put("redirect_url", ApiClient.BASE_URL + "reset_password");
        String url = "/api/auth/password";

        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        Call<JsonObject> call = apiService.postServise(url, params);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int statusCode = response.code();
                if(statusCode == 404) {
                    onFailed();
                } else if (statusCode == 200) {
                    onSuccess();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"connection failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    Boolean validate () {
        String email = mEmailView.getText().toString();
        Boolean valid = true;
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailView.setError("invalid email");
            mEmailView.requestFocus();
            valid = false;
        }
        return valid;
    }


    void onSuccess () {
        Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
    }

    void onFailed () {
        Toast.makeText(getApplicationContext(),"not valid email",Toast.LENGTH_SHORT).show();
    }
}
