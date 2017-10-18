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

import com.example.bedopedia.bedopedia_android.AskTeacherActivity;
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
    Toolbar myKidsToolbar ;
    DrawerLayout drawer;
    ListView notificationList;
    ActionBarDrawerToggle notificationToggle;
    ActionBarDrawerToggle mainToggle;
    List<NotificationModel> notifications;
    public Handler handler ;

    final String curUserKey = "cur_user";


    final String BaseUrlKey = "Base_Url";
    final String headerAccessTokenKey = "header_access-token";
    final String headerTokenTypeKey = "header_token-type";
    final String headerClientKey = "header_client";
    final String headerUidKey = "header_uid";
    final String userIdKey = "user_id";
    final String idKey = "id";
    final String usernameKey = "username";
    final String emailKey = "email";
    final String avatarUrlKey = "avatar_url";
    final String userDataKey = "user_data";
    final String isLoggedInKey = "is_logged_in";
    final String studentIdKey = "student_id";



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
                                    studentData.get("today_workload_status").getAsJsonObject(),
                                    0,null,null));
                        }


                        mRecyclerView = (RecyclerView) findViewById(R.id.mykids_recycler_view);
                        mLayoutManager = new LinearLayoutManager(context);
                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new MyKidsRecyclerViewAdapter(context, myKids,kidsAttendances);
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
            Map <String, Object> params = new HashMap<>();
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
        TextView notificationNumberText= (TextView) findViewById(R.id.my_kids_notification_number);
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


        SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(MyKidsActivity.this, curUserKey );
        String email =  sharedPreferences.getString(emailKey, "");
        String avatarUrl = sharedPreferences.getString(avatarUrlKey, "");

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        TextView emailView = (TextView) hView.findViewById(R.id.my_email);
        ImageView myAvatar = (ImageView) hView.findViewById(R.id.my_avatar_photo);

        emailView.setText(email);

        ImageViewHelper.getImageFromUrlWithIdFailure(context,avatarUrl, myAvatar, R.drawable.student);

        myKidsToolbar = (Toolbar) findViewById(R.id.custom_toolbar_my_kids_id);
        myKidsToolbar.setTitle("");
        setSupportActionBar(myKidsToolbar);
        TextView myKidsTitle = (TextView)findViewById(R.id.student_title);
        myKidsTitle.setText("My Kids");

        ActionBar myKidsActionbar = getSupportActionBar();
        myKidsActionbar.setDisplayHomeAsUpEnabled(true);
//        myKidsActionbar.setTitle(R.string.MyKidsTitle);

        TextView notificationNumberText= (TextView) findViewById(R.id.my_kids_notification_number);

        if (MyKidsActivity.notificationNumber == 0) {
            notificationNumberText.setVisibility(View.INVISIBLE);
        } else  {
            notificationNumberText.setVisibility(View.VISIBLE);
        }

        Button notificationButton =  (Button) findViewById(R.id.my_kids_action_bar_notification);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainToggle = new ActionBarDrawerToggle(
                this, drawer, myKidsToolbar , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
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
        handler = new Handler();
        handler.postDelayed( updateNotification, 500); //Every 120000 ms (2 minutes)z
    }




    Runnable updateNotification = new Runnable() {
        public void run() {
            TextView notificationNumberText= (TextView) findViewById(R.id.my_kids_notification_number);
            TextView myKidsTitle = (TextView)findViewById(R.id.student_title);
            if (MyKidsActivity.notificationNumber == 0) {
                notificationNumberText.setVisibility(View.INVISIBLE);
            } else  {
                notificationNumberText.setVisibility(View.VISIBLE);
            }

            Typeface roboto = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
            if(drawer.isDrawerOpen(notificationList)){
//                SpannableString drawerTitle = new SpannableString((getString(R.string.NotificationString)));
//                drawerTitle.setSpan(roboto,0,drawerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                myKidsToolbar.setTitle(drawerTitle);
                  myKidsTitle.setText((getString(R.string.NotificationString)));
            } else {
//                SpannableString title = new SpannableString(getString(R.string.MyKidsTitle));
//                title.setSpan(roboto,0,title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                myKidsToolbar.setTitle(title);
                myKidsTitle.setText("My Kids");
            }

            notificationNumberText.setText( MyKidsActivity.notificationNumber.toString());
            handler.postDelayed(this, 0); //now is every 2 minutes
        }
    };



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {

            SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(MyKidsActivity.this, "cur_user" );
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(BaseUrlKey, "");
            editor.putString(headerAccessTokenKey, "");
            editor.putString(headerTokenTypeKey, "");
            editor.putString(headerClientKey, "");
            editor.putString(headerUidKey, "");
            editor.putString(userIdKey, "");
            editor.putString(idKey, "");
            editor.putString(usernameKey, "");
            editor.putString(emailKey, "");
            editor.putString(avatarUrlKey, "");
            editor.putString(userDataKey, "");
            editor.putString(isLoggedInKey, "false");

            editor.commit();

            Intent schoolCodeIntent = new Intent(this, schoolCode.class);
            startActivity(schoolCodeIntent);

        } else if (id == R.id.nav_message){
            Intent intent = new Intent(this, AskTeacherActivity.class);
            intent.putExtra(studentIdKey, "none");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateNotification);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return  true ;
    }







}