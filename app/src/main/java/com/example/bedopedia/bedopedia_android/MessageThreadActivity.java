package com.example.bedopedia.bedopedia_android;

import android.content.SharedPreferences;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.NotificationAdapter;
import Adapters.SingleMessageThreadAdapter;
import Models.CourseGroup;
import Models.Message;
import Models.MessageAttributes;
import Models.MessageThread;
import Models.User;
import Services.ApiClient;
import Services.ApiInterface;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.json.JSONObject;

import Models.MessageThread;
import okhttp3.RequestBody;
import retrofit2.*;
import retrofit2.http.*;

public class MessageThreadActivity extends AppCompatActivity {



    MessageThread thread;
    private SharedPreferences sharedPreferences;
    private User currUser;
    private SingleMessageThreadAdapter messagesAdapter;

    @BindView(R.id.message)
    EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_thread);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
        currUser = getCurrUser();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Ask Teacher");
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        Bundle extras= getIntent().getExtras();
        thread = (Models.MessageThread) getIntent().getSerializableExtra("message_thread");
        Log.e("Thread" , thread.getId() + "");
        TextView othersName = (TextView) findViewById(R.id.others_name);
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
                    return true;
                }
                return false;
            }
        });




    }

    User getCurrUser () {
        String user_data = sharedPreferences.getString("user_data" , "");
        JsonParser parser = new JsonParser();
        JsonObject user = parser.parse(user_data).getAsJsonObject();
        User currUser = new User(user.get("id").getAsInt(),
                user.get("firstname").getAsString(),
                user.get("lastname").getAsString(),
                user.get("gender").getAsString(),
                user.get("email").getAsString(),
                user.get("avatar_url").getAsString(),
                user.get("user_type").getAsString()
        );
        return currUser;
    }


    void sendMessage (final String text){

        MessageAttributes messageAttributes = new MessageAttributes(currUser.getId() , text , "");
        thread.sendMessage(messageAttributes);

        SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
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
                    thread.updateLastMessage(lastMessage);
                    messagesAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<MessageThread> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"connection failed",Toast.LENGTH_SHORT).show();
                Log.e("ERRRRROOOOO" , t.toString());
            }
        });
    }
}
