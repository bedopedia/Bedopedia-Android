package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.NewMessagePresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Course;
import trianglz.models.Stage;
import trianglz.models.Subject;
import trianglz.models.Teacher;
import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class NewMessageView {
    private Context context;
    private NewMessagePresenter newMessagePresenter;

    public NewMessageView(Context context, NewMessagePresenter newMessagePresenter) {
        this.context = context;
        this.newMessagePresenter = newMessagePresenter;
    }

    public void getCourseGroups(String url) {
        UserManager.getCourseGroups(url, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                newMessagePresenter.onGetCourseGroupsSuccess(parseGetGroups(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                newMessagePresenter.onGetCourseGroupsFailure(message, errorCode);
            }
        });
    }

    private ArrayList<Subject> parseGetGroups(JSONArray jsonArray) {
        ArrayList<Subject> subjectArrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject courseGroupJsonObject = jsonArray.optJSONObject(i);
            Course course = parseCourse(courseGroupJsonObject.optJSONObject(Constants.KEY_COURSE));
            ArrayList<Teacher> teacherArrayList = parseTeacherArrayList(courseGroupJsonObject.
                    optJSONArray(Constants.KEY_TEACHERS));
            Stage stage = parseStage(courseGroupJsonObject.optJSONObject(Constants.KEY_STAGE));
            int completedLessonCount =  courseGroupJsonObject.optInt(Constants.KEY_COMPLETED_LESSONS_COUNT);
            int courseID = courseGroupJsonObject.optInt(Constants.KEY_COURSE_ID);
            String courseName = courseGroupJsonObject.optString(Constants.KEY_COURSE_NAME);
            String iconName = courseGroupJsonObject.optString(Constants.KEY_ICON_NAME);
            int id = courseGroupJsonObject.optInt(Constants.KEY_ID);
            String lastCompletedLessonName = courseGroupJsonObject.optString(Constants.KEY_LAST_COMPLETED_LESSON_NAME);
            int lessonsCount = courseGroupJsonObject.optInt(Constants.KEY_LESSONS_COUNT);
            String name = courseGroupJsonObject.optString(Constants.KEY_NAME);
            Subject subject = new Subject(completedLessonCount,course,courseID,
                    courseName,iconName,id,lastCompletedLessonName,lessonsCount,name,
                    stage,teacherArrayList);
            subjectArrayList.add(subject);
        }
        return subjectArrayList;
    }

    private Course parseCourse(JSONObject courseJsonObject){
        String checkListString = courseJsonObject.optString(Constants.KEY_CHECK_LIST_STRING);
        String code = courseJsonObject.optString(Constants.KEY_CODE);
        String createdAt = courseJsonObject.optString(Constants.KEY_CREATED_AT);
        String deletedAt = courseJsonObject.optString(Constants.KEY_DELETED_AT);
        String descriptionField = courseJsonObject.optString(Constants.KEY_DESCRIPTION);
        int hodId = courseJsonObject.optInt(Constants.KEY_HOD_ID);
        String iconName =courseJsonObject.optString(Constants.KEY_ICON_NAME);
        int id = courseJsonObject.optInt(Constants.KEY_ID);
        int levelId = courseJsonObject.optInt(Constants.KEY_LEVEL_ID);
        String name = courseJsonObject.optString(Constants.KEY_NAME);
        String ownerId = courseJsonObject.optString(Constants.KEY_OWNER_ID);
        int passLimit = courseJsonObject.optInt(Constants.KEY_PASS_LIMIT);
        String questionPoolId = courseJsonObject.optString(Constants.KEY_QUESTION_POOL_ID);
        String semesterId = courseJsonObject.optString(Constants.KEY_SEMESTER_ID);
        boolean showFinalGrade = courseJsonObject.optBoolean(Constants.KEY_SHOW_FINAL_GRADE);
        int totalGrade = courseJsonObject.optInt(Constants.KEY_TOTAL_GRADE);
        String updatedAt = courseJsonObject.optString(Constants.KEY_UPADTED_AT);
        Course course = new Course(checkListString,code,createdAt,deletedAt,
                descriptionField,hodId,iconName,id,levelId,name,ownerId,passLimit,
                questionPoolId,semesterId,showFinalGrade,totalGrade,updatedAt);
        return course;
    }

    private ArrayList<Teacher> parseTeacherArrayList(JSONArray teacherJsonArray){
        ArrayList<Teacher> teacherArrayList = new ArrayList<>();
        for(int i = 0 ; i <teacherJsonArray.length(); i++){
            JSONObject teacherJsonObject = teacherJsonArray.optJSONObject(i);
            int actableId = teacherJsonObject.optInt(Constants.KEY_CHILD_ID);
            String avatarUrl = teacherJsonObject.optString(Constants.KEY_AVATER_URL);
            String firstName = teacherJsonObject.optString(Constants.KEY_FIRST_NAME);
            String gender = teacherJsonObject.optString(Constants.KEY_GENDER);
            int id = teacherJsonObject.optInt(Constants.KEY_USER_ID);
            String lastName = teacherJsonObject.optString(Constants.KEY_FIRST_NAME);
            String name = teacherJsonObject.optString(Constants.KEY_NAME);
            String nameWithTitle = teacherJsonObject.optString(Constants.KEY_NAME_WITH_TITLE);
            String thumbUrl = teacherJsonObject.optString(Constants.KEY_THUMB_URL);
            String userType = teacherJsonObject.optString(Constants.KEY_USER_TYPE);
            Teacher teacher = new Teacher(actableId,avatarUrl,firstName,gender,id,lastName,name,
                    nameWithTitle,thumbUrl,userType);
            teacherArrayList.add(teacher);
        }


        return teacherArrayList;
    }

    private Stage parseStage(JSONObject stageJsonObject){
         String createdAt = stageJsonObject.optString(Constants.KEY_CREATED_AT);
        Object deletedAt = stageJsonObject.opt(Constants.KEY_DELETED_AT);
        int id = stageJsonObject.optInt(Constants.KEY_ID);
        boolean isDeleted = stageJsonObject.optBoolean(Constants.KEY_IS_DELETED);
        boolean isPreSchool = stageJsonObject.optBoolean(Constants.KEY_IS_PRESCHOOL);
        String name = stageJsonObject.optString(Constants.KEY_NAME);
        int sectionId = stageJsonObject.optInt(Constants.KEY_SECTION_ID);
        String updatedAt = stageJsonObject.optString(Constants.KEY_UPADTED_AT);
        Stage stage = new Stage(createdAt,deletedAt,id,isDeleted,isPreSchool,name,sectionId,updatedAt);
        return stage;
    }
}
