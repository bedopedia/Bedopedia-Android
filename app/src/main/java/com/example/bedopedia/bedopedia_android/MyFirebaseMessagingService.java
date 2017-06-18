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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import myKids.MyKidsActivity;
import student.StudentActivity;
import student.StudentFragment;

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


            if (data.get("event").equals("notification")) {

               /* if (data.get("message").equals("markAsSeen")) {
                    MyKidsActivity.notificationNumber = 0;
                    NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nMgr.cancelAll();
                } else {

                }
                */
                try {
                    JSONObject jsonObj = new JSONObject(data.get("payload"));
                    MyKidsActivity.notificationNumber = MyKidsActivity.notificationNumber + 1;
                    android.support.v4.app.NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.quizzes_ico)
                                    .setContentTitle(jsonObj.getString("message"))
                                    .setContentText(jsonObj.getString("text"));
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //}


            } else if (data.get("event").equals("message")){
                try {
                    JSONObject message = new JSONObject(data.get("payload"));
                    JSONObject user = new JSONObject(message.getString("user"));
                    StudentFragment.messageNumber = StudentFragment.messageNumber + 1;
                    android.support.v4.app.NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.quizzes_ico)
                                    .setContentTitle(user.getString("name"))
                                    .setContentText(android.text.Html.fromHtml(message.getString("body")));
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


    }
}
