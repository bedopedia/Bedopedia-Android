package student;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.AskTeacherActivity;
import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import Adapters.NotificationAdapter;
import Tools.FragmentUtils;
import Tools.ImageViewHelper;
import badges.Badge;
import Models.NotificationModel;
import myKids.Student;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import badges.BadgesDialog;
import Tools.Dialogue;
import Tools.InternetConnection;
import attendance.AttendanceActivity;
import behaviorNotes.BehaviorNote;
import behaviorNotes.BehaviorNotesActivity;
import grades.CourseGroup;
import grades.GradesAvtivity;
import myKids.MyKidsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timetable.Fragments.TodayFragment;
import timetable.Fragments.TomorrowFragment;
import timetable.TimeTableFragment;
import timetable.TimetableActivity;
import timetable.TimetableSlot;

/**
 * Created by khaled on 2/22/17.
 */

public class StudentActivity extends AppCompatActivity {


    ActionBar ab ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);


        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("");

        FragmentUtils.createFragment(getSupportFragmentManager(), StudentFragment.newInstance(), R.id.student_home_container);



    }
}
