package trianglz.core.presenters;

import java.util.ArrayList;
import java.util.HashMap;

import trianglz.models.Assignment;
import trianglz.models.CourseGradingPeriods;
import trianglz.models.GradeItem;
import trianglz.models.Quiz;

/**
 * Created by ${Aly} on 11/7/2018.
 */
public interface GradeDetailPresenter {
    void onGetAverageGradesSuccess(HashMap<String,Double> quizzesHashMap,
                                   HashMap<String,Double> assignmentsHashMap,
                                   HashMap<String,Double> gradeItemHashMap);
    void onGetAverageGradeFailure(String message,int errorCode);
    void onGetStudentGradeBookSuccess(ArrayList<Assignment> assignmentArrayList,
                                      ArrayList<Quiz> quizArrayList, ArrayList<GradeItem> gradeItemArrayList);
    void onGetStudentGradeBookFailure(String message,int errorCode);
    void onGetSemestersSuccess(ArrayList<CourseGradingPeriods> courseGradingPeriodsArrayList);
    void onGetSemesterFailure(String message, int errorCode);
}
