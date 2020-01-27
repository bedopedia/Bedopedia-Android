package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import trianglz.core.presenters.GradeDetailPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Assignment;
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
                presenter.onGetAverageGradeFailure(message, errorCode);
            }
        });
    }

    public void getStudentGradeBook(String url, final int studentId) {
        UserManager.getStudentGradeBook(url, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try{
                    response = new JSONObject("{\n" +
                            "  \"id\": 1,\n" +
                            "  \"name\": \"Term 1\",\n" +
                            "  \"start_date\": \"2019-09-10T00:00:00.000Z\",\n" +
                            "  \"end_date\": \"2020-01-15T23:59:59.000Z\",\n" +
                            "  \"created_at\": \"2019-11-11T10:51:43.000Z\",\n" +
                            "  \"updated_at\": \"2019-11-11T10:51:43.000Z\",\n" +
                            "  \"level_id\": null,\n" +
                            "  \"academic_term_id\": 2,\n" +
                            "  \"deleted_at\": null,\n" +
                            "  \"weight\": 50,\n" +
                            "  \"parent_id\": null,\n" +
                            "  \"lock\": false,\n" +
                            "  \"publish\": false,\n" +
                            "  \"category_is_numeric\": false,\n" +
                            "  \"courses_grading_period_id\": 99,\n" +
                            "  \"categories\": [\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"weight\": 100,\n" +
                            "      \"name\": \"Ungraded\",\n" +
                            "      \"parent_id\": null,\n" +
                            "      \"total\": 72,\n" +
                            "      \"grade\": \"****\",\n" +
                            "      \"quizzes_total\": 6,\n" +
                            "      \"quizzes_grade\": 0,\n" +
                            "      \"quizzes\": [\n" +
                            "        {\n" +
                            "          \"id\": 5,\n" +
                            "          \"name\": \"quiz 1\",\n" +
                            "          \"status\": null,\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"quiz\",\n" +
                            "          \"total\": 1,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\",\n" +
                            "          \"grade_view\": \"N/A\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 6,\n" +
                            "          \"name\": \"QUIZ 2\",\n" +
                            "          \"status\": null,\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"quiz\",\n" +
                            "          \"total\": 5,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\",\n" +
                            "          \"grade_view\": \"N/A\"\n" +
                            "        }\n" +
                            "      ],\n" +
                            "      \"assignments_total\": 46,\n" +
                            "      \"assignments_grade\": 0,\n" +
                            "      \"assignments\": [\n" +
                            "        {\n" +
                            "          \"id\": 51,\n" +
                            "          \"name\": \"The polygons - Measure the length\",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"assignment\",\n" +
                            "          \"total\": 4,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"status\": null,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\",\n" +
                            "          \"grade_view\": \"N/A\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 60,\n" +
                            "          \"name\": \"The polygons \",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"assignment\",\n" +
                            "          \"total\": 4,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"status\": null,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\",\n" +
                            "          \"grade_view\": \"N/A\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 70,\n" +
                            "          \"name\": \"Measure the length\",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"assignment\",\n" +
                            "          \"total\": 4,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"status\": null,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\",\n" +
                            "          \"grade_view\": \"N/A\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 79,\n" +
                            "          \"name\": \"The solids\",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"assignment\",\n" +
                            "          \"total\": 4,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"status\": null,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\",\n" +
                            "          \"grade_view\": \"N/A\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 93,\n" +
                            "          \"name\": \"Solids\",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"assignment\",\n" +
                            "          \"total\": 5,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"status\": null,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\",\n" +
                            "          \"grade_view\": \"N/A\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 99,\n" +
                            "          \"name\": \"Solids\",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"assignment\",\n" +
                            "          \"total\": 5,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"status\": null,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\",\n" +
                            "          \"grade_view\": \"N/A\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 104,\n" +
                            "          \"name\": \"Measuring the mass\",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"assignment\",\n" +
                            "          \"total\": 5,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"status\": null,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"grade_view\": \"N/A\",\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 123,\n" +
                            "          \"name\": \"o'clock\",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"assignment\",\n" +
                            "          \"total\": 5,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"status\": null,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"grade_view\": \"N/A\",\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 126,\n" +
                            "          \"name\": \"Clock\",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"assignment\",\n" +
                            "          \"total\": 5,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"status\": null,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"grade_view\": \"N/A\",\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"id\": 134,\n" +
                            "          \"name\": \"Final Revision Sheet\",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"type\": \"assignment\",\n" +
                            "          \"total\": 5,\n" +
                            "          \"grade\": 0,\n" +
                            "          \"status\": null,\n" +
                            "          \"hide_grade\": 0,\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\",\n" +
                            "          \"grade_view\": \"N/A\"\n" +
                            "        }\n" +
                            "      ],\n" +
                            "      \"grade_items_total\": 20,\n" +
                            "      \"grade_items_grade\": 19,\n" +
                            "      \"grade_items\": [\n" +
                            "        {\n" +
                            "          \"id\": 10,\n" +
                            "          \"name\": \"November Quiz\",\n" +
                            "          \"category_id\": 2,\n" +
                            "          \"hide_grade\": false,\n" +
                            "          \"type\": \"grade_item\",\n" +
                            "          \"grading_period_id\": 1,\n" +
                            "          \"total\": 20,\n" +
                            "          \"grade\": 19,\n" +
                            "          \"status\": 0,\n" +
                            "          \"end_date\": \"2020-01-14T23:59:59.000Z\",\n" +
                            "          \"feedback_content\": null,\n" +
                            "          \"feedback_id\": null,\n" +
                            "          \"grade_view\": 19\n" +
                            "        }\n" +
                            "      ],\n" +
                            "      \"percentage\": 26.39,\n" +
                            "      \"grade_view\": \"****\",\n" +
                            "      \"sub_categories\": []\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"total\": 100,\n" +
                            "  \"grade\": \"****\",\n" +
                            "  \"percentage\": 13.2,\n" +
                            "  \"grade_view\": \"****\",\n" +
                            "  \"letter_scale\": \"****\",\n" +
                            "  \"gpa_scale\": \"****\"\n" +
                            "}");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                parseGetStudentBook(response, studentId);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetStudentGradeBookFailure(message, errorCode);
            }
        });

    }

    public void getSemesters(String url, String courseId) {
        UserManager.getSemesters(url, courseId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                presenter.onGetSemestersSuccess(parseSemstersResponse(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                presenter.onGetSemesterFailure(message, errorCode);
            }
        });
    }


    private void parseGetStudentBook(JSONObject response, int id) {
        JSONArray categories = response.optJSONArray(Constants.KEY_CATEGORIES);
        JSONObject category = categories.optJSONObject(0);
        JSONArray assignmentsJsonArray = category.optJSONArray(Constants.KEY_ASSIGNMENTS);
        ArrayList<Assignment> assignmentArrayList = parseAssignments(assignmentsJsonArray);
        JSONArray quizzesJsonArray = category.optJSONArray(Constants.KEY_QUIZZES);
        ArrayList<Quiz> quizArrayList = parseQuizzes(quizzesJsonArray);
        JSONArray gradeItemsJsonArray = category.optJSONArray(Constants.KEY_GRADE_ITEMS);
        ArrayList<GradeItem> gradeItemArrayList = parseGradeItems(gradeItemsJsonArray);
        presenter.onGetStudentGradeBookSuccess(assignmentArrayList, quizArrayList, gradeItemArrayList);

    }


    private void parseAverageStudentsMarks(JSONObject jsonObject) {
        JSONObject assignmentsAverages = jsonObject.optJSONObject("assignments_averages");
        JSONObject quizzesAverage = jsonObject.optJSONObject("quizzes_averages");
        JSONObject grades_averages = jsonObject.optJSONObject("grades_averages");
        presenter.onGetAverageGradesSuccess(parseAverage(quizzesAverage)
                , parseAverage(assignmentsAverages), parseAverage(grades_averages));
    }

    private HashMap<String, Double> parseAverage(JSONObject jsonObject) {
        HashMap<String, Double> averageHashMap = new HashMap<>();
        Iterator<?> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Double value = jsonObject.optDouble(key);
            averageHashMap.put(key, value);
        }

        return averageHashMap;
    }


    private ArrayList<Assignment> parseAssignments(JSONArray jsonArray) {
        ArrayList<Assignment> assignmentArrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int id = jsonObject.optInt(Constants.KEY_ID);
            String name = jsonObject.optString(Constants.KEY_NAME);
            int total = jsonObject.optInt(Constants.KEY_TOTAL);
            double grade = jsonObject.optDouble(Constants.KEY_GRADE);
            String gradeView = jsonObject.optString(Constants.KEY_GRADE_VIEW);
            String feedBack = jsonObject.optString(Constants.KEY_FEED_BACK);
            String endDate = jsonObject.optString(Constants.KEY_END_DATE);
            int hideGrade = jsonObject.optInt(Constants.KEY_HIDE_GRADE);
            assignmentArrayList.add(new Assignment(id, name, total, grade, gradeView, feedBack, endDate, hideGrade));
//            }
//        }
        }
        return assignmentArrayList;
    }

    private ArrayList<Quiz> parseQuizzes(JSONArray jsonArray) {
        ArrayList<Quiz> quizArrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int id = jsonObject.optInt(Constants.KEY_ID);
            String name = jsonObject.optString(Constants.KEY_NAME);
            double total = jsonObject.optDouble(Constants.KEY_TOTAL);
            double totalScore = jsonObject.optDouble(Constants.KEY_TOTAL_SCORE);
            double grade = jsonObject.optDouble(Constants.KEY_GRADE);
            String gradeView = jsonObject.optString(Constants.KEY_GRADE_VIEW);
            String feedBack = jsonObject.optString(Constants.KEY_FEED_BACK);
            String endDate = jsonObject.optString(Constants.KEY_END_DATE);
            int hideGrade = jsonObject.optInt(Constants.KEY_HIDE_GRADE);
            quizArrayList.add(new Quiz(id, name, totalScore, total, grade, gradeView, feedBack, endDate, hideGrade));
        }
        return quizArrayList;
    }

    private ArrayList<GradeItem> parseGradeItems(JSONArray jsonArray) {
        ArrayList<GradeItem> gradeItemArrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int id = jsonObject.optInt(Constants.KEY_ID);
            String name = jsonObject.optString(Constants.KEY_NAME);
            int maxGrade = jsonObject.optInt(Constants.KEY_MAX_GRADE);
            double total = jsonObject.optDouble(Constants.KEY_TOTAL);
            double grade = jsonObject.optDouble(Constants.KEY_GRADE);
            String gradeView = jsonObject.optString(Constants.KEY_GRADE_VIEW);
            String feedBack = jsonObject.optString(Constants.KEY_FEED_BACK);
            String endDate = jsonObject.optString(Constants.KEY_END_DATE);
            int gradingPeriodId = jsonObject.optInt(Constants.KEY_GRADING_PERIOD_ID);
            boolean hideGrade = jsonObject.optBoolean(Constants.KEY_HIDE_GRADE);
            gradeItemArrayList.add(new GradeItem(id, name, maxGrade, total, grade, gradeView, feedBack, endDate, gradingPeriodId, hideGrade));
        }
        return gradeItemArrayList;
    }

    private ArrayList<CourseGradingPeriods> parseSemstersResponse(JSONArray response) {
        ArrayList<CourseGradingPeriods> gradingPeriodsArrayList = new ArrayList<>();
        CourseGradingPeriods subCourseGradingPeriods = null;
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = response.optJSONObject(i);
            String endDate = jsonObject.optString(Constants.KEY_END_DATE);
            int id = jsonObject.optInt(Constants.KEY_ID);
            boolean lock = jsonObject.optBoolean(Constants.KEY_LOCK);
            String name = jsonObject.optString(Constants.KEY_NAME);
            boolean publish = jsonObject.optBoolean(Constants.KEY_PUBLISH);
            String startDate = jsonObject.optString(Constants.KEY_START_DATE);
            JSONArray subGradingArray = jsonObject.optJSONArray(Constants.KEY_SUB_GRADING_ATTRIBUTES);
            ArrayList<CourseGradingPeriods> subGradingPeriodsArrayList = new ArrayList<>();
            for (int j = 0; j < subGradingArray.length(); j++) {
                JSONObject subGradeObject = subGradingArray.optJSONObject(j);
                String subEndDate = subGradeObject.optString(Constants.KEY_END_DATE);
                int subID = subGradeObject.optInt(Constants.KEY_ID);
                boolean subLock = subGradeObject.optBoolean(Constants.KEY_LOCK);
                String subName = subGradeObject.optString(Constants.KEY_NAME);
                boolean subPublish = subGradeObject.optBoolean(Constants.KEY_PUBLISH);
                String subStartDate = subGradeObject.optString(Constants.KEY_START_DATE);
                subCourseGradingPeriods = new CourseGradingPeriods(subEndDate,
                        subID, subLock, subName, subPublish, subStartDate, null, true, false);
                subCourseGradingPeriods.parentStartDate = startDate;
                subCourseGradingPeriods.parentEndDate = endDate;
                subGradingPeriodsArrayList.add(subCourseGradingPeriods);
            }
            if (subGradingPeriodsArrayList.size() > 0) {
                gradingPeriodsArrayList.add(new CourseGradingPeriods(endDate + "", id, lock,
                        name, publish, startDate, subGradingPeriodsArrayList,
                        false, true));
            } else {
                gradingPeriodsArrayList.add(new CourseGradingPeriods(endDate + "", id, lock,
                        name, publish, startDate, subGradingPeriodsArrayList,
                        false, false));
            }

        }
        return gradingPeriodsArrayList;
    }


}
