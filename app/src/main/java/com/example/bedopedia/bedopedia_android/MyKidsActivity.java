package com.example.bedopedia.bedopedia_android;

/**
 * Created by mohamed on 2/9/17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Adapters.MyKidsAdapter;
import Adapters.NotificationAdapter;
import Models.NotificationModel;
import Models.Student;
import Services.ApiClient;
import Services.ApiInterface;
import Tools.Dialogue;
import Tools.InternetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyKidsActivity extends AppCompatActivity{

    List<Student> myKids;
    String id;
    Context context;
    ProgressDialog progress;
    ArrayList<JsonArray> kidsAttendances;

    DrawerLayout notificationLayout;
    ImageButton notificationButton;
    ListView notificationList;
    ActionBarDrawerToggle notificationToggle;
    List<NotificationModel> notifications;


    public void loading(){
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
    }

    private class KidsAsyncTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading();
            progress.show();
        }

        protected void onProgressUpdate(String... progress) {
            loading();
        }

        @Override
        protected List<Student> doInBackground(Object... param) {

            SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            id = sharedPreferences.getString("id", "");
            String url = "api/parents/" + id + "/children";
            Map <String, String> params = new HashMap<>();
            params.put("parent_id" , id);
            Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);

            call.enqueue(new Callback<ArrayList<JsonObject> >() {

                @Override
                public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                    progress.dismiss();
                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,"Not Authorized","you don't have the right to do this");
                    } else if (statusCode == 200) {
                        for (int i = 0 ; i < response.body().size() ; i++){
                            JsonObject studentData = response.body().get(i);
                            JsonArray attenobdances = studentData.get("attendances").getAsJsonArray();
                            kidsAttendances.add(attenobdances);
                            myKids.add(new Student(Integer.parseInt(studentData.get("id").toString()),
                                    studentData.get("firstname").toString().substring(1,studentData.get("firstname").toString().length()-1),
                                    studentData.get("lastname").toString().substring(1, studentData.get("lastname").toString().length()-1),
                                    studentData.get("gender").toString().substring(1,studentData.get("gender").toString().length()-1),
                                    studentData.get("email").toString().substring(1,studentData.get("email").toString().length()-1),
                                    studentData.get("avatar_url").toString().substring(1,studentData.get("avatar_url").toString().length()-1),
                                    studentData.get("user_type").toString().substring(1,studentData.get("user_type").toString().length()-1),
                                    studentData.get("level_name").toString().substring(1,studentData.get("level_name").toString().length()-1),
                                    studentData.get("section_name").toString().substring(1,studentData.get("section_name").toString().length()-1),
                                    studentData.get("stage_name").toString().substring(1,studentData.get("stage_name").toString().length()-1),
                                    0,null,null));
                        }

                        MyKidsAdapter adapter = new MyKidsAdapter(context, R.layout.single_student, myKids);
                        GridView studentsList = (GridView) findViewById(R.id.students_list);
                        studentsList.setAdapter(adapter);


                    }
                }

                @Override
                public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                    progress.dismiss();
                    Dialogue.AlertDialog(context,"Connection Failed","Check your Netwotk connection and Try again");
                }
            });
            return myKids;
        }


    }


    private class NotificationsAsyncTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading();
            progress.show();
        }

        protected void onProgressUpdate(String... progress) {
            loading();
        }

        @Override
        protected List<Student> doInBackground(Object... param) {

            SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            id = sharedPreferences.getString("user_id", "");
            String url = "/api/users/"+id +"/notifications";
            Map <String, String> params = new HashMap<>();
            params.put("page" , "1");
            params.put("per_page" , "20");
            Call<JsonObject>  call = apiService.getServise(url, params);

            call.enqueue(new Callback<JsonObject> () {

                @Override
                public void onResponse(Call<JsonObject>  call, Response<JsonObject>  response) {
                    progress.dismiss();
                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,"Not Authorized","you don't have the right to do this");
                    } else if (statusCode == 200) {
                        notifications = new  ArrayList<NotificationModel>();
                        Log.v("Notifications",response.body().toString());
                        JsonObject notificationsRespone = response.body();


                        for (JsonElement pa : notificationsRespone.get("notifications").getAsJsonArray()) { // gest needed data from assig, quizz, grade item
                            JsonObject notificationObj = pa.getAsJsonObject();
                            Log.v("notification message",notificationObj.get("message").getAsString());
                            try {

                                notifications.add(new NotificationModel(notificationObj.get("message").getAsString(), new Date() ,notificationObj.get("logo").getAsString()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        NotificationAdapter notificationAdapter = new NotificationAdapter(context, R.layout.notification_list_item,notifications);
                        ListView listView = (ListView) findViewById(R.id.listview_notification);
                        listView.setAdapter(notificationAdapter);


                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progress.dismiss();
                    Log.v("Error",t.toString());
                    Dialogue.AlertDialog(context,"Connection Failed","Check your Netwotk connection and Try again");
                }


            });
            return myKids;
        }


    }










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_kids);
        setTitle("My kids");
        myKids = new ArrayList<Student>();
        context = this;
        progress = new ProgressDialog(this);
        kidsAttendances = new ArrayList<JsonArray>();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_actionbar);

        notificationLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        notificationList = (ListView) findViewById(R.id.listview_notification);
        notificationButton = (ImageButton) findViewById(R.id.home_action_bar_notification);
        notificationLayout.setDrawerListener(notificationToggle);
        notificationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(notificationLayout.isDrawerOpen(notificationList)){
                    notificationLayout.closeDrawer(notificationList);
                } else {

                        new NotificationsAsyncTask().execute();


                    notificationLayout.openDrawer(notificationList);
                }
            }
        });

        if (InternetConnection.isInternetAvailable(this)){
            new KidsAsyncTask().execute();
        } else {
            Dialogue.AlertDialog(this,"No NetworkConnection","Check your Netwotk connection and Try again");
        }


    }

    public void itemClicked (int index){
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra("student_id", String.valueOf(myKids.get(index).getId()));
        intent.putExtra("student_name", myKids.get(index).getFirstName() + " " + myKids.get(index).getLastName());
        intent.putExtra("student_avatar", myKids.get(index).getAvatar());
        intent.putExtra("student_level", myKids.get(index).getLevel());
        intent.putExtra("attendances",kidsAttendances.get(index).toString());
        startActivity(intent);

//        Toast.makeText(getApplicationContext(),"you clicked " + myKids.get(index).getFirstName(),
//        Toast.LENGTH_LONG).show();
    }
}
