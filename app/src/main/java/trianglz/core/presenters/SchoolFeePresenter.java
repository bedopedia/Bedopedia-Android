package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Meta;
import trianglz.models.Notification;
import trianglz.models.SchoolFee;

 public interface SchoolFeePresenter {
    void onGetSchoolFeesSuccess(ArrayList<SchoolFee> schoolFees, Meta meta);
    void onGetSchoolFeesFailure(String message, int errorCode);
}
