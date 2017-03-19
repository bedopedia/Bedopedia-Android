package com.example.bedopedia.bedopedia_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import 	android.view.inputmethod.EditorInfo;

import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import android.widget.ImageView;
import Services.ApiClient;
import Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class LoginActivity extends AppCompatActivity {

    private LoginActivity context = this;
    private ApiInterface apiService;


    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setTextType();

        sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);

        apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("");

        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                onBackPressed();
            }
        });






        setSchool();


        ((EditText) findViewById(R.id.password)).setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_GO)) {
                    loginService();
                }
                return (actionId == EditorInfo.IME_ACTION_GO);
            }
        });

        ((Button) findViewById(R.id.loginSubmit)).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  loginService();

              }
        });

        ((View) findViewById(R.id.forget_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(i);
            }
        });


    }



    private void setSchool () {
        String schoolData = sharedPreferences.getString("school_data" , "");
        JsonParser parser = new JsonParser();
        JsonObject school_data = parser.parse(schoolData).getAsJsonObject();
        String schoolName = school_data.get("name").getAsString();
        String schoolAvatar = school_data.get("avatar_url").getAsString();
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText(schoolName);

        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        Picasso.with(context)
                .load(schoolAvatar)
                .error(R.drawable.logo_icon)
                .into(imageView);
    }

    public void updateToken() throws JSONException {
        final SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
        if (!sharedPreferences.getString("token_changed","").equals("True")) {
            return;
        }
        String id = sharedPreferences.getString("user_id", "");
        String url = "api/users/" + id  ;
        String token = sharedPreferences.getString("token","");
        JsonObject params = new JsonObject();
        JsonObject tokenJson = new JsonObject();
        tokenJson.addProperty("mobile_device_token",token);
        params.add("user",tokenJson);
        Call<JsonObject> call = apiService.putServise(url,  params);

        call.enqueue(new Callback<JsonObject >() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token_changed","False");

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token_changed","True");
            }

        });

    }


     private void loginService () {
        String email = ((AutoCompleteTextView)findViewById(R.id.email)).getText().toString();
        String password = ((AutoCompleteTextView)findViewById(R.id.password)).getText().toString();

        if(!validate(email,password)){
            return;
        }

        Map<String,String> params = new HashMap();
        params.put("email",email);
        params.put("password",password);
        String url = "api/auth/sign_in";

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

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("header_access-token", accessToken);
                    editor.putString("header_token-type", tokenType);
                    editor.putString("header_client", clientCode);
                    editor.putString("header_uid", uid);

                    editor.putString("user_id", userId);
                    editor.putString("is_logged_in", "true");
                    editor.putString("id", id);
                    editor.putString("username", username);
                    editor.putString("user_data", data.toString());
                    editor.commit();
                    try {
                        updateToken();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent i =  new Intent(getApplicationContext(), MyKidsActivity.class);
                    startActivity(i);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"connection failed",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public boolean validate(String email, String password) {
        boolean valid = true;


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ((AutoCompleteTextView)findViewById(R.id.email)).setError("Enter a valid email address");
            valid = false;
        } else {
            ((AutoCompleteTextView)findViewById(R.id.email)).setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            ((AutoCompleteTextView)findViewById(R.id.password)).setError("Enter a valid password");
            valid = false;
        } else {
            ((AutoCompleteTextView)findViewById(R.id.password)).setError(null);
        }

        return valid;
    }

    private void setTextType() {
        Typeface robotoMedian = Typeface.createFromAsset(this.getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(this.getAssets(), "font/Roboto-Regular.ttf");
        ((TextView)findViewById(R.id.head_text)).setTypeface(robotoMedian);
        ((Button)findViewById(R.id.loginSubmit)).setTypeface(robotoMedian);
        ((AutoCompleteTextView)findViewById(R.id.email)).setTypeface(robotoRegular);
        ((AutoCompleteTextView)findViewById(R.id.password)).setTypeface(robotoRegular);
        ((TextView)findViewById(R.id.forget_password)).setTypeface(robotoRegular);
    }

}
