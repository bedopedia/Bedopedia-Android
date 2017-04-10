package grades;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.bedopedia.bedopedia_android.ActivityCourse;
import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Tools.Dialogue;
import Models.Course;
import Services.ApiClient;
import Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohamedkhaled on 2/13/17.
 */

public class GradesAvtivity extends AppCompatActivity {
    List<Course> courses;
    ImageButton back;
    String student_id;
    ProgressDialog progress;
    public static Context context;
    List<CourseGroup> courseGroups;

    public void loading(){
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
    }

    private class GradesAsyncTask extends AsyncTask {

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
        protected List<Course> doInBackground(Object... param) {

            SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            String url = "api/students/" + student_id + "/grade_certificate";
            Map<String, String> params = new HashMap<>();
            Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);
            call.enqueue(new Callback<ArrayList<JsonObject> >() {
                @Override
                public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                    progress.dismiss();
                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,"Not Authorized","you don't have the right to do this");
                    } else {
                        if (statusCode == 200) {
                            for (int i = 0; i < response.body().size()-1; i++) {
                                JsonObject courseData = response.body().get(i);
                                courses.add(new Course(
                                        courseData.get("name").toString().substring(1,courseData.get("name").toString().length()-1),
                                        "",
                                        0,
                                        courseData.get("grade").toString().substring(1,courseData.get("grade").toString().length()-1),
                                        courseData.get("icon").toString().substring(1,courseData.get("icon").toString().length()-1)
                                ));
                            }


                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                    progress.dismiss();
                    Dialogue.AlertDialog(context,"Connection Failed","Check your Netwotk connection and Try again");
                }
            });
            return courses;
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades);
        progress = new ProgressDialog(this);
        Bundle extras= getIntent().getExtras();
        student_id = extras.getString("student_id");
        courseGroups = (List<CourseGroup>) getIntent().getSerializableExtra("courseGroups");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        courses = new ArrayList<Course>();
        context = this;

        GradesAdapter adapter = new GradesAdapter(context, R.layout.single_grade, courseGroups);
        ListView gradesList = (ListView) findViewById(R.id.grades_list);
        gradesList.setAdapter(adapter);


    }

    public void itemClicked(int position){
        Intent i =  new Intent(getApplicationContext(), ActivityCourse.class);
        i.putExtra("student_id",student_id);
        i.putExtra("course_group_id", String.valueOf(courseGroups.get(position).getId()));
        i.putExtra("course_id", String.valueOf(courseGroups.get(position).getCourseId()));
        i.putExtra("course_name", courseGroups.get(position).getCourseName());
        startActivity(i);
    }

}
