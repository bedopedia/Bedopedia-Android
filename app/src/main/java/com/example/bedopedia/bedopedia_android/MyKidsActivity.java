package com.example.bedopedia.bedopedia_android;

/**
 * Created by mohamed on 2/9/17.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.MyKidsAdapter;
import Models.Student;
import Services.ApiClient;
import Services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.data;
import static android.R.attr.id;

public class MyKidsActivity extends AppCompatActivity{

    List<Student> myKids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_kids);
        setTitle("My kids");
        myKids = new ArrayList<Student>();
        myKids.add(new Student("Heba", "Ashraf", "female", "h@gmail.com", "avatar", "student", "level-0", "Section A", "Stage-1", 25, null, null));
        myKids.add(new Student("Abeer", "El-sayed", "female", "a@gmail.com", "avatar", "student", "level-1", "Section B", "Stage-2", 25, null, null));
        myKids.add(new Student("Khadeja", "Hussein", "female", "k@gmail.com", "avatar", "student", "level-2", "Section C", "Stage-3", 25, null, null));

        MyKidsAdapter adapter = new MyKidsAdapter(this, R.layout.single_student, myKids);
        GridView studentsList = (GridView) findViewById(R.id.students_list);
        studentsList.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
        String url = "api/parents/" + id + "/children";
        Map <String, String> m = new HashMap<>();
        m.put("parent_id" , "" + id);
        Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, m);
        call.enqueue(new Callback<ArrayList<JsonObject> >() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                int statusCode = response.code();
                if(statusCode == 401) {
                    String errorText = "wrong username or password";
                    Toast.makeText(getApplicationContext(),errorText,Toast.LENGTH_SHORT).show();
                } else if (statusCode == 200) {

                    JsonObject data = response.body().get(0);
//                    Log.e("data" , data.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"connection failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void itemClicked (int index){
        Toast.makeText(getApplicationContext(),"you clicked " + myKids.get(index).getFirstName(),
                Toast.LENGTH_LONG).show();
    }
}
