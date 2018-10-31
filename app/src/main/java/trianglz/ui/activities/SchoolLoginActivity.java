package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.skolera.skolera_android.R;

import trianglz.core.presenters.SchoolLoginPresenter;
import trianglz.core.views.SchoolLoginView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.School;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class SchoolLoginActivity extends SuperActivity implements View.OnClickListener,SchoolLoginPresenter {
    private MaterialEditText codeEditText;
    private Button verifyBtn;
    private SchoolLoginView schoolLoginView;
    private String schoolUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_login);
        bindViews();
        setListeners();
    }

    private void bindViews() {
        codeEditText = findViewById(R.id.et_school_name);
        verifyBtn = findViewById(R.id.btn_verify);
        schoolLoginView = new SchoolLoginView(this,this);
        schoolUrl = "";
    }

    private void setListeners(){
        verifyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_verify:
                if (validate(codeEditText.getText().toString())) {
                    if(Util.isNetworkAvailable(this)){
                        super.showLoadingDialog();
                        schoolLoginView.getSchoolUrl(ApiEndPoints.SCHOOL_CODE_BASE_URL, codeEditText.getText().toString());
                    }else {
                        Util.showNoInternetConnectionDialog(this);
                    }
                }
                break;
        }
    }
    public boolean validate(String code) {
        boolean valid = true;
        if (code.isEmpty() || code.length() < 2) {
            if (code.isEmpty()) {
                showErrorMessage(codeEditText,getResources().getString(R.string.code_is_empty));
            } else {
                showErrorMessage(codeEditText,getResources().getString(R.string.code_length_error));
            }
            valid = false;
        }

        return valid;
    }
    private void showErrorMessage(MaterialEditText editText, String message) {
        editText.setError(message);
        editText.setUnderlineColor(getResources().getColor(R.color.pale_red));
        editText.setHideUnderline(false);
        editText.setErrorColor(getResources().getColor(R.color.tomato));
    }

    @Override
    public void onGetSchoolUrlSuccess(String url) {
        schoolUrl = url;
        SessionManager.getInstance().setBaseUrl(url);
        url += "/api/get_school_by_code";
        if(Util.isNetworkAvailable(this)){
            schoolLoginView.getSchoolData(url, codeEditText.getText().toString());
        }else {
            Util.showNoInternetConnectionDialog(this);
        }

    }

    @Override
    public void onGetSchoolUrlFailure(String message, int errorCode) {
        super.progress.dismiss();
        if(errorCode == 401 || errorCode == 0){
            Util.showErrorDialog(this,getResources().getString(R.string.skolera),getResources().getString(R.string.not_correct_school_code));
        }
    }

    @Override
    public void onGetSchoolDataSuccess(School school) {
        super.progress.dismiss();
        Intent myIntent = new Intent(SchoolLoginActivity.this, LoginActivity.class);
        school.schoolUrl = schoolUrl;
        myIntent.putExtra(Constants.SCHOOL, school);
        SchoolLoginActivity.this.startActivity(myIntent);
    }

    @Override
    public void onGetSchoolDataFailure(String message,int errorCode) {
        super.progress.dismiss();
        if(errorCode == 401){
            Util.showErrorDialog(this,getResources().getString(R.string.skolera),getResources().getString(R.string.wrong_username_or_password));
        }
    }
}
