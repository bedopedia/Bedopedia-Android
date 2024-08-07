package login;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skolera.skolera_android.R;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import Tools.SharedPreferenceUtils;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgetPasswordFRagment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgetPasswordFRagment extends Fragment {


    private SharedPreferences sharedPreferences;
    private AutoCompleteTextView mEmailView;
    private Button requestBtn;
    String emailKey = "email";
    String redirectUrlKey = "redirect_url";

    public ForgetPasswordFRagment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ForgetPasswordFRagment.
     */
    public static ForgetPasswordFRagment newInstance() {
        ForgetPasswordFRagment fragment = new ForgetPasswordFRagment();
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
        final View rootView = inflater.inflate(R.layout.fragment_forget_password_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTextType(view);
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        requestBtn = (Button) view.findViewById(R.id.request_btn);
        sharedPreferences = SharedPreferenceUtils.getSharedPreference(this.getActivity(),"cur_user");

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
        Map<String,Object> params = new HashMap();
        params.put(emailKey,email);
        params.put(redirectUrlKey, ApiClient.BASE_URL + "reset_password");
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
                Toast.makeText(getActivity(),getString(R.string.ConnectionErrorTitle),Toast.LENGTH_SHORT).show();
            }
        });
    }

    Boolean validate () {
        String email = mEmailView.getText().toString();
        Boolean valid = true;
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailView.setError(getString(R.string.InvalidMail));
            mEmailView.requestFocus();
            valid = false;
        }
        return valid;
    }

    private void setTextType(View rootView) {
        Typeface robotoMedian = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        ((TextView) rootView.findViewById(R.id.head_text)).setTypeface(robotoMedian);
        ((Button) rootView.findViewById(R.id.request_btn)).setTypeface(robotoMedian);
        ((AutoCompleteTextView) rootView.findViewById(R.id.email)).setTypeface(robotoRegular);
        setText(rootView);
    }

    private void setText(View rootView){
        ((TextView) rootView.findViewById(R.id.head_text)).setText(R.string.fragmentForgetPassowdMessage_tv);
        ((Button) rootView.findViewById(R.id.request_btn)).setText(R.string.fragmentForgetPasswordResetPassword_btn);
        ((AutoCompleteTextView) rootView.findViewById(R.id.email)).setHint(R.string.fragmentForgetPasswordMail_tv);
    }

    void onSuccess () {
        Toast.makeText(getActivity(),getString(R.string.SuccessMessage),Toast.LENGTH_SHORT).show();
    }

    void onFailed () {
        Toast.makeText(getActivity(),getString(R.string.NotValidMail),Toast.LENGTH_SHORT).show();
    }
}
