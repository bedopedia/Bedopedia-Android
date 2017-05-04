package com.example.bedopedia.bedopedia_android;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.util.HashMap;
import java.util.Map;

import Adapters.SingleMessageThreadAdapter;
import Models.Message;
import Models.MessageAttributes;
import Models.MessageThread;
import Models.User;
import Tools.SharedPreferenceUtils;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageThreadActivity extends AppCompatActivity {



    MessageThread thread;
    private SharedPreferences sharedPreferences;
    private User currUser;
    private SingleMessageThreadAdapter messagesAdapter;
    String idKey =  "id";
    String firstnameKey = "firstname";
    String lastnameKey = "lastname";
    String genderKey = "gender";
    String emailKey = "email";
    String avatarUrlKey = "avatar_url";
    String userTypeKey = "user_type";


    @BindView(R.id.message)
    EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_thread);
        ButterKnife.bind(this);

        sharedPreferences = SharedPreferenceUtils.getSharedPreference(MessageThreadActivity.this, "cur_user");
        currUser = getCurrUser();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Ask Teacher");
        ImageButton back = (ImageButton) findViewById(R.id.action_bar_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        Bundle extras= getIntent().getExtras();
        thread = (Models.MessageThread) getIntent().getSerializableExtra("message_thread");
        thread.reverseMessagesOrder();
        TextView othersName = (TextView) findViewById(R.id.message_receipient_name);
        othersName.setText(thread.getOthersName());

        messagesAdapter = new SingleMessageThreadAdapter(this, R.layout.single_send_message,thread.getMessages());
        ListView listView = (ListView) findViewById(R.id.messages_list);
        listView.setAdapter(messagesAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();





        messageText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_SEND)) {
                    sendMessage(messageText.getText().toString());
                    messageText.setText("" , TextView.BufferType.EDITABLE);
                    messageText.requestFocus();
                }
                return (actionId == EditorInfo.IME_ACTION_SEND);
            }
        });




    }

    User getCurrUser () {
        String user_data = SharedPreferenceUtils.getStringValue("user_data" , "", sharedPreferences);
        JsonParser parser = new JsonParser();
        JsonObject user = parser.parse(user_data).getAsJsonObject();
        User currUser = new User(user.get(idKey).getAsInt(),
                user.get(firstnameKey).getAsString(),
                user.get(lastnameKey).getAsString(),
                user.get(genderKey).getAsString(),
                user.get(emailKey).getAsString(),
                user.get(avatarUrlKey).getAsString(),
                user.get(userTypeKey).getAsString()
        );
        return currUser;
    }



    void sendMessage (final String text){

        MessageAttributes messageAttributes = new MessageAttributes(currUser.getId() , text , "");
        thread.sendMessage(messageAttributes);

        SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(MessageThreadActivity.this, "cur_user");
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        Map <String , Object> message_thread = new HashMap<>();
        message_thread.put("message_thread"  , thread);
        Call<MessageThread> call = apiService.putThreadMessages(thread.getId(), message_thread);

        call.enqueue(new Callback<MessageThread>() {
            @Override
            public void onResponse(Call<MessageThread> call, Response<MessageThread> response) {
                int statusCode = response.code();
                if(statusCode == 401) {

                } else if (statusCode == 200) {
                    Message lastMessage = new Message(text, "" , "" , getCurrUser(), response.body().getId());
                    thread = response.body();
                    messagesAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<MessageThread> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"connection failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
