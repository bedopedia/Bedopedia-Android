package com.example.bedopedia.bedopedia_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Adapters.GradesAdapter;
import Adapters.MyKidsAdapter;
import Models.Course;
import Models.CourseGroup;
import Models.Student;
import Services.ApiClient;
import Services.ApiInterface;
import Tools.Dialogue;
import Tools.InternetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by khaled on 2/22/17.
 */

public class StudentActivity extends AppCompatActivity {

    String studentId, studentName;
    String studentAvatar, studentLevel;
    Context context;
    ProgressDialog progress;
    ArrayList<CourseGroup> courseGroups;
    SharedPreferences sharedPreferences;
    ApiInterface apiService;
    String attendance;
    int presentDays;
    String totalGrade;
    TextView totalGradeText;
    ImageView studentAvatarImage;
    TextView studentLevelView;
    TextView studentNameView;

    public void loading(){
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
    }

    public void getStudentCourseGroups(){
        String url = "api/students/" + studentId + "/course_groups";
        Map<String, String> params = new HashMap<>();
        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);

        call.enqueue(new Callback<ArrayList<JsonObject> >() {

            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                //progress.dismiss();
                int statusCode = response.code();
                if(statusCode == 401) {
                    Dialogue.AlertDialog(context,"Not Authorized","you don't have the right to do this");
                } else if (statusCode == 200) {
                    for (int i = 0 ; i < response.body().size() ; i++) {
                        JsonObject courseGroupData = response.body().get(i);
                        JsonObject course = courseGroupData.get("course").getAsJsonObject();

                        courseGroups.add(new CourseGroup(
                                courseGroupData.get("id").getAsInt(),
                                course.get("id").getAsInt(),
                                courseGroupData.get("name").getAsString(),
                                courseGroupData.get("course_name").getAsString()
                        ));
                    }
                    getStudentGrades();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Dialogue.AlertDialog(context,"Connection Failed","Check your Netwotk connection and Try again");
            }
        });
    }

    public void getStudentGrades(){
        String url = "api/students/" + studentId + "/grade_certificate";
        Map<String, String> params = new HashMap<>();
        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);
        call.enqueue(new Callback<ArrayList<JsonObject> >() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                // must be closed at the last service
                progress.dismiss();
                int statusCode = response.code();
                if(statusCode == 401) {
                    Dialogue.AlertDialog(context,"Not Authorized","you don't have the right to do this");
                } else {
                    if (statusCode == 200) {
                        int i = 0;
                        for (; i < response.body().size()-1; i++) {
                            JsonObject courseData = response.body().get(i);
                            for(int j = 0 ; j < courseGroups.size() ; j++){
                                if(courseGroups.get(j).getCourseId() == courseData.get("course_id").getAsInt()){
                                    courseGroups.get(j).setGrade(courseData.get("grade").getAsString());
                                    courseGroups.get(j).setIcon(courseData.get("icon").getAsString());
                                }
                            }

                        }
                        totalGrade = response.body().get(i).get("total_grade").getAsString();
                        totalGradeText.setText("Average : "+totalGrade);

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Dialogue.AlertDialog(context,"Connection Failed","Check your Netwotk connection and Try again");
            }
        });
    }

    private class StudentAsyncTask extends AsyncTask {

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

            getStudentCourseGroups();

            return null;
        }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);

        progress = new ProgressDialog(this);
        Bundle extras= getIntent().getExtras();
        studentId = extras.getString("student_id");
        studentName = extras.getString("student_name");
        studentAvatar = extras.getString("student_avatar");
        studentLevel = extras.getString("student_level");
        attendance = extras.getString("attendances");

        studentAvatarImage = (ImageView) findViewById(R.id.home_student_avatar);
        studentLevelView = (TextView) findViewById(R.id.home_student_level);
        studentNameView = (TextView) findViewById(R.id.home_student_name);

        studentNameView.setText(studentName);
        studentLevelView.setText(studentLevel);
        Picasso.with(this).load(ApiClient.BASE_URL+studentAvatar).into(studentAvatarImage);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText(studentName);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(attendance);
        final JsonArray attenobdances = tradeElement.getAsJsonArray();
        Set<Date> attendaceDates = new HashSet<>();
        presentDays=0;

        for(JsonElement element: attenobdances){
            JsonObject day = element.getAsJsonObject();
            Date date = new Date();
            date.setTime(day.get("date").getAsLong());
            if(!attendaceDates.contains(date)){
                if(day.get("status").getAsString().equals("present"))
                    presentDays++;
            }
            attendaceDates.add(date);
        }
        TextView attendaceText = (TextView) findViewById(R.id.attendance_text);

        totalGradeText = (TextView) findViewById(R.id.average_grade);


        attendaceText.setText(presentDays + " / " + attendaceDates.size());


        ImageButton attendanceBtn = (ImageButton) findViewById(R.id.attendance_button);
        attendanceBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StudentActivity.this, AttendanceActivity.class);
                intent.putExtra("attendances",attendance);
                startActivity(intent);
            }
        });

        ImageButton gradesBtn = (ImageButton) findViewById(R.id.grades_btn);
        gradesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StudentActivity.this, GradesAvtivity.class);
                intent.putExtra("student_id", studentId);
                intent.putExtra("courseGroups", courseGroups);
                startActivity(intent);
            }
        });
        context = this;
        courseGroups = new ArrayList<CourseGroup>();

        sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
        apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);

        if (InternetConnection.isInternetAvailable(this)) {
            new StudentAsyncTask().execute();
        } else {
            Dialogue.AlertDialog(this,"No NetworkConnection","Check your Netwotk connection and Try again");
        }

    }
}
