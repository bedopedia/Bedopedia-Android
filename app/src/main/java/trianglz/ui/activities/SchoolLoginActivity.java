package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skolera.skolera_android.R;

import trianglz.core.presenters.SchoolLoginPresenter;
import trianglz.core.views.SchoolLoginView;
import trianglz.managers.api.ApiEndPoints;
import trianglz.utils.Util;

public class SchoolLoginActivity extends AppCompatActivity implements View.OnClickListener,SchoolLoginPresenter {
    private EditText schoolNameEditText;
    private Button loginBtn;
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
        loginBtn = findViewById(R.id.btn_login);
        schoolLoginView = new SchoolLoginView(this,this);
    }

    private void setListeners(){
        loginBtn.setOnClickListener(this);
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
    public void onGetSchoolUrlSuccess() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetSchoolUrlFailure() {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
    }
}
