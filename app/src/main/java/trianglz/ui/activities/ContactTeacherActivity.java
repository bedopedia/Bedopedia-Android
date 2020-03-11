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

import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.ContactTeacherPresenter;
import trianglz.core.views.ContactTeacherView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Actor;
import trianglz.models.Message;
import trianglz.models.MessageThread;
import trianglz.models.Student;
import trianglz.ui.adapters.ContactTeacherAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class ContactTeacherActivity extends SuperActivity implements View.OnClickListener,
        ContactTeacherPresenter, ContactTeacherAdapter.ContactTeacherAdapterInterface {
    private ImageButton backBtn;
    private ImageButton newMessageBtn;
    private Student student;
    private ContactTeacherView contactTeacherView;
    private RecyclerView recyclerView;
    private ContactTeacherAdapter contactTeacherAdapter;
    private ArrayList<MessageThread> messageThreadArrayList;
    private Actor actor;
    private TextView headerTextView;
    private boolean isOpeningThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_teacher);
        bindViews();
        setListeners();
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
        } else {
            actor = (Actor) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.KEY_ACTOR);
        }

    }

    private void bindViews() {
        backBtn = findViewById(R.id.btn_back);
        newMessageBtn = findViewById(R.id.btn_new_message);
        contactTeacherView = new ContactTeacherView(this, this);
        recyclerView = findViewById(R.id.recycler_view);
        contactTeacherAdapter = new ContactTeacherAdapter(this, this);
        recyclerView.setAdapter(contactTeacherAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, this), false));
        messageThreadArrayList = new ArrayList<>();
        headerTextView = findViewById(R.id.tv_header);
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            headerTextView.setText(getResources().getString(R.string.contact_teacher));
            newMessageBtn.setVisibility(View.VISIBLE);
        } else {
            headerTextView.setText(getResources().getString(R.string.messages));
            newMessageBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        newMessageBtn.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Util.isNetworkAvailable(this)) {
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getThreads();
            contactTeacherView.getMessages(url, SessionManager.getInstance().getId());
        } else {
            Util.showNoInternetConnectionDialog(this);
        }
        isOpeningThread = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_new_message:
                if (Util.isNetworkAvailable(this)) {
                    openNewMessageActivity();
                } else {
                    Util.showNoInternetConnectionDialog(this);
                }
                break;
        }
    }

    @Override
    public void onGetMessagesSuccess(ArrayList<MessageThread> messageThreadArrayList) {
        this.messageThreadArrayList = messageThreadArrayList;
        contactTeacherAdapter.addData(messageThreadArrayList);
        progress.dismiss();
    }

    @Override
    public void onGetMessagesFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        showErrorDialog(this, errorCode, "");
    }

    @Override
    public void onGetSingleThreadSuccess(ArrayList<Message> messages, int position) {
        MessageThread messageThread = contactTeacherAdapter.mDataList.get(position);
        messageThread.messageArrayList.clear();
        messageThread.messageArrayList.addAll(messages);
        openChatActivity(messageThread);
    }

    @Override
    public void onGetSingleThreadFailure(String message, int errorCode) {
        showErrorDialog(this, errorCode, "");
    }

    @Override
    public void onThreadClicked(int position) {
        if (!isOpeningThread) {
            if (Util.isNetworkAvailable(this)) {
                isOpeningThread = true;
                MessageThread messageThread = contactTeacherAdapter.mDataList.get(position);
                String url = SessionManager.getInstance().getBaseUrl() +ApiEndPoints.getSingleThread(messageThread.id);
                contactTeacherView.getSingleThread(url,position);
            } else {
                Util.showNoInternetConnectionDialog(this);
            }

        }


    }

    private void openChatActivity(MessageThread messageThread) {
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_MESSAGES, messageThread);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);
    }

    private void openNewMessageActivity() {
        Intent intent = new Intent(this, NewMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOpeningThread = false;
    }
}
