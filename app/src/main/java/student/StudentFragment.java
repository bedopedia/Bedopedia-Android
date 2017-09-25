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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import Models.NotificationModel;
import Tools.CalendarUtils;
import Tools.Dialogue;
import Tools.ImageViewHelper;
import Tools.InternetConnection;
import Tools.SharedPreferenceUtils;
import attendance.AttendanceActivity;
import badges.Badge;
import badges.BadgesFragment;
import behaviorNotes.BehaviorNote;
import behaviorNotes.BehaviorNotesActivity;
import grades.CourseGroup;
import grades.GradesAvtivity;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import myKids.MyKidsActivity;
import myKids.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timetable.Fragments.TodayFragment;
import timetable.Fragments.TomorrowFragment;
import timetable.TimetableActivity;
import timetable.TimetableSlot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment {


    String studentId, studentName;
    String studentAvatar, studentLevel;
    Context context;
    ProgressDialog progress;
    ArrayList<CourseGroup> courseGroups;
    SharedPreferences sharedPreferences;
    ApiInterface apiService;
    String attendance;
    int absentDays;
    String totalGrade;
    TextView totalGradeText;
    ImageView studentAvatarImage;
    TextView studentLevelView;
    TextView studentNameView;
    TextView badgesNumber;
    TextView nextSlot;
    TextView positiveNotesCounter;
    TextView negativeNotesCounter;

    TextView attendaceText;
    TextView attendanceLabel;
    TextView timetableLabel;
    TextView gradesLabel;
    TextView behaviorNotesLabel;

    Button badgesButton;
    LinearLayout attendanceLayer;
    LinearLayout gradesLayer;
    LinearLayout timeTableLayer;
    LinearLayout notesLayer;
    DrawerLayout notificationLayout;
    ListView notificationList;
    ActionBar studentFragmentActionBar ;
    final String thursdayKey ="thursday";
    final String badgeNameKey = "badge_name";
    final String GuruKey = "Guru";
    final String grandMaesterKey = "Grand Maester";
    final String studentIdKey = "student_id";
    final String studentNameKey = "student_name";
    final String studentAvatarKey = "student_avatar";
    final String studentLevelKey = "student_level";
    final String attendancesKey = "attendances";
    final String courseGroupsKey = "courseGroups";
    final String positiveNotesListKey = "positiveNotesList";
    final String negativeNotesListKey = "negativeNotesList";
    final String curUserKey = "cur_user";

    public static Handler handler;
    int servicesCount;
    private static final int servicesNumber = 5;
    public static Integer messageNumber = 0;

    ActionBarDrawerToggle notificationToggle;
    List<NotificationModel> notifications;
    ArrayList<Badge> badges;

    public  List<TimetableSlot> todaySlots;
    public  List<TimetableSlot> tomorrowSlots;

    public  List<BehaviorNote> positiveNotesList;
    public static List<BehaviorNote> negativeNotesList;

    ProgressBar attendanceProgress;

    public StudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment StudentFragment.
     */
    public static StudentFragment newInstance() {
        StudentFragment studentFragment = new StudentFragment();
        Bundle args = new Bundle();
        studentFragment.setArguments(args);
        return studentFragment;
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        badges = new ArrayList<Badge>();
        todaySlots = new ArrayList<TimetableSlot>();
        tomorrowSlots = new ArrayList<TimetableSlot>();

        positiveNotesList = new ArrayList<BehaviorNote>();
        negativeNotesList = new ArrayList<BehaviorNote>();


        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);

        Bundle extras= this.getActivity().getIntent().getExtras();


        studentId = extras.getString(studentIdKey);
        studentName = extras.getString(studentNameKey);
        studentAvatar = extras.getString(studentAvatarKey);
        studentLevel = extras.getString(studentLevelKey);
        attendance = extras.getString(attendancesKey);
        courseGroups = new ArrayList<CourseGroup>();

        sharedPreferences = SharedPreferenceUtils.getSharedPreference(getActivity(),"cur_user" );
        apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_student, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        servicesCount = 0;
        TextView notificationNumberText= (TextView) getActivity().findViewById(R.id.student_notification_number);

        if (MyKidsActivity.notificationNumber == 0) {
            notificationNumberText.setVisibility(View.INVISIBLE);
        } else  {
            notificationNumberText.setVisibility(View.VISIBLE);
        }

        RelativeLayout notificationButton =  (RelativeLayout) getActivity().findViewById(R.id.student_relative_layout);

        Typeface robotoMedium = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");


        attendanceLabel = (TextView) view.findViewById(R.id.attendance_lable);
        timetableLabel = (TextView) view.findViewById(R.id.time_table_lable);
        gradesLabel = (TextView) view.findViewById(R.id.grades_lable);
        behaviorNotesLabel = (TextView) view.findViewById(R.id.behavior_notes_lable);
        attendaceText = (TextView) view.findViewById(R.id.attendance_ratio_text);




        attendanceLabel.setTypeface(robotoMedium);
        timetableLabel.setTypeface(robotoMedium);
        gradesLabel.setTypeface(robotoMedium);
        behaviorNotesLabel.setTypeface(robotoMedium);


        attendanceLabel.setText(R.string.fragmentStudentAttendance_tv);
        timetableLabel.setText(R.string.fragmentStudentTimetable_tv);
        gradesLabel.setText(R.string.fragmentStudentGrades_tv);
        behaviorNotesLabel.setText(R.string.fragmentStudentBehaviorNotes_tv);

        attendanceLayer = (LinearLayout) view.findViewById(R.id.attendance_layout);
        gradesLayer = (LinearLayout) view.findViewById(R.id.grades_layout);
        timeTableLayer = (LinearLayout) view.findViewById(R.id.time_table_layout);
        notesLayer = (LinearLayout) view.findViewById(R.id.behavior_notes_layout);


        studentAvatarImage = (ImageView) view.findViewById(R.id.home_student_avatar);
        studentLevelView = (TextView) view.findViewById(R.id.home_student_level);
        studentNameView = (TextView) view.findViewById(R.id.home_student_name);
        nextSlot = (TextView) view.findViewById(R.id.next_slot);







        positiveNotesCounter = (TextView) view.findViewById(R.id.positive_notes_counter);
        negativeNotesCounter = (TextView) view.findViewById(R.id.negative_notes_counter);

        badgesButton = (Button) view.findViewById(R.id.badges_button);
        badgesNumber = (TextView) view.findViewById(R.id.badges_number);
        totalGradeText = (TextView) view.findViewById(R.id.average_grade);


        totalGradeText.setTypeface(robotoRegular);
        attendaceText.setTypeface(robotoRegular);
        nextSlot.setTypeface(robotoRegular);

        notificationLayout = (DrawerLayout) view.findViewById(R.id.student_drawer_layout);
        notificationList = (ListView) view.findViewById(R.id.student_listview_notification);
        notificationLayout.setDrawerListener(notificationToggle);
        notificationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(notificationLayout.isDrawerOpen(notificationList)){
                    notificationLayout.closeDrawer(notificationList);
                } else {
                    new NotificationsAsyncTask().execute();
                    NotificationManager nMgr = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    nMgr.cancelAll();
                    MyKidsActivity.notificationNumber = 0;
                    changeTheNotificationNumber();
                    new MarkAllAsSeenAsyncTask().execute();
                    notificationLayout.openDrawer(notificationList);
                }
            }
        });


        handler = new Handler();
        handler.postDelayed(updateNotificationRunnable, 500); //Every 120000 ms (2 minutes)


        Button messagesButton = (Button) view.findViewById(R.id.student_home_message_notification);
        messagesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                TextView messagecnt = (TextView) view.findViewById(R.id.student_home_messaage_count_text);
                messagecnt.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getActivity(), AskTeacherActivity.class);
                startActivity(intent);
            }
        });

        attendanceProgress = (ProgressBar) view.findViewById(R.id.attendance_progress);
        studentNameView.setText(studentName);
        studentLevelView.setText(studentLevel);

        if(studentAvatar.substring(0,8).equals("/uploads")) {
            studentAvatar = ApiClient.BASE_URL + studentAvatar;
        }
        com.squareup.picasso.Callback callback = new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                TextView studentAvatarName = (TextView) view.findViewById(R.id.st_home_text_name);
                studentAvatarImage.setVisibility(View.GONE);
                String[] names = studentName.split(" ");
                studentAvatarName.setVisibility(View.VISIBLE);
                studentAvatarName.setText("" +names[0].charAt(0) + names[1].charAt(0) );

            }
        };
        ImageViewHelper.getImageFromUrlWithCallback(getActivity(),studentAvatar,studentAvatarImage,callback);


        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(attendance);
        final JsonArray attenobdances = tradeElement.getAsJsonArray();
        Set<Date> attendaceDates = new HashSet<>();
        absentDays=0;

        for(JsonElement element: attenobdances){
            JsonObject day = element.getAsJsonObject();
            Date date = new Date();
            date.setTime(day.get("date").getAsLong());
            if(!attendaceDates.contains(date)){
                if(day.get("status").getAsString().equals("absent"))
                    absentDays++;
            }
            attendaceDates.add(date);
        }

        context = getActivity();

        if (attendaceDates.size() != 0)
            attendanceProgress.setProgress((absentDays*100)/attendaceDates.size());
        attendaceText.setText("Absent " + absentDays + " out " + attendaceDates.size() +" days");


        attendanceLayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent attendanceIntent = new Intent(getActivity(), AttendanceActivity.class);
                attendanceIntent.putExtra(attendancesKey,attendance);
                startActivity(attendanceIntent);
            }
        });

        gradesLayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent gradesIntent = new Intent(getActivity(), GradesAvtivity.class);
                gradesIntent.putExtra(studentIdKey, studentId);
                gradesIntent.putExtra(courseGroupsKey, courseGroups);
                startActivity(gradesIntent);
            }
        });



        notesLayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent behaviorNotesIntent = new Intent(getActivity(), BehaviorNotesActivity.class);
                behaviorNotesIntent.putExtra(studentIdKey, studentId);
                Bundle bundle = new Bundle();
                bundle.putSerializable(positiveNotesListKey, (Serializable) positiveNotesList);
                bundle.putSerializable(negativeNotesListKey, (Serializable) negativeNotesList);
                behaviorNotesIntent.putExtras(bundle);
                startActivity(behaviorNotesIntent);
            }
        });



        timeTableLayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent timeTableIntent = new Intent(getActivity(), TimetableActivity.class);
                timeTableIntent.putExtra(studentIdKey, studentId);
                Bundle bundle = new Bundle();
                bundle.putSerializable(TomorrowFragment.KEY_NAME, (Serializable) tomorrowSlots);
                bundle.putSerializable(TodayFragment.KEY_NAME, (Serializable) todaySlots);
                timeTableIntent.putExtras(bundle);
                startActivity(timeTableIntent);
            }
        });

        badgesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                BadgesFragment badgesDialog = BadgesFragment.newInstance(badges);
                badgesDialog.show(fragmentManager,"fragment_badges");

                //BadgesDialog.AlertDialog(context , badges);
            }
        });



        if (InternetConnection.isInternetAvailable(getActivity())) {
            new StudentAsyncTask().execute();
        } else {
            Dialogue.AlertDialog(getActivity(),getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
        }


    }

    public Runnable updateNotificationRunnable = new Runnable() {
        public void run() {

            TextView notificationNumberText= (TextView) getActivity().findViewById(R.id.student_notification_number);

            TextView messagecnt = (TextView) getActivity().findViewById(R.id.student_home_messaage_count_text);


            if (MyKidsActivity.notificationNumber == 0) {
                notificationNumberText.setVisibility(View.INVISIBLE);
            } else  {
                notificationNumberText.setVisibility(View.VISIBLE);
            }

            if (messageNumber == 0) {
                messagecnt.setVisibility(View.INVISIBLE);
            } else  {
                messagecnt.setVisibility(View.VISIBLE);
            }
            Typeface roboto = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");

            Toolbar studentFragmentToolbar = (Toolbar) getActivity().findViewById(R.id.custom_toolbar_id);
            ((AppCompatActivity)getActivity()).setSupportActionBar(studentFragmentToolbar);
            studentFragmentActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            studentFragmentActionBar.setDisplayHomeAsUpEnabled(true);
            if(notificationLayout.isDrawerOpen(notificationList)){
                SpannableString title = new SpannableString(getString(R.string.notificationString));
                title.setSpan(roboto,0,title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                studentFragmentActionBar.setTitle(title);
            } else {
                SpannableString title = new SpannableString(studentName);
                title.setSpan(roboto,0,title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                studentFragmentActionBar.setTitle(title);
            }

            notificationNumberText.setText( MyKidsActivity.notificationNumber.toString());
            messagecnt.setText( messageNumber.toString());

            handler.postDelayed(this, 0); //now is every 2 minutes
        }
    } ;


    public void loading(){
        progress.setTitle(R.string.LoadDialogueTitle);
        progress.setMessage(getString(R.string.LoadDialogueBody));
    }

    public void getStudentCourseGroups(){
        String url = "api/students/" + studentId + "/course_groups";
        Map<String, String> params = new HashMap<>();
        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);

        call.enqueue(new Callback<ArrayList<JsonObject> >() {

            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                int statusCode = response.code();
                if(statusCode == 401) {
                    Dialogue.AlertDialog(context,getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
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
                }
                getStudentGrades();
                servicesCount++;
                if(servicesCount==servicesNumber)
                    progress.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Dialogue.AlertDialog(context,getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
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
                    Dialogue.AlertDialog(context,getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
                } else {
                    if (statusCode == 200) {
                        int i = 0;
                        for (; i < response.body().size()-1; i++) {
                            JsonObject courseData = response.body().get(i);
                            if(courseData.has("course_id"))
                            for(int j = 0 ; j < courseGroups.size() ; j++){
                                if(courseGroups.get(j).getCourseId() == courseData.get("course_id").getAsInt() && !courseData.get("grade").isJsonArray()) {
                                    courseGroups.get(j).setGrade(courseData.get("grade").getAsString());
                                    if (courseData.get("icon").toString().equals("null"))
                                        courseGroups.get(j).setIcon("dragon");
                                    else
                                        courseGroups.get(j).setIcon(courseData.get("icon").getAsString());
                                } else {
                                    courseGroups.get(j).setIcon("non");
                                }
                            }

                        }
                       // totalGrade = response.body().get(i).get("total_grade").getAsString();
                        totalGrade = "F";
                        totalGradeText.setText("Average grade:  "+totalGrade);
                    }
                }
                servicesCount++;
                if(servicesCount==servicesNumber)
                    progress.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Dialogue.AlertDialog(context,getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
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
                    Dialogue.AlertDialog(context, getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
                } else if (statusCode == 200) {
                    Calendar calendar = CalendarUtils.getCalendarWithoutDate();
                    Date date = calendar.getTime();
                    String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
                    calendar.add( Calendar.DATE, 1 );
                    date = calendar.getTime();
                    String tomorrow = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
                    today = today.toLowerCase();
                    tomorrow = tomorrow.toLowerCase();
                    if (today.equals(thursdayKey)){
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

                    for (TimetableSlot timeSlotIterator : todaySlots){
                        if ((timeSlotIterator.getFrom().getHours() == current.getHours() && timeSlotIterator.getFrom().getMinutes() >= current.getMinutes()) ||
                                timeSlotIterator.getFrom().getHours() > current.getHours()){
                            nextSlotFound = true;
                            nextSlot.setText("Next: " + timeSlotIterator.getCourseName() + ", " + timeSlotIterator.getDay() + " " + dateFormat.format(timeSlotIterator.getFrom()));
                            break;
                        }
                    }
                    if(!nextSlotFound){
                        TimetableSlot timeSlot = tomorrowSlots.get(0);
                        nextSlot.setText("Next: " + timeSlot.getCourseName() + ", " + timeSlot.getDay() + " " + dateFormat.format(timeSlot.getFrom()));
                    }
                }
                servicesCount++;
                if(servicesCount==servicesNumber)
                    progress.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Dialogue.AlertDialog(context,getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
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
                //progress.dismiss();
                int statusCode = response.code();
                if(statusCode == 401) {
                    Dialogue.AlertDialog(context,getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
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
                servicesCount++;
                if(servicesCount==servicesNumber)
                    progress.dismiss();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progress.dismiss();
                Dialogue.AlertDialog(context,getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
            }
        });
    }

    public void getStudentBadges(){
        String url = "/api/badges/get_by_student";
        Map<String, String> params = new HashMap<>();
        params.put("student_id" , studentId);

        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);
        call.enqueue(new Callback<ArrayList<JsonObject> >() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                // must be called in the last service
                int statusCode = response.code();
                if(statusCode == 401) {
                    Dialogue.AlertDialog(context,getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
                } else {
                    if (statusCode == 200) {
                        for (int i = 0 ; i < response.body().size(); i++) {
                            JsonObject jsonBadge = response.body().get(i);
                            String reason;
                            if (jsonBadge.get(badgeNameKey).getAsString().equals(GuruKey)){
                                reason = "High performance in a quiz";
                            } else if (jsonBadge.get(badgeNameKey).getAsString().equals(grandMaesterKey)){
                                reason = "Excellent performance in total grade";
                            } else {
                                reason = "Active user of Skolera";
                            }
                            badges.add(new Badge(jsonBadge.get("badge_name").getAsString(),
                                    jsonBadge.get("badge_icon").getAsString(),
                                    reason,
                                    jsonBadge.get("course_name").getAsString()));
                        }
                        badgesNumber.setText(badges.size()+"");
                    }
                }
                servicesCount++;
                if(servicesCount==servicesNumber)
                    progress.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                progress.dismiss();
                Dialogue.AlertDialog(context,getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
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
            getStudentTimeTable();
            getStudentBehaviorNotes();
            getStudentBadges();
            return null;
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

            SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(getActivity(),"cur_user" );
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            String id = SharedPreferenceUtils.getStringValue("user_id", "",sharedPreferences);
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
                        Dialogue.AlertDialog(context,getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
                    } else if (statusCode == 200) {
                        notifications = new  ArrayList<NotificationModel>();
                        JsonObject notificationsRespone = response.body();


                        for (JsonElement pa : notificationsRespone.get("notifications").getAsJsonArray()) { // gest needed data from assig, quizz, grade item
                            JsonObject notificationObj = pa.getAsJsonObject();
                            try {
                                String studentNames = "";
                                if (!notificationObj.get("additional_params").isJsonNull()) {
                                    JsonObject additionalParams = notificationObj.getAsJsonObject("additional_params");
                                    int i = 0 , len = additionalParams.get("studentNames").getAsJsonArray().size() ;
                                    for (JsonElement name : additionalParams.get("studentNames").getAsJsonArray()) {
                                        studentNames += name.getAsString();
                                        if (i > 0 && i != len - 1) {
                                            studentNames += ", ";
                                        }
                                    }

                                }
                                notifications.add(new NotificationModel(notificationObj.get("text").getAsString(), notificationObj.get("created_at").getAsString() ,notificationObj.get("logo").getAsString(), studentNames ,notificationObj.get("message").getAsString() ));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        NotificationAdapter notificationAdapter = new NotificationAdapter(context, R.layout.notification_list_item,notifications);
                        ListView listView = (ListView) getView().findViewById(R.id.student_listview_notification);
                        listView.setAdapter(notificationAdapter);

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progress.dismiss();
                    Dialogue.AlertDialog(context,getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
                }


            });
            return null;
        }
    }


    private class MarkAllAsSeenAsyncTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading();

        }

        protected void onProgressUpdate(String... progress) {
            loading();
        }

        @Override
        protected List<Student> doInBackground(Object... param) {

            SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(getActivity(), curUserKey );
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            String id = SharedPreferenceUtils.getStringValue("user_id", "",sharedPreferences);
            String url ="/api/users/"+id +"/notifications/mark_as_seen";
            Map <String, String> params = new HashMap<>();
            params.put("type" , "android");
            Call<JsonObject>  call = apiService.postServise(url, params);

            call.enqueue(new Callback<JsonObject> () {

                @Override
                public void onResponse(Call<JsonObject>  call, Response<JsonObject>  response) {

                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
                    } else if (statusCode == 200) {

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }


            });
            return null;
        }


    }

    public  void changeTheNotificationNumber() {
        TextView notificationNumberText= (TextView) getActivity().findViewById(R.id.student_notification_number);
        notificationNumberText.setText( MyKidsActivity.notificationNumber.toString());
        Typeface roboto = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf"); //use this.getAssets if you are calling from an Activity
        notificationNumberText.setTypeface(roboto);
    }


    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(updateNotificationRunnable);
    }
}
