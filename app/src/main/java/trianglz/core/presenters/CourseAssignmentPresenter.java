package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.AssignmentsDetail;
import trianglz.models.CourseAssignment;

/**
 * Created by ${Aly} on 6/20/2019.
 */
public interface CourseAssignmentPresenter {
    void onGetCourseAssignmentSuccess(ArrayList<CourseAssignment> courseAssignmentArrayList);
    void onGetCourseAssignmentFailure(String message, int errorCode);
    void onGetAssignmentDetailSuccess(ArrayList<AssignmentsDetail> assignmentsDetailArrayList);
    void onGetAssignmentDetailFailure(String message,int errorCode);
}
