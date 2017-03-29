package com.example.bedopedia.bedopedia_android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapters.NotificationAdapter;
import Adapters.SingleMessageThreadAdapter;
import Models.CourseGroup;
import Models.Message;
import Models.MessageThread;

public class MessageThreadActivity extends AppCompatActivity {

    Models.MessageThread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_thread);

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

        TextView othersName = (TextView) findViewById(R.id.others_name);
        othersName.setText(thread.getOthersName());

        SingleMessageThreadAdapter messagesAdapter = new SingleMessageThreadAdapter(this, R.layout.single_send_message,thread.getMessages());
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

}
