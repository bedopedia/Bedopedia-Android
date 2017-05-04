package gradeBook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import Tools.SharedPreferenceUtils;
import gradeBook.Adapters.CourseAdapter;
import login.Services.ApiClient;
import login.Services.ApiInterface;
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
    String studentIdKey = "student_id";
    String assignmentsKey = "assignments";
    String studentKey = "student";
    String submittedAssignmentsKey = "submitted_assignments";
    String assignmentsAveragesKey = "assignments_averages";
    String quizzesKey = "quizzes";
    String submittedQuizzesKey = "submitted_quizzes";
    String quizzesAveragesKey = "quizzes_averages";
    String gradeItemsKey = "grade_items";
    String submittedGradesKey = "submitted_grades";
    String gradesAveragesKey = "grades_averages";
    String courseGroupIdKey = "course_group_id";
    String courseIdKey = "course_id";
    String courseNameKey = "course_name";
    String pointsKey = "points";
    String idKey = "id";
    String dashKey = "-";
    String maxGradeKey = "max_grade";
    String totalScoreKey = "total_score";

    public void loading(){
        progress.setTitle(R.string.LoadDialogueTitle);
        progress.setMessage(getString(R.string.LoadDialogueBody));
    }



    private class GradeBookAsyncTask extends AsyncTask {


        ExpandableListView courseListView ;
        CourseAdapter courseItemAdapter ;
        int responseSize = 0 ;


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
            params.put(studentIdKey,studentId);
            String url = "api/courses/"+ courseId +"/course_groups/"+courseGroupId+"/student_grade";
            SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(ActivityCourse.this, "cur_user" );
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            Call<JsonObject> call = apiService. getServise(url, params);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,getString(R.string.Dialogue401Title),getString(R.string.Dialogue401Body));
                    } else if (statusCode == 200) {

                        ArrayList<ArrayList<String>> header = new ArrayList<>();
                        ArrayList<ArrayList<ArrayList<String>>> courseItemsTempData = new ArrayList<ArrayList<ArrayList<String>>>();

                        JsonObject body = response.body();
                        JsonObject categories = (JsonObject) body.get("categories");
                        responseSize = categories.size() ;


                        Set<Map.Entry<String, JsonElement>> entries = categories.entrySet();//will return members of your object
                        for (Map.Entry<String, JsonElement> entry: entries) { // loop through all categories
                            ArrayList<String> temp = new ArrayList<String>();
                            temp.add(entry.getKey().toString());


                            ArrayList<ArrayList<String>> assignmentTempData = new ArrayList<>();
                            JsonObject item = entry.getValue().getAsJsonObject();
                            if (item.has(assignmentsKey) && body.has(studentKey) && body.get(studentKey).getAsJsonArray().get(0).getAsJsonObject().has(submittedAssignmentsKey)  ) {
                                addAssignmentsToList(item.get(assignmentsKey).getAsJsonArray(), assignmentTempData, body.get(studentKey).getAsJsonArray().get(0).getAsJsonObject().get(submittedAssignmentsKey).getAsJsonArray(), body.get(assignmentsAveragesKey).getAsJsonObject() );
                            }

                            if (item.has(quizzesKey)  &&  body.has(studentKey)  && body.get(studentKey).getAsJsonArray().get(0).getAsJsonObject().has(submittedQuizzesKey) ) {
                                addQuizzesToList(item.get(quizzesKey).getAsJsonArray(), assignmentTempData,body.get(studentKey).getAsJsonArray().get(0).getAsJsonObject().get(submittedQuizzesKey).getAsJsonArray(), body.get(quizzesAveragesKey).getAsJsonObject() );
                            }

                            if (item.has(gradeItemsKey)  && body.has(studentKey)  && body.get(studentKey).getAsJsonArray().get(0).getAsJsonObject().has(submittedGradesKey)  ) {
                                addGradeItemsToList(item.get(gradeItemsKey).getAsJsonArray(), assignmentTempData, body.get(studentKey).getAsJsonArray().get(0).getAsJsonObject().get(submittedGradesKey).getAsJsonArray(), body.get(gradesAveragesKey).getAsJsonObject() );
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

                        courseItemAdapter = new CourseAdapter(ActivityCourse.this, R.layout.activity_course, courseItemsTempData ,  header );
                        courseListView = (ExpandableListView) findViewById(R.id.category_list_view);
                        courseListView.setAdapter(courseItemAdapter);
                        ExpandAll();

                        courseListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                                return true;
                            }
                        });
                        courseListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                            @Override
                            public void onGroupExpand(int groupPosition) {
                                return;
                            }
                        });

                        courseListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                            @Override
                            public void onGroupCollapse(int groupPosition) {
                                return;
                            }
                        });


                        courseListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v,
                                                        int groupPosition, int childPosition, long id) {
                                return true;
                            }
                        });








                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progress.dismiss();
                    Dialogue.AlertDialog(context,getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
                }
            });

            return null;
        }
        void ExpandAll(){
            for(int i = 0 ; i < responseSize; i++)
                courseListView.expandGroup(i);
        }



    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        progress = new ProgressDialog(this);
        context = this;
        Bundle extras= getIntent().getExtras();
        studentId = extras.getString(studentIdKey);
        courseGroupId = extras.getString(courseGroupIdKey);
        courseId = extras.getString(courseIdKey);
        courseName = extras.getString(courseNameKey);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText(courseName);
        ImageButton back = (ImageButton) findViewById(R.id.actionbar_back_button);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        if (InternetConnection.isInternetAvailable(this)){
            new GradeBookAsyncTask().execute();
        } else {
            Dialogue.AlertDialog(this,getString(R.string.ConnectionErrorTitle),getString(R.string.ConnectionErrorBody));
        }

    }

    private void addAssignmentsToList( JsonArray assignments ,  ArrayList<ArrayList<String>> data , JsonArray studentSubmission, JsonObject assignmnetsAverages) {
        // points, max_grade, total_score
        for (JsonElement pa : assignments) { // gest needed data from assig, quizz, grade item
            JsonObject assignmentObj = pa.getAsJsonObject();

            String temp = "", comment = "";
            temp = getStudentGrade(assignmentObj.get(idKey).getAsInt(), studentSubmission , "assignment_id", "grade");
            if (temp.equals(dashKey)) {
                comment = "Not Graded";
            } else if (Integer.valueOf(temp)  >= (assignmentObj.get(pointsKey).getAsInt() * 80 )/ 100 ) {
                comment = "Wooha!";
            } else if (Integer.valueOf(temp) < assignmentObj.get(pointsKey).getAsInt() / 2 ) {
                comment ="Needs Improvement";

            } else if (Integer.valueOf(temp) < (assignmentObj.get(pointsKey).getAsInt() * 80 ) /100 &&  Integer.valueOf(temp) >= (assignmentObj.get("points").getAsInt() / 2)  ) {
                comment ="Good";
            }

            if (assignmentObj.get(pointsKey).getAsInt() == assignmentObj.get(pointsKey).getAsDouble()) {
                temp += "/" + assignmentObj.get(pointsKey).getAsInt();
            } else {
                temp += "/" + assignmentObj.get(pointsKey).getAsString();
            }
            totalCategory += assignmentObj.get(pointsKey).getAsDouble();
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
            temp = getStudentGrade(quizObj.get(idKey).getAsInt(), studentSubmission , "quiz_id", "score");
            if (temp.equals(dashKey)) {
                comment = "Not Graded";
            } else if (Integer.valueOf(temp) == (quizObj.get(totalScoreKey).getAsInt() * 80 )/ 100 ) {
                comment = "Wooha!";
            } else if (Integer.valueOf(temp) < quizObj.get(totalScoreKey).getAsInt() / 2 ) {
                comment ="Needs Improvement";

            } else if (Integer.valueOf(temp) < (quizObj.get(totalScoreKey).getAsInt() * 80 ) /100 &&  Integer.valueOf(temp) >= (quizObj.get(totalScoreKey).getAsInt() / 2)  ) {
                comment ="Good";
            }

            if (quizObj.get("total_score").getAsInt() == quizObj.get("total_score").getAsDouble()) {
                temp += "/" + quizObj.get(totalScoreKey).getAsInt();
            } else {
                temp += "/" + quizObj.get(totalScoreKey).getAsString();
            }

            totalCategory += quizObj.get(totalScoreKey).getAsDouble();


            ArrayList<String> tempDataForItem = new ArrayList<>();
            tempDataForItem.add(quizObj.get("name").getAsString());
            tempDataForItem.add(temp);
            tempDataForItem.add(comment);
            if (quizzesAverages.has(quizObj.get(idKey).getAsString())) {
                tempDataForItem.add(quizzesAverages.get(quizObj.get(idKey).getAsString()).getAsString());
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
            temp = getStudentGrade(gradeItemObj.get(idKey).getAsInt(), studentSubmission , "grade_item_id", "grade");
            if (temp.equals(dashKey)) {
                comment = "Not Graded";
            } else if (Integer.valueOf(temp) == (gradeItemObj.get(maxGradeKey).getAsInt() * 80 )/ 100) {
                comment = "Wooha!";
            } else if (Integer.valueOf(temp) <= gradeItemObj.get(maxGradeKey).getAsInt() / 2 ) {
                comment = "Needs Improvement";

            } else if (Integer.valueOf(temp) < (gradeItemObj.get(maxGradeKey).getAsInt() * 80 ) /100 &&  Integer.valueOf(temp) >= (gradeItemObj.get(maxGradeKey).getAsInt() / 2)  ) {
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
            if (gradeAverages.has(gradeItemObj.get(idKey).getAsString())) {
                tempDataForItem.add(gradeAverages.get(gradeItemObj.get(idKey).getAsString()).getAsString());
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
