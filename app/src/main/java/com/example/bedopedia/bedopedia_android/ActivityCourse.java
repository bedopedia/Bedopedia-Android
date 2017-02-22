package com.example.bedopedia.bedopedia_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Adapters.CourseAdapter;
import Services.ApiClient;
import Services.ApiInterface;
import Tools.Dialogue;
import Tools.InternetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;






public class ActivityCourse extends AppCompatActivity {

    ProgressDialog progress;
    Context context;
    private Double totalStudent = 0.0;
    private Double totalCategory = 0.0 ;


    public void loading(){
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
    }



    private class GradeBookAsyncTask extends AsyncTask {

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
        protected Object doInBackground(Object... param) {

            Map<String,String> params = new HashMap();
            params.put("student_id","122");
            String url = "api/courses/9/course_groups/33/student_grade";

            SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            Call<JsonObject> call = apiService. getServise(url, params);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,"Not Authorized","you don't have the right to do this");
                    } else if (statusCode == 200) {
                        ArrayList<ArrayList<String>> header = new ArrayList<>();
                        ArrayList<ArrayList<Pair<String,String>>> courseItemsTempData = new ArrayList<ArrayList<Pair<String,String>>>();

                        JsonObject body = response.body();
                        JsonObject categories = (JsonObject) body.get("categories");

                        Set<Map.Entry<String, JsonElement>> entries = categories.entrySet();//will return members of your object
                        for (Map.Entry<String, JsonElement> entry: entries) { // loop through all categories
                            ArrayList<String> temp = new ArrayList<String>();
                            temp.add(entry.getKey().toString());


                            ArrayList<Pair<String,String>> assignmentTempData = new ArrayList<>();
                            JsonObject item = entry.getValue().getAsJsonObject();
                            if (item.has("assignments")) {
                                addAssignmentsToList(item.get("assignments").getAsJsonArray(), assignmentTempData, body.get("student").getAsJsonArray().get(0).getAsJsonObject().get("submitted_assignments").getAsJsonArray());
                            }

                            if (item.has("quizzes")) {
                                addQuizzesToList(item.get("quizzes").getAsJsonArray(), assignmentTempData,body.get("student").getAsJsonArray().get(0).getAsJsonObject().get("submitted_quizzes").getAsJsonArray());
                            }

                            if (item.has("grade_items")) {
                                addGradeItemsToList(item.get("grade_items").getAsJsonArray(), assignmentTempData, body.get("student").getAsJsonArray().get(0).getAsJsonObject().get("submitted_grades").getAsJsonArray());
                            }

                            if(totalStudent == totalStudent.intValue()){
                                temp.add(totalStudent.intValue()+"");
                            }else{
                                temp.add(totalStudent.toString());
                            }
                            if(totalCategory == totalCategory.intValue()){
                                temp.add(totalCategory.intValue()+"");
                            }else{
                                temp.add(totalCategory.toString());
                            }


                            totalCategory = 0.0;
                            totalStudent = 0.0;
                            header.add(temp);
                            courseItemsTempData.add(assignmentTempData);

                        }

                        CourseAdapter courseItemAdapter = new CourseAdapter(ActivityCourse.this, R.layout.activity_course, courseItemsTempData ,  header );
                        ListView courseListView = (ListView) findViewById(R.id.category_list_view);
                        courseListView.setAdapter(courseItemAdapter);


                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progress.dismiss();
                    Dialogue.AlertDialog(context,"Connection Failed","Check your Netwotk connection and Try again");
                }
            });

            return null;
        }


    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        progress = new ProgressDialog(this);
        context = this;
        if (InternetConnection.isInternetAvailable(this)){
            new GradeBookAsyncTask().execute();
        } else {
            Dialogue.AlertDialog(this,"No NetworkConnection","Check your Netwotk connection and Try again");
        }

    }

    private void addAssignmentsToList( JsonArray assignments ,  ArrayList<Pair<String,String>> data , JsonArray studentSubmission) {
        // points, max_grade, total_score
        for (JsonElement pa : assignments) { // gest needed data from assig, quizz, grade item
            JsonObject assignmentObj = pa.getAsJsonObject();

            String temp = "";
            temp = getStudentGrade(assignmentObj.get("id").getAsInt(), studentSubmission , "assignment_id", "grade");
            if (assignmentObj.get("points").getAsInt() == assignmentObj.get("points").getAsDouble()) {
                temp += "/" + assignmentObj.get("points").getAsInt();
            } else {
                temp += "/" + assignmentObj.get("points").getAsString();
            }
            totalCategory += assignmentObj.get("points").getAsDouble();

            data.add(new Pair<String, String>(assignmentObj.get("name").getAsString(), temp));
        }
    }

    private void addQuizzesToList( JsonArray quizzes ,  ArrayList<Pair<String,String>> data , JsonArray studentSubmission) {
        // points, max_grade, total_score
        for (JsonElement pa : quizzes) { // gest needed data from assig, quizz, grade item
            JsonObject quizObj = pa.getAsJsonObject();
            String temp = "";
            temp = getStudentGrade(quizObj.get("id").getAsInt(), studentSubmission , "quiz_id", "score");
            if (quizObj.get("total_score").getAsInt() == quizObj.get("total_score").getAsDouble()) {
                temp += "/" + quizObj.get("total_score").getAsInt();
            } else {
                temp += "/" + quizObj.get("total_score").getAsString();
            }

            totalCategory += quizObj.get("total_score").getAsDouble();


            data.add(new Pair<String, String>(quizObj.get("name").getAsString(), temp));
        }
    }

    private void addGradeItemsToList( JsonArray gradeItems,  ArrayList<Pair<String,String>> data , JsonArray studentSubmission) {
        // points, max_grade, total_score
        for (JsonElement pa : gradeItems) { // gest needed data from assig, quizz, grade item
            JsonObject gradeItemObj = pa.getAsJsonObject();

            String temp = "";
            temp = getStudentGrade(gradeItemObj.get("id").getAsInt(), studentSubmission , "grade_item_id", "grade");
            if (gradeItemObj.get("max_grade").getAsInt() == gradeItemObj.get("max_grade").getAsDouble()) {
                temp += "/" + gradeItemObj.get("max_grade").getAsInt();
            }else {
                temp += "/" + gradeItemObj.get("max_grade").getAsString();
            }

            totalCategory += gradeItemObj.get("max_grade").getAsDouble();
            data.add(new Pair<String, String>(gradeItemObj.get("name").getAsString(), temp));
        }
    }

    private String getStudentGrade(Integer itemId, JsonArray studentSubmission, String key, String requiredKey){
        for(JsonElement pa: studentSubmission){
            JsonObject studentSubmissionObj = pa.getAsJsonObject();
            if(studentSubmissionObj.get(key).getAsInt() == itemId){
                totalStudent +=  studentSubmissionObj.get(requiredKey).getAsDouble();
                if(studentSubmissionObj.get(requiredKey).getAsInt() == studentSubmissionObj.get(requiredKey).getAsDouble()){
                    return studentSubmissionObj.get(requiredKey).getAsInt() + "";
                }else
                    return studentSubmissionObj.get(requiredKey).getAsString();
            }
        }
        return "-";
    }


}
