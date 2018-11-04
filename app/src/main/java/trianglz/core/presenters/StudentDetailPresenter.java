package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.CourseGroup;

/**
 * Created by ${Aly} on 11/4/2018.
 */
public interface StudentDetailPresenter {
    void onGetStudentCourseGroupSuccess(ArrayList<CourseGroup> courseGroups);
    void onGetStudentCourseGroupFailure(String message,int code);

    void onGetStudentGradesSuccess(ArrayList<CourseGroup> courseGroups,String totalGrade);
    void onGetStudentGradesFailure(String message,int code);

    void oneGetTimeTableSuccess(ArrayList<Object> timeTableData);
    void onGetTimeTableFailure(String message,int code);
}
