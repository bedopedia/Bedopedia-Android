package com.example.bedopedia.bedopedia_android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;



import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import myKids.MyKidsActivity;

/**
 * Created by ali on 27/02/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static int mdl = 0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
        if (sharedPreferences.getString("is_logged_in", "").equals("true")) {
            Map<String, String> data = remoteMessage.getData();

            if (data.get("notification_type").equals("notification")) {
                if (data.get("message").equals("markAsSeen")) {
                    MyKidsActivity.notificationNumber = 0;
                    NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nMgr.cancelAll();
                } else {
                    MyKidsActivity.notificationNumber = MyKidsActivity.notificationNumber + 1;
                    android.support.v4.app.NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.quizzes_ico)
                                    .setContentTitle(data.get("title"))
                                    .setContentText(data.get("message"));
                    Intent resultIntent = new Intent(this, MyKidsActivity.class);

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addParentStack(MyKidsActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(mdl, mBuilder.build());
                    mdl = mdl +1;
                }


            } else if (data.get("notification_type").equals("message")){
                StudentActivity.messageNumber = StudentActivity.messageNumber + 1;
                android.support.v4.app.NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.quizzes_ico)
                                .setContentTitle(data.get("name"))
                                .setContentText(android.text.Html.fromHtml(data.get("message")));
                Intent resultIntent = new Intent(this, AskTeacherActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(MyKidsActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(mdl, mBuilder.build());
                mdl = mdl +1;
            }
            }


        }
    }
