package com.example.bedopedia.bedopedia_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
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


        notifications = new  ArrayList<NotificationModel>();

        notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", "Today 9:00 AM", null));
        notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", "Today 9:00 AM", null));
        notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", "Today 9:00 AM", null));
        notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", "Today 9:00 AM", null));
        notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", "Today 9:00 AM", null));
        notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", "Today 9:00 AM", null));
        notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", "Today 9:00 AM", null));
        notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", "Today 9:00 AM", null));
        notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", "Today 9:00 AM", null));
        notifications.add(new NotificationModel("Quiz 5 is submitted and auto graded", "Today 9:00 AM", null));



        NotificationAdapter notificationAdapter = new NotificationAdapter(this, R.layout.activity_notification,notifications);
        ListView listView = (ListView) findViewById(R.id.listview_notification);
        listView.setAdapter(notificationAdapter);



    }
}
