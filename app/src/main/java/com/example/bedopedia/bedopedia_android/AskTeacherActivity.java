package com.example.bedopedia.bedopedia_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Adapters.AskTeacherAdapter;
import Models.Message;
import Models.MessageThread;
import Tools.SharedPreferenceUtils;
import gradeBook.ActivityCourse;
import myKids.Student;
import Models.User;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import Tools.Dialogue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static timetable.TimetableActivity.context;


public class AskTeacherActivity extends AppCompatActivity {
    ProgressDialog progress;
    Map<String,Pair<String,ArrayList<MessageThread>>> items =  new HashMap() ;
    ArrayList<ArrayList<MessageThread>> itemsParam = new ArrayList<>();

    ArrayList<String> header = new ArrayList<>();
    String userIdKey = "user_id";
    String courseIdKey = "course_id";

    public void loading(){
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
    }

    private class MessageThreads extends AsyncTask {

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

            SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(AskTeacherActivity.this, "cur_user" );
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            String id = SharedPreferenceUtils.getStringValue(userIdKey, "",sharedPreferences);
            String url = "/api/threads";
            Map <String, String> params = new HashMap<>();
            params.put(userIdKey , id);

            Call<ArrayList<JsonObject>>  call = apiService.getServiseArr(url, params);

            call.enqueue(new Callback<ArrayList<JsonObject>> () {

                @Override
                public void onResponse(Call<ArrayList<JsonObject>>  call, Response<ArrayList<JsonObject>>  response) {
                    progress.dismiss();
                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,"Not Authorized","you don't have the right to do this");
                    } else if (statusCode == 200) {
                        ArrayList<JsonObject> threads = response.body();
                        for (int i = 0 ; i < threads.size(); i++) {
                            JsonObject messageThread = threads.get(i);
                            JsonArray messages = messageThread.get("messages").getAsJsonArray();
                            MessageThread thread;
                            ArrayList<Message> threadMessages = new ArrayList<Message>();
                            for (JsonElement messageElement : messages) { // gest needed data from assig, quizz, grade item
                                JsonObject messageObj = messageElement.getAsJsonObject();
                                JsonObject user = messageObj.get("user").getAsJsonObject();
                                User sender = new User(user.get("id").getAsInt(),
                                        user.get("firstname").getAsString(),
                                        user.get("lastname").getAsString(),
                                        user.get("gender").getAsString(),
                                        "" ,
                                        user.get("avatar_url").getAsString(),
                                        user.get("user_type").getAsString()
                                    );

                                Message message = new Message(messageObj.get("body").getAsString(),
                                        messageObj.get("created_at").getAsString(),
                                        messageObj.get("updated_at").getAsString(),
                                        sender,
                                        messageThread.get("id").getAsInt()
                                        );
                                threadMessages.add(message);
                            }
                            if ( items.containsKey(messageThread.get(courseIdKey).toString())) {
                                ArrayList<MessageThread> array = items.get(messageThread.get(courseIdKey).toString()).second;

                                thread = new MessageThread(messageThread.get("last_added_date").getAsString(),
                                        messages.get(0).getAsJsonObject().get("body").getAsString(),
                                        messageThread.get("name").getAsString(),
                                        2,
                                        messages.get(0).getAsJsonObject().get("user").getAsJsonObject().get("avatar_url").getAsString(),
                                        threadMessages,
                                        messageThread.get("id").getAsInt(),
                                        messageThread.get("others_names").getAsString()
                                );

                                array.add(thread);
                            }
                            else if (!(messageThread.get(courseIdKey) == JsonNull.INSTANCE)) {
                                ArrayList<MessageThread> array = new ArrayList<MessageThread>();
                                thread = new MessageThread(messageThread.get("last_added_date").getAsString(),
                                        messages.get(0).getAsJsonObject().get("body").getAsString(),
                                        messageThread.get("name").getAsString(),
                                        2,
                                        messages.get(0).getAsJsonObject().get("user").getAsJsonObject().get("avatar_url").getAsString(),
                                        threadMessages,
                                        messageThread.get("id").getAsInt(),
                                        messageThread.get("others_names").getAsString()
                                );

                                array.add(thread);
                                items.put(messageThread.get("course_id").toString(), new Pair<String, ArrayList<MessageThread>>(messageThread.get("course_name").toString(),array));
                            }

                        }

                    }
                    for (String key : items.keySet()) {
                        itemsParam.add(items.get(key).second);
                        header.add(items.get(key).first);
                    }


                    AskTeacherAdapter askTeacherAdapter = new AskTeacherAdapter(AskTeacherActivity.this, R.layout.activity_ask_teacher,itemsParam, header);

                    ListView askTeacherListView = (ListView) findViewById(R.id.ask_teacher_list);
                    askTeacherListView.setAdapter(askTeacherAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                    progress.dismiss();
                }


            });
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_teacher);

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

        progress = new ProgressDialog(this);
        new MessageThreads().execute();
    }

}
