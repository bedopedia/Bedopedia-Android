package com.example.bedopedia.bedopedia_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Models.NotificationModel;
import adapters.NotificationAdapter;

public class Notification extends AppCompatActivity {

    private List<NotificationModel> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setTitle("Notifications");

        try {
            notifications = new  ArrayList<NotificationModel>();
            notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", new Date() , null));
            notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", new Date() , null));
            notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", new Date() , null));
            notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", new Date() , null));
            notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", new Date() , null));
            notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", new Date() , null));
            NotificationAdapter notificationAdapter = new NotificationAdapter(this, R.layout.activity_notification,notifications);
            ListView listView = (ListView) findViewById(R.id.listview_notification);
            listView.setAdapter(notificationAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }






    }
}
