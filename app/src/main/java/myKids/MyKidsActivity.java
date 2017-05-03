package myKids;

/**
 * Created by mohamed on 2/9/17.
 */

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import Tools.SharedPreferenceUtils;
import student.StudentActivity;

import Tools.ImageViewHelper;
import login.schoolCode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.NotificationAdapter;
import Models.NotificationModel;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import Tools.Dialogue;
import Tools.InternetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyKidsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ArrayList<Student> myKids;
    String id;
    Context context;
    ProgressDialog progress;
    ArrayList<JsonArray> kidsAttendances;
    public static Integer notificationNumber = 0;
    Toolbar tb ;
    DrawerLayout drawer;
    ListView notificationList;
    ActionBarDrawerToggle notificationToggle;
    ActionBarDrawerToggle mainToggle;
    List<NotificationModel> notifications;
    ImageButton menuButton;

    private RecyclerView mRecyclerView;
    private MyKidsRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    public void loading(){
        progress.setTitle(R.string.LoadDialogueTitle);
        progress.setMessage(getString(R.string.LoadDialogueBody));
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

            SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(MyKidsActivity.this, "cur_user" );
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            id =  SharedPreferenceUtils.getStringValue("id", "",sharedPreferences);

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
                        Dialogue.AlertDialog(context,getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
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


                        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                        mLayoutManager = new LinearLayoutManager(context);
                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new MyKidsRecyclerViewAdapter(context, myKids);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                    progress.dismiss();
                    Dialogue.AlertDialog(context,getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
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

            SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(MyKidsActivity.this, "cur_user" );
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            id = SharedPreferenceUtils.getStringValue("user_id", "",sharedPreferences);
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
                                JsonObject additionalParams = notificationObj.getAsJsonObject("additional_params");
                                String studentNames = "";
                                int i = 0 , len = additionalParams.get("studentNames").getAsJsonArray().size() ;
                                for (JsonElement name : additionalParams.get("studentNames").getAsJsonArray()) {
                                    studentNames += name.getAsString();
                                    if (i > 0 && i != len - 1) {
                                        studentNames += ", ";
                                    }
                                }
                                    notifications.add(new NotificationModel(notificationObj.get("text").getAsString(), notificationObj.get("created_at").getAsString() ,notificationObj.get("logo").getAsString(), studentNames ,notificationObj.get("message").getAsString() ));
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
            SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(MyKidsActivity.this, "cur_user" );
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            id = sharedPreferences.getString("user_id", "");
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
        TextView notificationNumberText= (TextView) findViewById(R.id.student_notification_number);
        notificationNumberText.setText( MyKidsActivity.notificationNumber.toString());
        Typeface roboto = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf"); //use this.getAssets if you are calling from an Activity
        notificationNumberText.setTypeface(roboto);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_kids);

        myKids = new ArrayList<Student>();
        context = this;
        progress = new ProgressDialog(this);
        kidsAttendances = new ArrayList<JsonArray>();

        SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(MyKidsActivity.this, "cur_user" );
        String email =  sharedPreferences.getString("email", "");
        String avatarUrl = sharedPreferences.getString("avatar_url", "");

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        TextView emailView = (TextView) hView.findViewById(R.id.my_email);
        ImageView myAvatar = (ImageView) hView.findViewById(R.id.my_avatar_photo);

        emailView.setText(email);
        ImageViewHelper.getImageFromUrl(context,avatarUrl, myAvatar );

        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.MyKidsTitle);

        TextView notificationNumberText= (TextView) findViewById(R.id.student_notification_number);

        if (MyKidsActivity.notificationNumber == 0) {
            notificationNumberText.setVisibility(View.INVISIBLE);
        } else  {
            notificationNumberText.setVisibility(View.VISIBLE);
        }

        Button notificationButton =  (Button) findViewById(R.id.student_action_bar_notification);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainToggle = new ActionBarDrawerToggle(
                this, drawer, tb , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(mainToggle);
        mainToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        notificationList = (ListView) findViewById(R.id.listview_notification);
        drawer.setDrawerListener(notificationToggle);

        notificationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(drawer.isDrawerOpen(notificationList)){
                    drawer.closeDrawer(notificationList);
                } else {
                        new NotificationsAsyncTask().execute();
                        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        nMgr.cancelAll();
                        notificationNumber = 0;
                        changeTheNotificationNumber();
                        new MarkAllAsSeenAsyncTask().execute();
                        drawer.openDrawer(notificationList);
                }
                if(drawer.isDrawerOpen(navigationView)) {
                    drawer.closeDrawer(navigationView);
                }
            }
        });

        drawer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(drawer.isDrawerOpen(navigationView)){
                    drawer.closeDrawer(navigationView);
                } else {
                    drawer.openDrawer(navigationView);
                }
                if(drawer.isDrawerOpen(notificationList)) {
                    drawer.closeDrawer(notificationList);
                }
            }
        });

        if (InternetConnection.isInternetAvailable(this)){
            new KidsAsyncTask().execute();
        } else {
            Dialogue.AlertDialog(this,getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                TextView notificationNumberText= (TextView) findViewById(R.id.student_notification_number);
                if (MyKidsActivity.notificationNumber == 0) {
                    notificationNumberText.setVisibility(View.INVISIBLE);
                } else  {
                    notificationNumberText.setVisibility(View.VISIBLE);
                }

                Typeface roboto = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
                if(drawer.isDrawerOpen(notificationList)){
                    SpannableString title = new SpannableString((getString(R.string.NotificationString)));
                    title.setSpan(roboto,0,title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tb.setTitle(title);
                } else {
                    SpannableString title = new SpannableString("My Kids");
                    title.setSpan(roboto,0,title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tb.setTitle(title);
                }

                notificationNumberText.setText( MyKidsActivity.notificationNumber.toString());
                handler.postDelayed(this, 0); //now is every 2 minutes
            }
        }, 500); //Every 120000 ms (2 minutes)

    }

    public void itemClicked (int index){
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra("student_id", String.valueOf(myKids.get(index).getId()));
        intent.putExtra("student_name", myKids.get(index).getFirstName() + " " + myKids.get(index).getLastName());
        intent.putExtra("student_avatar", myKids.get(index).getAvatar());
        intent.putExtra("student_level", myKids.get(index).getLevel());
        intent.putExtra("attendances",kidsAttendances.get(index).toString());
        startActivity(intent);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(MyKidsActivity.this, "cur_user" );
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Base_Url", "");
            editor.putString("header_access-token", "");
            editor.putString("header_token-type", "");
            editor.putString("header_client", "");
            editor.putString("header_uid", "");
            editor.putString("user_id", "");
            editor.putString("id", "");
            editor.putString("username", "");
            editor.putString("email", "");
            editor.putString("avatar_url", "");
            editor.putString("user_data", "");
            editor.putString("is_logged_in", "false");

            editor.commit();

            Intent intent = new Intent(this, schoolCode.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return  true ;
    }
}