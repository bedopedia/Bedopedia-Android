package trianglz.core.presenters;

import trianglz.models.School;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public interface SchoolLoginPresenter {
    void onGetSchoolUrlSuccess(String url);
    void onGetSchoolUrlFailure(String message,int errorCode);
    void onGetSchoolDataSuccess(School school);
    void onGetSchoolDataFailure(String message,int errorCode);
}
