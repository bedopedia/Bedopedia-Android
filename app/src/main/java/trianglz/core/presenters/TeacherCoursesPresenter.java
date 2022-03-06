package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.TeacherCourse;

public interface TeacherCoursesPresenter {
    void onGetTeacherCoursesSuccess(ArrayList<TeacherCourse> teacherCourses);
    void onGetTeacherCoursesFailure(String message, int errorCode);
}
