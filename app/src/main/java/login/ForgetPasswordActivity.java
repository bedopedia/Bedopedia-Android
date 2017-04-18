package login;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import login.Services.ApiClient;
import login.Services.ApiInterface;
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

        setTextType();




        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Forget Password");




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

    private void setTextType() {
        Typeface robotoMedian = Typeface.createFromAsset(this.getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(this.getAssets(), "font/Roboto-Regular.ttf");
        ((TextView)findViewById(R.id.head_text)).setTypeface(robotoMedian);
        ((Button)findViewById(R.id.request_btn)).setTypeface(robotoMedian);
        ((AutoCompleteTextView)findViewById(R.id.email)).setTypeface(robotoRegular);
    }


    void onSuccess () {
        Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
    }

    void onFailed () {
        Toast.makeText(getApplicationContext(),"not valid email",Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
