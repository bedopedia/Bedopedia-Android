package com.example.bedopedia.bedopedia_android;

/**
 * Created by mohamed on 2/9/17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import Adapters.MyKidsAdapter;
import Models.Student;
import Services.ApiClient;
import Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyKidsActivity extends AppCompatActivity{

    List<Student> myKids;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_kids);
        setTitle("My kids");
        myKids = new ArrayList<Student>();

        final Context context = this;

        SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        id = sharedPreferences.getString("id", "");
        String url = "api/parents/" + id + "/children";
        Map <String, String> params = new HashMap<>();
        params.put("parent_id" , id);
        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);

        call.enqueue(new Callback<ArrayList<JsonObject> >() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                int statusCode = response.code();
                if(statusCode == 401) {

                    String errorText = "wrong username or password";
                    Toast.makeText(getApplicationContext(),errorText,Toast.LENGTH_SHORT).show();
                } else if (statusCode == 200) {
                    for (int i = 0 ; i < response.body().size() ; i++){
                        JsonObject studentData = response.body().get(i);
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

                    MyKidsAdapter adapter = new MyKidsAdapter(context, R.layout.single_student, myKids);
                    GridView studentsList = (GridView) findViewById(R.id.students_list);
                    studentsList.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"connection failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void itemClicked (int index){
        Intent intent = new Intent(this, GradesAvtivity.class);
        intent.putExtra("student_id", String.valueOf(myKids.get(index).getId()));
        startActivity(intent);

//        Toast.makeText(getApplicationContext(),"you clicked " + myKids.get(index).getFirstName(),
//        Toast.LENGTH_LONG).show();
    }
}
