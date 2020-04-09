package trianglz.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;
import com.vanniktech.emoji.EmojiEditText;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

import trianglz.components.HideKeyboardOnTouch;
import trianglz.components.OnImageSelectedListener;
import trianglz.core.presenters.ChatPresenter;
import trianglz.core.views.ChatView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Message;
import trianglz.models.MessageThread;
import trianglz.models.User;
import trianglz.ui.adapters.ChatAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class ChatActivity extends SuperActivity implements View.OnClickListener, ChatPresenter {
    private MessageThread messageThread;
    private TextView chatHeaderTextView;
    private ImageButton backBtn;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private EmojiEditText messageEditText;
    private Button sendBtn;
    private ChatView chatView;
    private int teacherId;
    private int courseId;
    private ImageButton imageBtn;
    private LinearLayout rootView, sendLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatView = new ChatView(this, this);
        getValueFromIntent();
        bindViews();
        setListeners();
    }

    private void getValueFromIntent() {
        if (getIntent().getBundleExtra(Constants.KEY_BUNDLE) != null) {
            messageThread = (MessageThread) getIntent().getBundleExtra(Constants.KEY_BUNDLE)
                    .getSerializable(Constants.KEY_MESSAGES);
        } else {
            teacherId = getIntent().getIntExtra(Constants.KEY_TEACHER_ID, 0);
            courseId = getIntent().getIntExtra(Constants.KEY_COURSE_ID, 0);
        }
        if (messageThread != null) {
            messageThread.reverseMessagesOrder();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getSetReadThreadUrl();
            chatView.setAsSeen(url, messageThread.id);

        }
    }

    private void bindViews() {
        chatHeaderTextView = findViewById(R.id.tv_chat_header);
        if (messageThread != null) {
            String name = "";
            String[] nameArray = messageThread.otherNames.split(" ");
            if (nameArray.length > 1) {
                String first = nameArray[0];
                String last = nameArray[nameArray.length - 1];
                if (first.length() > 0 && last.length() > 0) {
                    name = first + " " + last;
                } else if (first.length() == 0 && last.length() > 0) {
                    name = last;
                } else if (first.length() > 0) {
                    name = first;
                }
            } else {
                name = nameArray[0];
            }
            chatHeaderTextView.setText(name);
        }
        backBtn = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_view);
        chatAdapter = new ChatAdapter(this, SessionManager.getInstance().getUserId());
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (messageThread != null) {
            setAdapterData(messageThread.messageArrayList);
        }
        messageEditText = findViewById(R.id.et_message);
        sendBtn = findViewById(R.id.enter_chat1);
        imageBtn = findViewById(R.id.btn_image);
        rootView = findViewById(R.id.root_view);
        sendLinearLayout = findViewById(R.id.ll_send);

        if (messageThread != null) {
            if (messageThread.id == -1) {
                sendLinearLayout.setVisibility(View.GONE);
            } else {
                sendLinearLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        recyclerView.setOnTouchListener(new HideKeyboardOnTouch(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.enter_chat1:
                if (isMessageValid()) {
                    if (messageThread != null) {
                        addMessageToAdapter(Html.toHtml(messageEditText.getText()));
                        sendMessage(messageEditText.getText().toString());
                        messageEditText.setText("");
                    } else {
                        showLoadingDialog();
                        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getThreads();
                        chatView.sendFirstMessage(url, teacherId + "",
                                SessionManager.getInstance().getUserId(),
                                messageEditText.getText().toString(), courseId + "");
                        addFirstMessageToAdapter(Html.toHtml(messageEditText.getText()));
                    }

                }
                break;
            case R.id.btn_image:
                if (chatAdapter.mDataList.size() > 0 && messageThread != null) {
                    Util.selectImagesFromGallery(
                            new OnImageSelectedListener() {
                                @Override
                                public void onImagesSelected(Uri uri) {
                                    sendImage(uri);

                                }
                            },
                            this,
                            rootView
                    );
                }
                break;
        }
    }

    private void setAdapterData(ArrayList<Message> messageArrayList) {
        if (messageArrayList.size() > 0) {
//            showLoadingDialog();
            ArrayList<Object> messageObjectArrayList = new ArrayList<>();
            messageObjectArrayList.addAll(messageArrayList);
            chatAdapter.addData(setDates(messageObjectArrayList));
//            checkNullExtensions();
//            Util.isImageUrl(messageObjectArrayList,this);
        }
    }

    private boolean isMessageValid() {
        return !messageEditText.getText().toString().isEmpty() && !android.text.Html.fromHtml(messageEditText.getText().toString()).toString().isEmpty();
    }

    private void addMessageToAdapter(String messageString) {
        User user = new User();
        user.id =(Integer.parseInt(SessionManager.getInstance().getUserId()));
        Message message = new Message("", messageString,
                Util.convertLocaleToUtc(Util.getCurrentDate()), "", "", messageThread.id, messageThread.id, "", user);
        if (chatAdapter.mDataList.size() == 0) {
            chatAdapter.mDataList.add(Util.getDate(Util.getCurrentDate(), this));
            chatAdapter.mDataList.add(message);
            chatAdapter.notifyItemInserted(chatAdapter.mDataList.size() - 1);
            chatAdapter.notifyItemRangeChanged(chatAdapter.mDataList.size() - 2, chatAdapter.mDataList.size());
        } else {
            if (!(chatAdapter.mDataList.get(chatAdapter.mDataList.size() - 1) instanceof String)) {
                Message lastMessage = (Message) chatAdapter.mDataList.get(chatAdapter.mDataList.size() - 1);
                if (!Util.isSameDay(message.createdAt, lastMessage.createdAt)) {
                    chatAdapter.mDataList.add(Util.getDate(Util.getCurrentDate(), this));
                    chatAdapter.mDataList.add(message);
                    chatAdapter.notifyItemInserted(chatAdapter.mDataList.size() - 1);
                    chatAdapter.notifyItemRangeChanged(chatAdapter.mDataList.size() - 2, chatAdapter.mDataList.size());
                } else {
                    chatAdapter.mDataList.add(message);
                    chatAdapter.notifyItemInserted(chatAdapter.mDataList.size() - 1);
                    chatAdapter.notifyItemRangeChanged(chatAdapter.mDataList.size() - 1, chatAdapter.mDataList.size());
                }
            }
        }
        recyclerView.smoothScrollToPosition(chatAdapter.mDataList.size() - 1);
    }


    private void addFirstMessageToAdapter(String messageString) {
        User user = new User();
        user.id = (Integer.parseInt(SessionManager.getInstance().getUserId()));
        Message message = new Message("", messageString,
                Util.convertLocaleToUtc(Util.getCurrentDate()), "", "", 1, 2, "", user);
        if (chatAdapter.mDataList.size() == 0) {
            chatAdapter.mDataList.add(Util.getDate(Util.getCurrentDate(), this));
            chatAdapter.mDataList.add(message);
            chatAdapter.notifyItemInserted(chatAdapter.mDataList.size() - 1);
            chatAdapter.notifyItemRangeChanged(chatAdapter.mDataList.size() - 2, chatAdapter.mDataList.size());
        } else {
            if (!(chatAdapter.mDataList.get(chatAdapter.mDataList.size() - 1) instanceof String)) {
                Message lastMessage = (Message) chatAdapter.mDataList.get(chatAdapter.mDataList.size() - 1);
                if (!Util.isSameDay(message.createdAt, lastMessage.createdAt)) {
                    chatAdapter.mDataList.add(Util.getDate(Util.getCurrentDate(), this));
                    chatAdapter.mDataList.add(message);
                    chatAdapter.notifyItemInserted(chatAdapter.mDataList.size() - 1);
                    chatAdapter.notifyItemRangeChanged(chatAdapter.mDataList.size() - 2, chatAdapter.mDataList.size());
                } else {
                    chatAdapter.mDataList.add(message);
                    chatAdapter.notifyItemInserted(chatAdapter.mDataList.size() - 1);
                    chatAdapter.notifyItemRangeChanged(chatAdapter.mDataList.size() - 1, chatAdapter.mDataList.size());
                }
            }
        }
        recyclerView.smoothScrollToPosition(chatAdapter.mDataList.size() - 1);
    }


    private void addImageToAdapter(String attachmentUrl) {

        User user = new User();
        user.id = (Integer.parseInt(SessionManager.getInstance().getUserId()));
        Message message = new Message(attachmentUrl, "",
                Util.convertLocaleToUtc(Util.getCurrentDate()), "png", "", messageThread.id, messageThread.id, "", user);
        message.isImage = true;
        if (chatAdapter.mDataList.size() == 0) {
            chatAdapter.mDataList.add(Util.getDate(Util.getCurrentDate(), this));
            chatAdapter.mDataList.add(message);
            chatAdapter.notifyItemInserted(chatAdapter.mDataList.size() - 1);
            chatAdapter.notifyItemRangeChanged(chatAdapter.mDataList.size() - 2, chatAdapter.mDataList.size());
        } else {
            if (!(chatAdapter.mDataList.get(chatAdapter.mDataList.size() - 1) instanceof String)) {
                Message lastMessage = (Message) chatAdapter.mDataList.get(chatAdapter.mDataList.size() - 1);
                if (!Util.isSameDay(message.createdAt, lastMessage.createdAt)) {
                    chatAdapter.mDataList.add(Util.getDate(Util.getCurrentDate(), this));
                    chatAdapter.mDataList.add(message);
                    chatAdapter.notifyItemInserted(chatAdapter.mDataList.size() - 1);
                    chatAdapter.notifyItemRangeChanged(chatAdapter.mDataList.size() - 2, chatAdapter.mDataList.size());
                } else {
                    chatAdapter.mDataList.add(message);
                    chatAdapter.notifyItemInserted(chatAdapter.mDataList.size() - 1);
                    chatAdapter.notifyItemRangeChanged(chatAdapter.mDataList.size() - 1, chatAdapter.mDataList.size());
                }
            }
        }
        recyclerView.smoothScrollToPosition(chatAdapter.mDataList.size() - 1);


    }

    private void sendImage(Uri imageUri) {
        if (messageThread != null) {
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getSendImageId(messageThread.id);
            chatView.sendImage(url, "currenttime.jpg", imageUri);
            addImageToAdapter(imageUri.toString());
        }
    }

    private void sendMessage(String message) {
        message = StringEscapeUtils.escapeJava(message);
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getSendMessageUrl(messageThread.id);
        chatView.sendMessage(url, message, messageThread.id + "",
                SessionManager.getInstance().getUserId(), messageThread.id + "", messageThread.name);
    }

    @Override
    public void onSendMessageSuccess() {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void onSendMessageFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        showErrorDialog(this, errorCode, "");
    }

    @Override
    public void onFirstMessageSuccess() {
        messageEditText.setText("");
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void onFirstMessageFailure(String message, int errorCode) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        showErrorDialog(this, errorCode, "");
    }

    @Override
    public void onSendImageSuccess(String attachmentUrl) {

    }

    @Override
    public void onSendImageFailure(String message, int errorCode) {
        showErrorDialog(this, errorCode, "");
    }

    private ArrayList<Object> setDates(ArrayList<Object> messageObjectArrayList) {
        int i = 0;
        int j = 1;
        while (i < messageObjectArrayList.size() && j < messageObjectArrayList.size()) {
            Message message1 = (Message) messageObjectArrayList.get(i);
            Message message2 = (Message) messageObjectArrayList.get(j);
            if (Util.isSameDay(message1.createdAt, message2.createdAt)) {
                i = j;
                j = j + 1;
            } else {
                messageObjectArrayList.add(j, Util.getDate(message2.createdAt, this));
                i = j + 1;
                j = j + 2;
            }
        }
        if (messageObjectArrayList.size() > 0) {
            Message message = (Message) messageObjectArrayList.get(0);
            messageObjectArrayList.add(0, Util.getDate(message.createdAt, this));
        }
        return messageObjectArrayList;
    }
//
//    @Override
//    public void onCheckType(final Message message, final int position) {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if(position < chatAdapter.mDataList.size() && !message.isImage){
//                    chatAdapter.mDataList.set(position, message);
//                    chatAdapter.notifyItemChanged(position);
//                }
//            }
//        });
//    }


//    private void checkNullExtensions(){
//        for(int i = 0 ; i<chatAdapter.mDataList.size(); i++){
//            if(!(chatAdapter.mDataList.get(i) instanceof  String)){
//                Message message = (Message)chatAdapter.mDataList.get(i);
//                if(!message.attachmentUrl.isEmpty() && !message.attachmentUrl.equals("null")){
//                    if(message.ext.isEmpty() || message.ext.equals("null")){
//                        Util.isImageUrl(message,i,this);
//                    }
//                }
//
//            }
//        }
//    }
}
