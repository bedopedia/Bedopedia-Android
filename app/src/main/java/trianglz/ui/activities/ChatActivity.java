package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;
import com.vanniktech.emoji.EmojiEditText;

import java.util.ArrayList;

import trianglz.managers.SessionManager;
import trianglz.models.Message;
import trianglz.models.MessageThread;
import trianglz.ui.adapters.ChatAdapter;
import trianglz.utils.Constants;

public class ChatActivity extends SuperActivity implements View.OnClickListener {
    private MessageThread messageThread;
    private TextView chatHeaderTextView;
    private ImageButton backBtn;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private EmojiEditText messageEditText;
    private Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void getValueFromIntent() {
        messageThread =(MessageThread) getIntent().getBundleExtra(Constants.KEY_BUNDLE)
                .getSerializable(Constants.KEY_MESSAGES);
        messageThread.reverseMessagesOrder();
    }

    private void bindViews(){
        chatHeaderTextView = findViewById(R.id.tv_chat_header);
        chatHeaderTextView.setText(messageThread.otherNames);
        backBtn = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_view);
        chatAdapter = new ChatAdapter(this, SessionManager.getInstance().getUserId());
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        chatAdapter.addData(getAdapterData(messageThread.messageArrayList));
        messageEditText = findViewById(R.id.et_message);
        sendBtn = findViewById(R.id.enter_chat1);
    }
    private void setListeners(){
        backBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.enter_chat1:
                if(isMessageValid()){

                }
                break;
        }
    }

    private ArrayList<Object> getAdapterData(ArrayList<Message> messageArrayList){

        ArrayList<Object> messageObjectArrayList = new ArrayList<>();
        messageObjectArrayList.addAll(messageArrayList);
        return messageObjectArrayList;
    }
    private boolean isMessageValid(){
        return !messageEditText.getText().toString().isEmpty();
    }

    private void sendMessage(String message){

    }
}
