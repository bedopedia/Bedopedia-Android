package login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

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
 * Use the {@link SchoolCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchoolCodeFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private final String schoolApiUrl = "https://bedopedia-schools.herokuapp.com/";
    private final String path = "schools/get_by_code";
    final String schoolDataKey = "school_data";
    final String BaseUrlKey = "Base_Url";
    final String headerAccessTokenKey = "header_access-token";
    final String userDataKey = "user_data";
    final String headerUidKey = "header_uid";


    public SchoolCodeFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SchoolCodeFragment.
     */
    public static SchoolCodeFragment newInstance() {
        SchoolCodeFragment schoolCodeFragment = new SchoolCodeFragment();
        Bundle args = new Bundle();
        schoolCodeFragment.setArguments(args);
        return schoolCodeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("cur_user", MODE_PRIVATE);


        if(checkAuthenticate()) {
            Intent i = new Intent(getActivity().getApplicationContext(), MyKidsActivity.class);
            startActivity(i);
            getActivity().finish();
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_school_code, container, false);

        return rootView;

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTextType(view);

        ((Button)view.findViewById(R.id.code_submit_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSchoolCode(view);

            }
        });

        ((EditText) view.findViewById(R.id.scool_code_edit_text)).setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    getSchoolCode(view);
                    return true;
                }
                return false;
            }
        });

    }

    private void getSchoolCode (View rootView) {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService (getActivity().CONNECTIVITY_SERVICE);
        if (!(conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected())) {
            Toast.makeText(getActivity().getApplicationContext(),"Check your Netwotk connection and Try again!",Toast.LENGTH_SHORT).show();
            return;
        }
        String code = ((AutoCompleteTextView)rootView.findViewById(R.id.scool_code_edit_text)).getText().toString();
        if(code.length() < 1){
            ((AutoCompleteTextView)rootView.findViewById(R.id.scool_code_edit_text)).setError("Enter a valid school code");
            return;
        }

        ApiClient.BASE_URL = schoolApiUrl;
        SharedPreferences sharedPreferences =  SharedPreferenceUtils.getSharedPreference(this.getActivity(),"cur_user");
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
                    Toast.makeText(getActivity().getApplicationContext(),errorText,Toast.LENGTH_SHORT).show();
                } else if (statusCode == 200) {
                    String url = response.body().get("url").getAsString();
                    setSchool(url);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(),"Incorrect code, try again!",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity().getApplicationContext(),errorText,Toast.LENGTH_SHORT).show();
                } else if (statusCode == 200) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(schoolDataKey, response.body().toString());
                    editor.putString(BaseUrlKey, schoolUrl);
                    editor.commit();

                    Intent i =  new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(),"School connection failed",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setTextType(View rootView) {
        Typeface robotoMedian = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");
        ((TextView)rootView.findViewById(R.id.head_text)).setTypeface(robotoMedian);
        ((Button)rootView.findViewById(R.id.code_submit_btn)).setTypeface(robotoMedian);
        ((AutoCompleteTextView)rootView.findViewById(R.id.scool_code_edit_text)).setTypeface(robotoRegular);
        ((TextView)rootView.findViewById(R.id.copy_right)).setTypeface(robotoRegular);
        setText(rootView);
    }


    private void setText(View rootView){
        ((TextView)rootView.findViewById(R.id.head_text)).setText(R.string.fragmentSchoolcodeMeesage_tv);
        ((Button)rootView.findViewById(R.id.code_submit_btn)).setText(R.string.fragmentSchoolcodeContinue_btn);
        ((AutoCompleteTextView)rootView.findViewById(R.id.scool_code_edit_text)).setHint(R.string.fragmentSchoolcodeHint_tv);
        ((TextView)rootView.findViewById(R.id.copy_right)).setText(R.string.copyRights_tv);
    }





    private Boolean checkAuthenticate() {

        String baseUrl = SharedPreferenceUtils.getStringValue(BaseUrlKey,"",sharedPreferences);
        String authToken = SharedPreferenceUtils.getStringValue(headerAccessTokenKey,"",sharedPreferences);
        String userData = SharedPreferenceUtils.getStringValue(userDataKey,"",sharedPreferences);
        String uid = SharedPreferenceUtils.getStringValue(headerUidKey,"",sharedPreferences);
        ApiClient.BASE_URL = baseUrl;
        return !(authToken.equals("") || userData.equals("") || uid.equals("") || baseUrl.equals(""));
    }
}
