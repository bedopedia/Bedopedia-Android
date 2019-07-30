package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;

import trianglz.core.presenters.TeacherCoursesPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;

public class TeacherCoursesView {
    private Context context;
    private Gson gson;
    private TeacherCoursesPresenter teacherCoursesPresenter;


    public TeacherCoursesView(Context context, TeacherCoursesPresenter teacherCoursesPresenter) {
        this.context = context;
        this.teacherCoursesPresenter = teacherCoursesPresenter;
        gson = new Gson();
    }

    public void getTeacherCourses(String teacherId) {
        UserManager.getTeacherCourses(teacherId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {

            }

            @Override
            public void onFailure(String message, int errorCode) {

            }
        });
    }
}
