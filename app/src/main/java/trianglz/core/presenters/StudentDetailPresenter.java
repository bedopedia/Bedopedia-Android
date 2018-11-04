package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.CourseGroup;

/**
 * Created by ${Aly} on 11/4/2018.
 */
public interface StudentDetailPresenter {
    void onGetStudentGradesSuccess(ArrayList<CourseGroup> courseGroups);
    void onGetStudentGradesFailure(String message,int code);

    void oneGetTimeTableSuccess(ArrayList<Object> timeTableData);
    void onGetTimeTableFailure(String message,int code);
}
