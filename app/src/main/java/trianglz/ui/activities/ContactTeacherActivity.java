package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.skolera.skolera_android.R;

import trianglz.models.Student;
import trianglz.utils.Constants;

public class ContactTeacherActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backBtn;
    private ImageButton newMessageBtn;
    private Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_teacher);
        bindViews();
        setListeners();
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
    }

    private void bindViews(){
        backBtn = findViewById(R.id.btn_back);
        newMessageBtn = findViewById(R.id.btn_new_message);
    }

    private void setListeners(){
        backBtn.setOnClickListener(this);
        newMessageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_back:
               onBackPressed();
               break;
           case R.id.btn_new_message:
               // TODO: 11/11/2018 openNewMessage
               break;
       }
    }
}
