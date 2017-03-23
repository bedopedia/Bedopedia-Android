package com.example.bedopedia.bedopedia_android;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.AskTeacherAdapter;
import Models.AskTeacherMessage;
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

            Call<JsonObject>  call = apiService.getServise(url, params);

            call.enqueue(new Callback<JsonObject> () {

                @Override
                public void onResponse(Call<JsonObject>  call, Response<JsonObject>  response) {
                    progress.dismiss();
                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,"Not Authorized","you don't have the right to do this");
                    } else if (statusCode == 200) {
                        Log.v("Response", response.body().toString());

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progress.dismiss();
                    Dialogue.AlertDialog(context,"Connection Failed","Check your Netwotk connection and Try again");
                }


            });
            return null;
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_teacher);


        ArrayList<ArrayList<AskTeacherMessage>> items =  new ArrayList<>() ;
        ArrayList<String> header = new ArrayList<>();
        progress = new ProgressDialog(this);
        new MessageThreads().execute();

        for (int i = 0 ; i < 5 ; i++) {
            ArrayList<AskTeacherMessage> temp = new ArrayList<>();
            temp.add(new AskTeacherMessage("YESTERDAY","Rahim: Thanks!","Homework EX: 234", 2 , "fakedata"));
            temp.add(new AskTeacherMessage("YESTERDAY","Rahim: Thanks!","Homework EX: 234", 2 , "fakedata"));

            items.add(temp);
            header.add("Math");
        }

        AskTeacherAdapter askTeacherAdapter = new AskTeacherAdapter(AskTeacherActivity.this, R.layout.activity_ask_teacher,items, header);

        ListView askTeacherListView = (ListView) findViewById(R.id.ask_teacher_list);
        askTeacherListView.setAdapter(askTeacherAdapter);





    }
}
