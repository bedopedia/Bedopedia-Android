package trianglz.core.presenters;

import trianglz.models.SingleAssignment;

/**
 * Created by gemy on 7/24/19.
 */
public interface SingleAssignmentPresenter {
    void onShowAssignmentSuccess(SingleAssignment singleAssignment);
    void onShowAssignmentFailure(String message, int errorCode);
}
