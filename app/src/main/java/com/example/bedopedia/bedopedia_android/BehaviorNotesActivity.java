package com.example.bedopedia.bedopedia_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.BehaviorNotesFragmentAdapter;
import Models.BehaviorNote;
import Models.Student;
import Services.ApiClient;
import Services.ApiInterface;
import Tools.Dialogue;
import Tools.InternetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by khaled on 2/27/17.
 */

public class BehaviorNotesActivity extends AppCompatActivity {
    ProgressDialog progress;
    String studentId, id;
    public static Context context;

    private BehaviorNotesFragmentAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static List<BehaviorNote> positiveNotesList;
    public static List<BehaviorNote> negativeNotesList;

    public void loading(){
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
    }

    private class BehaviourNotesAsyncTask extends AsyncTask {

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
            String url = "api/behavior_notes";
            Map<String, String> params = new HashMap<>();
            params.put("student_id" , studentId);
            params.put("user_type" , "Parents");

            Call<JsonObject>  call = apiService.getServise(url, params);

            call.enqueue(new Callback<JsonObject> () {

                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progress.dismiss();
                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,"Not Authorized","you don't have the right to do this");
                    } else if (statusCode == 200) {
                        JsonArray behaviourNotes = response.body().get("behavior_notes").getAsJsonArray();
                        for(JsonElement element: behaviourNotes){
                            JsonObject note = element.getAsJsonObject();
                            String category = note.get("category").getAsString();
                            String noteBody =  note.get("note").getAsString();
                            if(category.equals("Cooperative") ||
                                    category.equals("Politeness") ||
                                    category.equals("Punctuality") ||
                                    category.equals("Leadership") ||
                                    category.equals("Honesty"))
                                positiveNotesList.add(new BehaviorNote(category,noteBody));
                            else
                                negativeNotesList.add(new BehaviorNote(category,noteBody));
                        }
                    }
                    mSectionsPagerAdapter = new BehaviorNotesFragmentAdapter(getSupportFragmentManager());

                    mViewPager = (ViewPager) findViewById(R.id.behavior_notes_container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    TabLayout tabLayout = (TabLayout) findViewById(R.id.behavior_notes_tabs);
                    tabLayout.setupWithViewPager(mViewPager);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    mSectionsPagerAdapter = new BehaviorNotesFragmentAdapter(getSupportFragmentManager());

                    mViewPager = (ViewPager) findViewById(R.id.behavior_notes_container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    TabLayout tabLayout = (TabLayout) findViewById(R.id.behavior_notes_tabs);
                    tabLayout.setupWithViewPager(mViewPager);

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
        setContentView(R.layout.behavior_notes);

        positiveNotesList = new ArrayList<BehaviorNote>();
        negativeNotesList = new ArrayList<BehaviorNote>();

        progress = new ProgressDialog(this);
        Bundle extras= getIntent().getExtras();
        studentId = extras.getString("student_id");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Behavior notes");
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        context = this;

        if (InternetConnection.isInternetAvailable(this)) {
            new BehaviourNotesAsyncTask().execute();
        } else {
            Dialogue.AlertDialog(this,"No NetworkConnection","Check your Netwotk connection and Try again");
        }

    }

}
