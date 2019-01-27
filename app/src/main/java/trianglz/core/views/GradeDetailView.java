package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import trianglz.managers.api.ArrayResponseListener;
import trianglz.models.Assignment;
import trianglz.core.presenters.GradeDetailPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.CourseGradingPeriods;
import trianglz.models.GradeItem;
import trianglz.models.Quiz;
import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public class GradeDetailView {

    private Context context;
    private GradeDetailPresenter presenter;

    public GradeDetailView(Context context, GradeDetailPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }


    public void getAverageGrade(String url, String id) {
        UserManager.getAverageGrades(url, id, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                parseAverageStudentsMarks(response);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetAverageGradeFailure(message,errorCode);
            }
        });
    }

    public void getStudentGradeBook(String url, final int studentId) {
        UserManager.getStudentGradeBook(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                parseGetStudentBook(response,studentId);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetStudentGradeBookFailure(message,errorCode);
            }
        });

    }

    public void getSemesters(String url, String courseId){
        UserManager.getSemesters(url, courseId ,new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                presenter.onGetSemestersSuccess(parseSemstersResponse(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetSemesterFailure(message,errorCode);
            }
        });
    }


    private void parseGetStudentBook(JSONObject response, int id) {
        JSONArray students = response.optJSONArray(Constants.KEY_STUDENTS);
        for (int i = 0; i < students.length(); i++) {
            JSONObject student = students.optJSONObject(i);
            int studentID = student.optInt(Constants.KEY_ID);
            if (id == studentID) {
                JSONObject assignmentsJsonObject = student.optJSONObject(Constants.KEY_ASSIGNMENTS);
                ArrayList<Assignment> assignmentArrayList = parseAssignments(assignmentsJsonObject);
                JSONObject quizzesJsonObject = student.optJSONObject(Constants.KEY_QUIZZES);
                ArrayList<Quiz> quizArrayList = parseQuizzes(quizzesJsonObject);
                JSONObject gradeItemsJsonObject = student.optJSONObject(Constants.KEY_GRADE_ITEMS);
                ArrayList<GradeItem> gradeItemArrayList = parseGradeItems(gradeItemsJsonObject);
                presenter.onGetStudentGradeBookSuccess(assignmentArrayList,quizArrayList,gradeItemArrayList);
            }
        }
    }


    private void parseAverageStudentsMarks(JSONObject jsonObject){
        JSONObject assignmentsAverages = jsonObject.optJSONObject("assignments_averages");
        JSONObject quizzesAverage = jsonObject.optJSONObject("quizzes_averages");
        JSONObject grades_averages = jsonObject.optJSONObject("grades_averages");
        presenter.onGetAverageGradesSuccess(parseAverage(quizzesAverage)
                ,parseAverage(assignmentsAverages),parseAverage(grades_averages));
    }

    private HashMap<String,Double> parseAverage(JSONObject jsonObject) {
        HashMap<String,Double> averageHashMap = new HashMap<>();
        Iterator<?> keys = jsonObject.keys();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            Double value = jsonObject.optDouble(key);
            averageHashMap.put(key,value);
        }

      return averageHashMap;
    }


    private ArrayList<Assignment> parseAssignments(JSONObject json) {
        ArrayList<Assignment> assignmentArrayList = new ArrayList<>();
        Iterator<String> iter = json.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            JSONObject jsonObject = json.optJSONObject(key);
            int id = jsonObject.optInt(Constants.KEY_ID);
            String name = jsonObject.optString(Constants.KEY_NAME);
            int total = jsonObject.optInt(Constants.KEY_TOTAL);
            double grade = jsonObject.optDouble(Constants.KEY_GRADE);
            String gradeView = jsonObject.optString(Constants.KEY_GRADE_VIEW);
            String feedBack = jsonObject.optString(Constants.KEY_FEED_BACK);
            String endDate = jsonObject.optString(Constants.KEY_END_DATE);
            assignmentArrayList.add(new Assignment(id,name,total,grade,gradeView,feedBack,endDate));
        }
        return assignmentArrayList;
    }

    private ArrayList<Quiz> parseQuizzes(JSONObject json) {
        ArrayList<Quiz> quizArrayList = new ArrayList<>();
        Iterator<String> iter = json.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            JSONObject jsonObject = json.optJSONObject(key);
            int id = jsonObject.optInt(Constants.KEY_ID);
            String name = jsonObject.optString(Constants.KEY_NAME);
            double total = jsonObject.optDouble(Constants.KEY_TOTAL);
            double totalScore = jsonObject.optDouble(Constants.KEY_TOTAL_SCORE);
            double grade = jsonObject.optDouble(Constants.KEY_GRADE);
            String gradeView = jsonObject.optString(Constants.KEY_GRADE_VIEW);
            String feedBack = jsonObject.optString(Constants.KEY_FEED_BACK);
            String endDate = jsonObject.optString(Constants.KEY_END_DATE);
            quizArrayList.add(new Quiz(id,name,totalScore,total,grade,gradeView,feedBack,endDate));
        }
        return quizArrayList;
    }

    private ArrayList<GradeItem> parseGradeItems(JSONObject json) {
        ArrayList<GradeItem> gradeItemArrayList = new ArrayList<>();
        Iterator<String> iter = json.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            JSONObject jsonObject = json.optJSONObject(key);
            int id = jsonObject.optInt(Constants.KEY_ID);
            String name = jsonObject.optString(Constants.KEY_NAME);
            int maxGrade = jsonObject.optInt(Constants.KEY_MAX_GRADE);
            double total = jsonObject.optDouble(Constants.KEY_TOTAL);
            double grade = jsonObject.optDouble(Constants.KEY_GRADE);
            String gradeView = jsonObject.optString(Constants.KEY_GRADE_VIEW);
            String feedBack = jsonObject.optString(Constants.KEY_FEED_BACK);
            String endDate = jsonObject.optString(Constants.KEY_END_DATE);
            int gradingPeriodId = jsonObject.optInt(Constants.KEY_GRADING_PERIOD_ID);
            gradeItemArrayList.add(new GradeItem(id,name,maxGrade,total,grade,gradeView,feedBack,endDate,gradingPeriodId));
        }
        return gradeItemArrayList;
    }

    private ArrayList<CourseGradingPeriods>  parseSemstersResponse(JSONArray response){
        ArrayList<CourseGradingPeriods> gradingPeriodsArrayList = new ArrayList<>();
        CourseGradingPeriods subCourseGradingPeriods = null;
        for(int i = 0 ; i<response.length(); i++){
            JSONObject jsonObject = response.optJSONObject(i);
            String endDate = jsonObject.optString(Constants.KEY_END_DATE);
            int id = jsonObject.optInt(Constants.KEY_ID);
            boolean lock = jsonObject.optBoolean(Constants.KEY_LOCK);
            String name = jsonObject.optString(Constants.KEY_NAME);
            boolean publish = jsonObject.optBoolean(Constants.KEY_PUBLISH);
            String startDate = jsonObject.optString(Constants.KEY_START_DATE);
            int weight = jsonObject.optInt(Constants.KEY_WEIGHT);
            JSONArray subGradingArray = jsonObject.optJSONArray(Constants.KEY_SUB_GRADING_ATTRIBUTES);
            ArrayList<CourseGradingPeriods> subGradingPeriodsArrayList = new ArrayList<>();
            for(int j = 0; j<subGradingArray.length(); j++){
                JSONObject subGradeObject = subGradingArray.optJSONObject(j);
                String subEndDate = subGradeObject.optString(Constants.KEY_END_DATE);
                int subID = subGradeObject.optInt(Constants.KEY_ID);
                boolean subLock = subGradeObject.optBoolean(Constants.KEY_LOCK);
                String subName = subGradeObject.optString(Constants.KEY_NAME);
                boolean subPublish = subGradeObject.optBoolean(Constants.KEY_PUBLISH);
                String subStartDate = subGradeObject.optString(Constants.KEY_START_DATE);
                int subWeight = subGradeObject.optInt(Constants.KEY_WEIGHT);
                subCourseGradingPeriods = new CourseGradingPeriods(subEndDate,
                        subID,subLock,subName,subPublish,subStartDate,null,subWeight,true,false);
                subCourseGradingPeriods.parentStartDate = startDate;
                subCourseGradingPeriods.parentEndDate = endDate;
                subGradingPeriodsArrayList.add(subCourseGradingPeriods);
            }
            if(subGradingPeriodsArrayList.size()>0){
                gradingPeriodsArrayList.add(new CourseGradingPeriods(endDate+"",id,lock,
                        name,publish,startDate,subGradingPeriodsArrayList,
                        weight,false,true));
            }else {
                gradingPeriodsArrayList.add(new CourseGradingPeriods(endDate+"",id,lock,
                        name,publish,startDate,subGradingPeriodsArrayList,
                        weight,false,false));
            }

        }
        return gradingPeriodsArrayList;
    }


}
