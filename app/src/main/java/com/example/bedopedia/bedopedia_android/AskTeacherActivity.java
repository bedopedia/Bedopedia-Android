package com.example.bedopedia.bedopedia_android;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Adapters.AskTeacherAdapter;
import Models.AskTeacherMessage;
import Models.MessageThread;
import Models.Student;
import Services.ApiClient;
import Services.ApiInterface;
import Tools.Dialogue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.R.id.progress;
import static com.example.bedopedia.bedopedia_android.TimetableActivity.context;


public class AskTeacherActivity extends AppCompatActivity {
    ProgressDialog progress;
    Map<String,Pair<String,ArrayList<AskTeacherMessage>>> items =  new HashMap() ;
    ArrayList<ArrayList<AskTeacherMessage>> itemsParam = new ArrayList<>();

    ArrayList<String> header = new ArrayList<>();

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

            SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            String id = sharedPreferences.getString("user_id", "");
            String url = "/api/threads";
            Map <String, String> params = new HashMap<>();
            params.put("user_id" , id);

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
                            if ( items.containsKey(messageThread.get("course_id").toString())) {
                                ArrayList<AskTeacherMessage> array = items.get(messageThread.get("course_id").toString()).second;
                                array.add(new AskTeacherMessage(messageThread.get("last_added_date").getAsString(),
                                        messages.get(0).getAsJsonObject().get("body").getAsString(),
                                        messageThread.get("name").getAsString(),
                                        2,
                                        messages.get(0).getAsJsonObject().get("user").getAsJsonObject().get("avatar_url").getAsString()));
                            }
                            else if (!(messageThread.get("course_id") == JsonNull.INSTANCE)) {
                                ArrayList<AskTeacherMessage> array = new ArrayList<AskTeacherMessage>();
                                array.add (new AskTeacherMessage(messageThread.get("last_added_date").getAsString(),
                                        messages.get(0).getAsJsonObject().get("body").getAsString(),
                                        messageThread.get("name").getAsString(),
                                        2 ,
                                        messages.get(0).getAsJsonObject().get("user").getAsJsonObject().get("avatar_url").getAsString()));
                                items.put(messageThread.get("course_id").toString(), new Pair<String, ArrayList<AskTeacherMessage>>(messageThread.get("course_name").toString(),array));
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

        progress = new ProgressDialog(this);
        new MessageThreads().execute();

    }
}
