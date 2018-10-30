package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import trianglz.core.presenters.SchoolLoginPresenter;
import trianglz.core.views.SchoolLoginView;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.School;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class SchoolLoginActivity extends AppCompatActivity implements View.OnClickListener,SchoolLoginPresenter {
    private EditText schoolNameEditText;
    private Button verifyBtn;
    private SchoolLoginView schoolLoginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_login);
        bindViews();
        setListeners();
    }

    private void bindViews() {
        schoolNameEditText = findViewById(R.id.et_school_name);
        verifyBtn = findViewById(R.id.btn_login);
        schoolLoginView = new SchoolLoginView(this,this);
    }

    private void setListeners(){
        verifyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                if(Util.isNetworkAvailable(this)){
                    schoolLoginView.getSchoolUrl(ApiEndPoints.SCHOOL_CODE_BASE_URL,schoolNameEditText.getText().toString());
                }
                break;
        }
    }

    @Override
    public void onGetSchoolUrlSuccess(String url) {
        url = url + "/api/get_school_by_code";
        schoolLoginView.getSchoolData(url, schoolNameEditText.getText().toString());
    }

    @Override
    public void onGetSchoolUrlFailure() {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetSchoolDataSuccess(School school) {
        Intent myIntent = new Intent(SchoolLoginActivity.this, LoginActivity.class);
        myIntent.putExtra(Constants.SCHOOL, school);
        SchoolLoginActivity.this.startActivity(myIntent);
    }

    @Override
    public void onGetSchoolDataFailure() {

    }
}
