package gradeBook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gradeBook.Adapters.CourseAdapter;
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
    String studentId, courseGroupId,courseId;
    String courseName;

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
            params.put("student_id",studentId);
            String url = "api/courses/"+ courseId +"/course_groups/"+courseGroupId+"/student_grade";
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
                        ArrayList<ArrayList<ArrayList<String>>> courseItemsTempData = new ArrayList<ArrayList<ArrayList<String>>>();

                        JsonObject body = response.body();
                        JsonObject categories = (JsonObject) body.get("categories");

                        Set<Map.Entry<String, JsonElement>> entries = categories.entrySet();//will return members of your object
                        for (Map.Entry<String, JsonElement> entry: entries) { // loop through all categories
                            ArrayList<String> temp = new ArrayList<String>();
                            temp.add(entry.getKey().toString());


                            ArrayList<ArrayList<String>> assignmentTempData = new ArrayList<>();
                            JsonObject item = entry.getValue().getAsJsonObject();
                            if (item.has("assignments") && body.has("student") && body.get("student").getAsJsonArray().get(0).getAsJsonObject().has("submitted_assignments")  ) {
                                addAssignmentsToList(item.get("assignments").getAsJsonArray(), assignmentTempData, body.get("student").getAsJsonArray().get(0).getAsJsonObject().get("submitted_assignments").getAsJsonArray(), body.get("assignments_averages").getAsJsonObject() );
                            }

                            if (item.has("quizzes")  &&  body.has("student")  && body.get("student").getAsJsonArray().get(0).getAsJsonObject().has("submitted_quizzes") ) {
                                addQuizzesToList(item.get("quizzes").getAsJsonArray(), assignmentTempData,body.get("student").getAsJsonArray().get(0).getAsJsonObject().get("submitted_quizzes").getAsJsonArray(), body.get("quizzes_averages").getAsJsonObject() );
                            }

                            if (item.has("grade_items")  && body.has("student")  && body.get("student").getAsJsonArray().get(0).getAsJsonObject().has("submitted_grades")  ) {
                                addGradeItemsToList(item.get("grade_items").getAsJsonArray(), assignmentTempData, body.get("student").getAsJsonArray().get(0).getAsJsonObject().get("submitted_grades").getAsJsonArray(), body.get("grades_averages").getAsJsonObject() );
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
        Bundle extras= getIntent().getExtras();
        studentId = extras.getString("student_id");
        courseGroupId = extras.getString("course_group_id");
        courseId = extras.getString("course_id");
        courseName = extras.getString("course_name");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText(courseName);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        if (InternetConnection.isInternetAvailable(this)){
            new GradeBookAsyncTask().execute();
        } else {
            Dialogue.AlertDialog(this,"No NetworkConnection","Check your Netwotk connection and Try again");
        }

    }

    private void addAssignmentsToList( JsonArray assignments ,  ArrayList<ArrayList<String>> data , JsonArray studentSubmission, JsonObject assignmnetsAverages) {
        // points, max_grade, total_score
        for (JsonElement pa : assignments) { // gest needed data from assig, quizz, grade item
            JsonObject assignmentObj = pa.getAsJsonObject();

            String temp = "", comment = "";
            temp = getStudentGrade(assignmentObj.get("id").getAsInt(), studentSubmission , "assignment_id", "grade");
            if (temp.equals("-")) {
                comment = "Not Graded";
            } else if (Integer.valueOf(temp)  >= (assignmentObj.get("points").getAsInt() * 80 )/ 100 ) {
                comment = "Wooha!";
            } else if (Integer.valueOf(temp) < assignmentObj.get("points").getAsInt() / 2 ) {
                comment ="Needs Improvement";

            } else if (Integer.valueOf(temp) < (assignmentObj.get("points").getAsInt() * 80 ) /100 &&  Integer.valueOf(temp) >= (assignmentObj.get("points").getAsInt() / 2)  ) {
                comment ="Good";
            }

            if (assignmentObj.get("points").getAsInt() == assignmentObj.get("points").getAsDouble()) {
                temp += "/" + assignmentObj.get("points").getAsInt();
            } else {
                temp += "/" + assignmentObj.get("points").getAsString();
            }
            totalCategory += assignmentObj.get("points").getAsDouble();
            ArrayList<String> tempDataForItem = new ArrayList<>();
            tempDataForItem.add(assignmentObj.get("name").getAsString());
            tempDataForItem.add(temp);
            tempDataForItem.add(comment);
            if (assignmnetsAverages.has(assignmentObj.get("id").getAsString())) {
                tempDataForItem.add(assignmnetsAverages.get(assignmentObj.get("id").getAsString()).getAsString());
            } else {
                tempDataForItem.add("0.0");
            }
            data.add(tempDataForItem);
        }
    }

    private void addQuizzesToList( JsonArray quizzes ,  ArrayList<ArrayList<String>> data , JsonArray studentSubmission, JsonObject quizzesAverages) {
        // points, max_grade, total_score
        for (JsonElement pa : quizzes) { // gest needed data from assig, quizz, grade item
            JsonObject quizObj = pa.getAsJsonObject();
            String temp = "", comment = "";
            temp = getStudentGrade(quizObj.get("id").getAsInt(), studentSubmission , "quiz_id", "score");
            if (temp.equals("-")) {
                comment = "Not Graded";
            } else if (Integer.valueOf(temp) == (quizObj.get("total_score").getAsInt() * 80 )/ 100 ) {
                comment = "Wooha!";
            } else if (Integer.valueOf(temp) < quizObj.get("total_score").getAsInt() / 2 ) {
                comment ="Needs Improvement";

            } else if (Integer.valueOf(temp) < (quizObj.get("total_score").getAsInt() * 80 ) /100 &&  Integer.valueOf(temp) >= (quizObj.get("total_score").getAsInt() / 2)  ) {
                comment ="Good";
            }

            if (quizObj.get("total_score").getAsInt() == quizObj.get("total_score").getAsDouble()) {
                temp += "/" + quizObj.get("total_score").getAsInt();
            } else {
                temp += "/" + quizObj.get("total_score").getAsString();
            }

            totalCategory += quizObj.get("total_score").getAsDouble();


            ArrayList<String> tempDataForItem = new ArrayList<>();
            tempDataForItem.add(quizObj.get("name").getAsString());
            tempDataForItem.add(temp);
            tempDataForItem.add(comment);
            if (quizzesAverages.has(quizObj.get("id").getAsString())) {
                tempDataForItem.add(quizzesAverages.get(quizObj.get("id").getAsString()).getAsString());
            } else {
                tempDataForItem.add("0.0");
            }
            data.add(tempDataForItem);
        }
    }

    private void addGradeItemsToList( JsonArray gradeItems,  ArrayList<ArrayList<String>> data , JsonArray studentSubmission, JsonObject gradeAverages) {
        // points, max_grade, total_score
        for (JsonElement pa : gradeItems) { // gest needed data from assig, quizz, grade item
            JsonObject gradeItemObj = pa.getAsJsonObject();

            String temp = "", comment = "";
            temp = getStudentGrade(gradeItemObj.get("id").getAsInt(), studentSubmission , "grade_item_id", "grade");
            if (temp.equals("-")) {
                comment = "Not Graded";
            } else if (Integer.valueOf(temp) == (gradeItemObj.get("max_grade").getAsInt() * 80 )/ 100) {
                comment = "Wooha!";
            } else if (Integer.valueOf(temp) <= gradeItemObj.get("max_grade").getAsInt() / 2 ) {
                comment = "Needs Improvement";

            } else if (Integer.valueOf(temp) < (gradeItemObj.get("max_grade").getAsInt() * 80 ) /100 &&  Integer.valueOf(temp) >= (gradeItemObj.get("max_grade").getAsInt() / 2)  ) {
                comment = "Good";
            }

            if (gradeItemObj.get("max_grade").getAsInt() == gradeItemObj.get("max_grade").getAsDouble()) {
                temp += "/" + gradeItemObj.get("max_grade").getAsInt();
            }else {
                temp += "/" + gradeItemObj.get("max_grade").getAsString();
            }

            totalCategory += gradeItemObj.get("max_grade").getAsDouble();
            ArrayList<String> tempDataForItem = new ArrayList<>();
            tempDataForItem.add(gradeItemObj.get("name").getAsString());
            tempDataForItem.add(temp);
            tempDataForItem.add(comment);
            if (gradeAverages.has(gradeItemObj.get("id").getAsString())) {
                tempDataForItem.add(gradeAverages.get(gradeItemObj.get("id").getAsString()).getAsString());
            } else {
                tempDataForItem.add("0.0");
            }
            data.add(tempDataForItem);
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
