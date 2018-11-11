package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.NewMessagePresenter;
import trianglz.core.views.NewMessageView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Student;
import trianglz.models.Subject;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class NewMessageActivity extends SuperActivity implements View.OnClickListener,NewMessagePresenter {
    private Student student;
    private ImageButton backBtn;
    private RecyclerView recyclerView;
    private NewMessageView newMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message2);
        bindViews();
        setListeners();
        getValueFromIntent();
        if(Util.isNetworkAvailable(this)){
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getCourseGroups(student.getId());
            newMessageView.getCourseGroups(url);
        }else {
            Util.showNoInternetConnectionDialog(this);
        }
    }

    private void bindViews() {
        backBtn = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_view);
        newMessageView = new NewMessageView(this,this);
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
    }

    private void getValueFromIntent() {
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onGetCourseGroupsSuccess(ArrayList<Subject> subjectArrayList) {
        progress.dismiss();
    }

    @Override
    public void onGetCourseGroupsFailure(String message, int errorCode) {
        progress.dismiss();
    }
}
