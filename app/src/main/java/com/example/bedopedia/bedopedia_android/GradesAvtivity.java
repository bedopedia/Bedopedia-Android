package com.example.bedopedia.bedopedia_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.GradesAdapter;
import Adapters.MyKidsAdapter;
import Models.Course;
import Models.Student;
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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades);
        Bundle extras= getIntent().getExtras();
        student_id = extras.getString("student_id");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO go to another activity
            }
        });

        courses = new ArrayList<Course>();
        final Context context = this;

        SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        String url = "api/students/" + student_id + "/grade_certificate";
        Map<String, String> params = new HashMap<>();
        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);

        call.enqueue(new Callback<ArrayList<JsonObject> >() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                int statusCode = response.code();
                if(statusCode == 401) {

                    String errorText = "wrong username or password";
                    Toast.makeText(getApplicationContext(),errorText,Toast.LENGTH_SHORT).show();
                } else {
                    if (statusCode == 200) {
                        for (int i = 0; i < response.body().size()-1; i++) {
                            JsonObject courseData = response.body().get(i);
                            Log.d("grades: " , ""+courseData);
                            Log.d("grades: ", ""+courseData.get("name"));
                            courses.add(new Course(
                                    courseData.get("name").toString().substring(1,courseData.get("name").toString().length()-1),
                                    "",
                                    0,
                                    courseData.get("grade").toString().substring(1,courseData.get("grade").toString().length()-1)
                            ));
                        }

                        GradesAdapter adapter = new GradesAdapter(context, R.layout.single_grade, courses);
                        GridView gradesList = (GridView) findViewById(R.id.grades_list);
                        gradesList.setAdapter(adapter);


                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"connection failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
