package com.example.bedopedia.bedopedia_android;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_teacher);

        Toolbar askTeacherToolbar = (Toolbar) findViewById(R.id.default_toolbar_id);
        setSupportActionBar(askTeacherToolbar);
        ActionBar askTeacherActionbar = getSupportActionBar();
        askTeacherActionbar.setDisplayHomeAsUpEnabled(true);
        askTeacherActionbar.setTitle(R.string.AskTeacherTitle);

    }

}
