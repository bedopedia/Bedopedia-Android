package com.example.bedopedia.bedopedia_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import Adapters.BehaviorNotesFragmentAdapter;
import Adapters.TimetableAdapter;
import Models.BehaviorNote;
import Models.CourseGroup;
import Models.Student;
import Models.TimetableSlot;
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
    TextView nextSlot;
    TextView positiveNotesCounter;
    TextView negativeNotesCounter;
    LinearLayout attendanceLayer;
    LinearLayout gradesLayer;
    LinearLayout timeTableLayer;
    LinearLayout notesLayer;

    public static List<TimetableSlot> todaySlots;
    public static List<TimetableSlot> tomorrowSlots;

    public static List<BehaviorNote> positiveNotesList;
    public static List<BehaviorNote> negativeNotesList;

    ProgressBar attendanceProgress;

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
                //progress.dismiss();
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
                                    if (courseData.get("icon").toString().equals("null"))
                                        courseGroups.get(j).setIcon("dragon");
                                    else
                                        courseGroups.get(j).setIcon(courseData.get("icon").getAsString());
                                }
                            }

                        }
                        totalGrade = response.body().get(i).get("total_grade").getAsString();
                        totalGradeText.setText("Average : "+totalGrade);

                        getStudentTimeTable();

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

    public void getStudentTimeTable(){
        String url = "api/students/" + studentId + "/timetable";
        Map<String, String> params = new HashMap<>();
        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);

        call.enqueue(new Callback<ArrayList<JsonObject> >() {

            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                //progress.dismiss();
                int statusCode = response.code();
                if (statusCode == 401) {
                    Dialogue.AlertDialog(context, "Not Authorized", "you don't have the right to do this");
                } else if (statusCode == 200) {

                    Calendar calendar = Calendar.getInstance();
                    Date date = calendar.getTime();
                    String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
                    calendar.add( Calendar.DATE, 1 );
                    date = calendar.getTime();
                    String tomorrow = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
                    today = today.toLowerCase();
                    tomorrow = tomorrow.toLowerCase();
                    if (today.equals("thursday")){
                        tomorrow = "sunday";
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    formatter.setTimeZone(TimeZone.getTimeZone("Egypt"));
                    for (int i = 0; i < response.body().size(); i++) {
                        JsonObject slot = response.body().get(i);
                        String from = slot.get("from").getAsString();
                        String to = slot.get("to").getAsString();
                        if (from.indexOf('.') != -1)
                            from = from.substring(0, from.indexOf('.')) + 'Z';
                        if (to.indexOf('.') != -1)
                            to = to.substring(0, to.indexOf('.')) + 'Z';
                        String day = slot.get("day").getAsString();
                        String courseName = slot.get("course_name").getAsString();
                        String classRoom = slot.get("school_unit").getAsString();

                        Date fromDate = null;
                        Date toDate = null;
                        try {

                            fromDate = formatter.parse(from.replaceAll("Z$", "+0000"));
                            toDate = formatter.parse(to.replaceAll("Z$", "+0000"));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (day.equals(today)){
                            todaySlots.add(new TimetableSlot(fromDate, toDate, day, courseName, classRoom));
                        } else if (day.equals(tomorrow)) {
                            tomorrowSlots.add(new TimetableSlot(fromDate, toDate, day, courseName, classRoom));
                        }

                    }
                    Date current = new Date();
                    boolean nextSlotFound = false;
                    Collections.sort(todaySlots);
                    Collections.sort(tomorrowSlots);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

                    for (TimetableSlot s : todaySlots){
                        if ((s.getFrom().getHours() == current.getHours() && s.getFrom().getMinutes() >= current.getMinutes()) ||
                                s.getFrom().getHours() > current.getHours()){
                            nextSlotFound = true;
                            nextSlot.setText("Next: " + s.getCourseName() + ", " + s.getDay() + " " + dateFormat.format(s.getFrom()));
                            break;
                        }
                    }
                    if(!nextSlotFound){
                        TimetableSlot s = tomorrowSlots.get(0);
                        nextSlot.setText("Next: " + s.getCourseName() + ", " + s.getDay() + " " + dateFormat.format(s.getFrom()));
                    }

                    getStudentBehaviorNotes();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Dialogue.AlertDialog(context, "Connection Failed", "Check your Netwotk connection and Try again");
            }
        });
    }

    public void getStudentBehaviorNotes(){
        String url = "api/behavior_notes";
        Map<String, String> params = new HashMap<>();
        params.put("student_id" , studentId);
        params.put("user_type" , "Parents");

        Call<JsonObject>  call = apiService.getServise(url, params);

        call.enqueue(new Callback<JsonObject> () {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // must be closed at the last service
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
                    positiveNotesCounter.setText(positiveNotesList.size()+"");
                    negativeNotesCounter.setText(negativeNotesList.size()+"");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

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

        todaySlots = new ArrayList<TimetableSlot>();
        tomorrowSlots = new ArrayList<TimetableSlot>();

        positiveNotesList = new ArrayList<BehaviorNote>();
        negativeNotesList = new ArrayList<BehaviorNote>();

        progress = new ProgressDialog(this);
        Bundle extras= getIntent().getExtras();
        studentId = extras.getString("student_id");
        studentName = extras.getString("student_name");
        studentAvatar = extras.getString("student_avatar");
        studentLevel = extras.getString("student_level");
        attendance = extras.getString("attendances");


        attendanceLayer = (LinearLayout) findViewById(R.id.open_attendance);
        gradesLayer = (LinearLayout) findViewById(R.id.open_grades);
        timeTableLayer = (LinearLayout) findViewById(R.id.open_timetable);
        notesLayer = (LinearLayout) findViewById(R.id.open_notes);

        studentAvatarImage = (ImageView) findViewById(R.id.home_student_avatar);
        studentLevelView = (TextView) findViewById(R.id.home_student_level);
        studentNameView = (TextView) findViewById(R.id.home_student_name);
        nextSlot = (TextView) findViewById(R.id.next_slot);

        positiveNotesCounter = (TextView) findViewById(R.id.positive_notes_counter);
        negativeNotesCounter = (TextView) findViewById(R.id.negative_notes_counter);

        attendanceProgress = (ProgressBar) findViewById(R.id.attendance_progress);
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
        if (attendaceDates.size() != 0)
            attendanceProgress.setProgress((presentDays*100)/attendaceDates.size());
        attendaceText.setText(presentDays + " / " + attendaceDates.size() +" days");


        attendanceLayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StudentActivity.this, AttendanceActivity.class);
                intent.putExtra("attendances",attendance);
                startActivity(intent);
            }
        });

        gradesLayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StudentActivity.this, GradesAvtivity.class);
                intent.putExtra("student_id", studentId);
                intent.putExtra("courseGroups", courseGroups);
                startActivity(intent);
            }
        });

        notesLayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, BehaviorNotesActivity.class);
                intent.putExtra("student_id", studentId);
                startActivity(intent);
            }
        });

        timeTableLayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, TimetableActivity.class);
                intent.putExtra("student_id", studentId);
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
