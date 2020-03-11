package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
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
import trianglz.models.MessageThread;
import trianglz.models.Student;
import trianglz.models.Subject;
import trianglz.models.Teacher;
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
    private  ArrayList<Object> subjectObjectArrayList;
    private ArrayList<MessageThread> messageThreadArrayList;
    private Subject selectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message2);
        getValueFromIntent();
        bindViews();
        setListeners();
        if(Util.isNetworkAvailable(this)){
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getCourseGroups(student.id);
            newMessageView.getCourseGroups(url);
        }else {
            Util.showNoInternetConnectionDialog(this);
        }
    }

    private void bindViews() {
        backBtn = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_view);
        newMessageView = new NewMessageView(this,this);
        newMessageAdapter = new NewMessageAdapter(this,this, NewMessageAdapter.Type.SUBJECT);
        recyclerView.setAdapter(newMessageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        subjectHeaderTextView = findViewById(R.id.tv_header_subject);
        teacherHeaderTextView = findViewById(R.id.tv_header_teacher);
        subjectObjectArrayList = new ArrayList<>();

    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        subjectHeaderTextView.setOnClickListener(this);
    }

    private void getValueFromIntent() {
        student = Student.create(getIntent().getStringExtra(Constants.STUDENT));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.tv_header_subject:
                if(subjectHeaderTextView.getText().toString() != getResources().getString(R.string.select_a_course)){
                    newMessageAdapter.subjectName = "";
                    teacherHeaderTextView.setVisibility(View.GONE);
                    subjectHeaderTextView.setText(getResources().getString(R.string.select_a_course));
                    subjectHeaderTextView.setTextColor(getResources().getColor(R.color.warm_grey));
                    newMessageAdapter.addData(subjectObjectArrayList, NewMessageAdapter.Type.SUBJECT);
                }
                break;
        }
    }

    @Override
    public void onGetCourseGroupsSuccess(ArrayList<Subject> subjectArrayList) {
        subjectObjectArrayList.addAll(subjectArrayList);
        newMessageAdapter.addData(subjectObjectArrayList, NewMessageAdapter.Type.SUBJECT);
        progress.dismiss();
    }

    @Override
    public void onGetCourseGroupsFailure(String message, int errorCode) {
        if(progress.isShowing()){
            progress.dismiss();
        }
        showErrorDialog(this, errorCode,"");
    }

    @Override
    public void onSubjectSelected(int position) {
        if(newMessageAdapter.type.equals(NewMessageAdapter.Type.SUBJECT)) {
            if (((Subject) newMessageAdapter.mDataList.get(position)).courseName.equals(subjectHeaderTextView.getText().toString())) {
                newMessageAdapter.subjectName = "";
                teacherHeaderTextView.setVisibility(View.GONE);
                subjectHeaderTextView.setText(getResources().getString(R.string.select_a_course));
                subjectHeaderTextView.setTextColor(getResources().getColor(R.color.warm_grey));
                newMessageAdapter.addData(subjectObjectArrayList, NewMessageAdapter.Type.SUBJECT);
            } else {
                selectedSubject = ((Subject) newMessageAdapter.mDataList.get(position));
                subjectHeaderTextView.setText(((Subject) newMessageAdapter.mDataList.get(position)).courseName);
                teacherHeaderTextView.setVisibility(View.VISIBLE);
                subjectHeaderTextView.setTextColor(getResources().getColor(R.color.gunmetal));
                newMessageAdapter.subjectName = ((Subject) newMessageAdapter.mDataList.get(position)).courseName;
                ArrayList<Object> teacherObjectArrayList = new ArrayList<>();
                teacherObjectArrayList.addAll(((Subject) newMessageAdapter.mDataList.get(position)).teacherArrayList);
                newMessageAdapter.addData(teacherObjectArrayList, NewMessageAdapter.Type.TEACHER);

            }
        }

    }

    @Override
    public void onTeacherSelected(int position) {
        Teacher teacher = (Teacher) newMessageAdapter.mDataList.get(position);
        openChatActivity(teacher.id,selectedSubject.id);
    }


    private void openChatActivity(int teacherId, int courseId) {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra(Constants.KEY_TEACHER_ID,teacherId);
        intent.putExtra(Constants.KEY_COURSE_ID,courseId);
        startActivity(intent);
    }
}
