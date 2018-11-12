package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.models.Message;
import trianglz.models.MessageThread;
import trianglz.utils.Constants;

public class ChatActivity extends SuperActivity implements View.OnClickListener {
    private MessageThread messageThread;
    private TextView chatHeaderTextView;
    private ImageButton backBtn;
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
    }

    private void bindViews(){
        chatHeaderTextView = findViewById(R.id.tv_chat_header);
        chatHeaderTextView.setText(messageThread.otherNames);
        backBtn = findViewById(R.id.btn_back);
    }
    private void setListeners(){
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
