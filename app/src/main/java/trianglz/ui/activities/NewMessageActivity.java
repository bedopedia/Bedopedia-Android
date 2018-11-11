package trianglz.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.core.presenters.NewMessagePresenter;
import trianglz.core.views.NewMessageView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Student;
import trianglz.models.Subject;
import trianglz.ui.adapters.NewMessageAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class NewMessageActivity extends SuperActivity implements View.OnClickListener,
        NewMessagePresenter,NewMessageAdapter.NewMessageAdapterInterface {
    private Student student;
    private ImageButton backBtn;
    private RecyclerView recyclerView;
    private NewMessageView newMessageView;
    private NewMessageAdapter newMessageAdapter;
    private TextView subjectHeaderTextView,teacherHeaderTextView;

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
        newMessageAdapter = new NewMessageAdapter(this,this);
        recyclerView.setAdapter(newMessageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        subjectHeaderTextView = findViewById(R.id.tv_header_subject);
        teacherHeaderTextView = findViewById(R.id.tv_header_teacher);
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
        newMessageAdapter.addData(subjectArrayList);
        progress.dismiss();
    }

    @Override
    public void onGetCourseGroupsFailure(String message, int errorCode) {
        progress.dismiss();
    }

    @Override
    public void onSubjectSelected(int position) {
        if(newMessageAdapter.mDataList.get(position).courseName.equals(subjectHeaderTextView.getText().toString())){
            teacherHeaderTextView.setVisibility(View.GONE);
            subjectHeaderTextView.setText(getResources().getString(R.string.select_a_course));
        }else {
            subjectHeaderTextView.setText(newMessageAdapter.mDataList.get(position).courseName);
            teacherHeaderTextView.setVisibility(View.VISIBLE);
        }
    }
}
