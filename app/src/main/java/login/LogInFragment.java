package login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import Tools.ImageViewHelper;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import myKids.MyKidsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

    private ApiInterface apiService;
    private SharedPreferences sharedPreferences;


    public LogInFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LogInFragment.
     */
    public static LogInFragment newInstance() {
        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);


        setTextType(rootView);

        sharedPreferences = this.getActivity().getSharedPreferences("cur_user", MODE_PRIVATE);

        apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);





        setSchool(rootView);


        ((EditText) rootView.findViewById(R.id.password)).setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_GO)) {
                    loginService(rootView);
                }
                return (actionId == EditorInfo.IME_ACTION_GO);
            }
        });

        ((Button) rootView.findViewById(R.id.loginSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginService(rootView);

            }
        });

        ((View) rootView.findViewById(R.id.forget_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }



    private void setSchool (View rootView) {
        String schoolData = sharedPreferences.getString("school_data" , "");
        JsonParser parser = new JsonParser();
        JsonObject school_data = parser.parse(schoolData).getAsJsonObject();
        String schoolName = school_data.get("name").getAsString();
        String schoolAvatar = school_data.get("avatar_url").getAsString();
        TextView actionBarTitle = (TextView) rootView.findViewById(R.id.action_bar_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(schoolName);


        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView1);
        ImageViewHelper.getImageFromUrlWithIdFailure(getActivity(),schoolAvatar,imageView,R.drawable.logo_icon);

    }

    public void updateToken() throws JSONException {
        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("cur_user", MODE_PRIVATE);
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


    private void loginService (View rootView) {
        final String email = ((AutoCompleteTextView)rootView.findViewById(R.id.email)).getText().toString();
        String password = ((AutoCompleteTextView)rootView.findViewById(R.id.password)).getText().toString();

        if(!validate(email,password, rootView)){
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
                    Toast.makeText(getActivity(),errorText,Toast.LENGTH_SHORT).show();
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
                    editor.putString("email", email);
                    editor.putString("avatar_url", data.get("avatar_url").getAsString());
                    editor.putString("user_data", data.toString());

                    editor.commit();
                    try {
                        updateToken();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent i =  new Intent(getActivity(), MyKidsActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getActivity(),"connection failed",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public boolean validate(String email, String password, View rootView) {
        boolean valid = true;


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ((AutoCompleteTextView) rootView.findViewById(R.id.email)).setError("Enter a valid email address");
            valid = false;
        } else {
            ((AutoCompleteTextView) rootView.findViewById(R.id.email)).setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            ((AutoCompleteTextView) rootView.findViewById(R.id.password)).setError("Enter a valid password");
            valid = false;
        } else {
            ((AutoCompleteTextView) rootView.findViewById(R.id.password)).setError(null);
        }

        return valid;
    }

    private void setTextType(View rootView) {
        Typeface robotoMedian = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        ((TextView) rootView.findViewById(R.id.head_text)).setTypeface(robotoMedian);
        ((Button) rootView.findViewById(R.id.loginSubmit)).setTypeface(robotoMedian);
        ((AutoCompleteTextView) rootView.findViewById(R.id.email)).setTypeface(robotoRegular);
        ((AutoCompleteTextView) rootView.findViewById(R.id.password)).setTypeface(robotoRegular);
        ((TextView) rootView.findViewById(R.id.forget_password)).setTypeface(robotoRegular);
    }






}
