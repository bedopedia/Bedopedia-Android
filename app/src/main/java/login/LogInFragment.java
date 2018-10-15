package login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skolera.skolera_android.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import Tools.ImageViewHelper;
import Tools.SharedPreferenceUtils;
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
    final String schoolDataKey = "school_data";
    final String nameKey = "name";
    final String tokenChangedKey = "token_changed";
    final String headerAccessTokenKey = "header_access-token";
    final String headerTokenTypeKey = "header_token-type";
    final String headerClientKey = "header_client";
    final String headerUidKey = "header_uid";
    final String userIdKey = "user_id";
    final String isLoggedInKey = "is_logged_in";
    final String idKey = "id";
    final String usernameKey = "username";
    final String emailKey = "email";
    final String avatarUrlKey = "avatar_url";
    final String userDataKey = "user_data";
    final String curUserKey = "cur_user";
    ProgressDialog progress;


    String token;
    String tokenKey = "token";
    String token_changedKey = "token_changed";
    String TrueKey = "True";


    public void loading(){
        progress.setTitle(R.string.LoadDialogueTitle);
        progress.setMessage(getString(R.string.LoadDialogueBody));
    }


    public LogInFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LogInFragment.
     */
    public static LogInFragment newInstance() {
        LogInFragment loginFragment = new LogInFragment();
        Bundle args = new Bundle();
        loginFragment.setArguments(args);
        return loginFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = SharedPreferenceUtils.getSharedPreference(this.getActivity(),curUserKey);
        apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        progress = new ProgressDialog(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTextType(view);
        setSchool(view);
        ((EditText) view.findViewById(R.id.password_edit_text)).setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_GO)) {
                    loginService(view);
                }
                return (actionId == EditorInfo.IME_ACTION_GO);
            }
        });

        ((Button) view.findViewById(R.id.login_submit_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                progress.show();
                loginService(view);

            }
        });

        ((View) view.findViewById(R.id.forget_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    private void setSchool (View rootView) {
        String schoolData = SharedPreferenceUtils.getStringValue(schoolDataKey,"",sharedPreferences);
        JsonParser parser = new JsonParser();
        JsonObject school_data = parser.parse(schoolData).getAsJsonObject();
        String schoolName = school_data.get(nameKey).getAsString();
        String schoolAvatar = school_data.get(avatarUrlKey).getAsString();
        TextView actionBarTitle = (TextView) rootView.findViewById(R.id.action_bar_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(schoolName);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.dragon_icon);
        ImageViewHelper.getImageFromUrlWithIdFailure(getActivity(),schoolAvatar,imageView,R.drawable.logo_icon);

    }

    public void updateToken() throws JSONException {
        final SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(this.getActivity(),"cur_user");
        if (!sharedPreferences.getString(tokenChangedKey,"").equals("True")) {
            return;
        }
        String id = SharedPreferenceUtils.getStringValue(userIdKey,"",sharedPreferences);
        String url = "api/users/" + id  ;
        String token = SharedPreferenceUtils.getStringValue("token","",sharedPreferences);
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        JsonObject params = new JsonObject();
        JsonObject tokenJson = new JsonObject();
        tokenJson.addProperty("mobile_device_token",token);
        params.add("user",tokenJson);
        Call<JsonObject> call = apiService.putServise(url,  params);

        call.enqueue(new Callback<JsonObject >() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(tokenChangedKey,"False");
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progress.dismiss();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(tokenChangedKey,"True");
            }

        });

    }


    private void loginService (View rootView) {
        final String email = ((AutoCompleteTextView)rootView.findViewById(R.id.email)).getText().toString();
        String password = ((AutoCompleteTextView)rootView.findViewById(R.id.password_edit_text)).getText().toString();

        if(!validate(email,password, rootView)){
            return;
        }

        Map<String,Object> params = new HashMap();
        params.put("email",email);
        params.put("password",password);
        String url = "api/auth/sign_in";

        Call<JsonObject> call = apiService.postServise(url, params);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int statusCode = response.code();
                progress.dismiss();

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
                    editor.putString(headerAccessTokenKey, accessToken);
                    editor.putString(headerTokenTypeKey, tokenType);
                    editor.putString(headerClientKey, clientCode);
                    editor.putString(headerUidKey, uid);
                    editor.putString(userIdKey, userId);
                    editor.putString(isLoggedInKey, "true");
                    editor.putString(idKey, id);
                    editor.putString(usernameKey, username);
                    editor.putString(emailKey, email);
                    editor.putString(avatarUrlKey, data.get("avatar_url").getAsString());
                    editor.putString(userDataKey, data.toString());
                    editor.commit();
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(),  new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String token = instanceIdResult.getToken();
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cur_user", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(tokenKey,token);
                                editor.putString(token_changedKey,TrueKey);
                                editor.commit();
                                try {
                                    updateToken();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    Intent i =  new Intent(getActivity(), MyKidsActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }



            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progress.dismiss();

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
            ((AutoCompleteTextView) rootView.findViewById(R.id.password_edit_text)).setError("Enter a valid password");
            valid = false;
        } else {
            ((AutoCompleteTextView) rootView.findViewById(R.id.password_edit_text)).setError(null);
        }

        return valid;
    }

    private void setTextType(View rootView) {
        Typeface robotoMedian = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        ((TextView) rootView.findViewById(R.id.head_text)).setTypeface(robotoMedian);
        ((Button) rootView.findViewById(R.id.login_submit_button)).setTypeface(robotoMedian);
        ((AutoCompleteTextView) rootView.findViewById(R.id.email)).setTypeface(robotoRegular);
        ((AutoCompleteTextView) rootView.findViewById(R.id.password_edit_text)).setTypeface(robotoRegular);
        ((TextView) rootView.findViewById(R.id.forget_password)).setTypeface(robotoRegular);
        setText(rootView);
    }


    private void setText(View rootView){
        ((TextView) rootView.findViewById(R.id.head_text)).setText(R.string.fragmentLoginMessage_tv);
        ((Button) rootView.findViewById(R.id.login_submit_button)).setText(R.string.fragmentLogin_btn);
        ((AutoCompleteTextView) rootView.findViewById(R.id.email)).setHint(R.string.fragmentLoginMail_tv);
        ((AutoCompleteTextView) rootView.findViewById(R.id.password_edit_text)).setHint(R.string.fragmentLoginPassword_tv);
        ((TextView) rootView.findViewById(R.id.forget_password)).setText(R.string.fragmentLoginForgetPassword_tv);
        ((TextView) rootView.findViewById(R.id.forget_password)).setVisibility(View.INVISIBLE);
    }
}
