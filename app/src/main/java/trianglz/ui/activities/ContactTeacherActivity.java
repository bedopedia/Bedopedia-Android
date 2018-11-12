package trianglz.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;

import com.skolera.skolera_android.AskTeacherActivity;
import com.skolera.skolera_android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.ContactTeacherPresenter;
import trianglz.core.views.ContactTeacherView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.MessageThread;
import trianglz.models.Student;
import trianglz.ui.adapters.ContactTeacherAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class ContactTeacherActivity extends SuperActivity implements View.OnClickListener,
        ContactTeacherPresenter,ContactTeacherAdapter.ContactTeacherAdapterInterface{
    private ImageButton backBtn;
    private ImageButton newMessageBtn;
    private Student student;
    private ContactTeacherView contactTeacherView;
    private RecyclerView recyclerView;
    private ContactTeacherAdapter contactTeacherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_teacher);
        bindViews();
        setListeners();
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
        // TODO: 11/11/2018 ask about incase student id is equal "none"
        if(Util.isNetworkAvailable(this)){
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getThreads();
            contactTeacherView.getMessages(url,SessionManager.getInstance().getId());
        }else {
            Util.showNoInternetConnectionDialog(this);
        }
    }

    private void bindViews(){
        backBtn = findViewById(R.id.btn_back);
        newMessageBtn = findViewById(R.id.btn_new_message);
        contactTeacherView = new ContactTeacherView(this,this);
        recyclerView = findViewById(R.id.recycler_view);
        contactTeacherAdapter = new ContactTeacherAdapter(this,this);
        recyclerView.setAdapter(contactTeacherAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8,this),false));

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
               openNewMessageActivity();
               break;
       }
    }

    @Override
    public void onGetMessagesSuccess(ArrayList<MessageThread> messageThreadArrayList) {
        contactTeacherAdapter.addData(messageThreadArrayList);
        progress.dismiss();
    }

    @Override
    public void onGetMessagesFailure(String message, int errorCode) {
        progress.dismiss();
    }

    @Override
    public void onThreadClicked(int position) {
        MessageThread messageThread = contactTeacherAdapter.mDataList.get(position);
        openChatActivity(messageThread);
    }

    private void openChatActivity(MessageThread messageThread) {
        Intent intent = new Intent(this,ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_MESSAGES,messageThread);
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(intent);
    }

    private void openNewMessageActivity(){
        Intent intent = new Intent(this, NewMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT,student);
       intent.putExtra(Constants.KEY_BUNDLE,bundle);
        startActivity(intent);
    }
}
