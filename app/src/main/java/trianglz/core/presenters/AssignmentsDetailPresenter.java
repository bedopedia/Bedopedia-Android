package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.PendingAssignment;

/**
 * Created by ${Aly} on 4/22/2019.
 */
public interface AssignmentsDetailPresenter {
    void onGetAssignmentDetailSuccess(ArrayList<PendingAssignment> pendingAssignmentArrayList);
    void onGetAssignmentDetailFailure(String message, int errorCode);
}
