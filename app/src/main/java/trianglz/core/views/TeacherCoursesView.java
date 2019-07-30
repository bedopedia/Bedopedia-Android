package trianglz.core.views;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.core.presenters.TeacherCoursesPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.TeacherCourse;

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
                ArrayList<TeacherCourse> teacherCourses = new ArrayList<>();
                for (int i = 0; i < responseArray.length(); i++) {
                    teacherCourses.add(TeacherCourse.create(responseArray.opt(i).toString()));
                }
                teacherCoursesPresenter.onGetTeacherCoursesSuccess(teacherCourses);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                teacherCoursesPresenter.onGetTeacherCoursesFailure(message, errorCode);
            }
        });
    }
}
