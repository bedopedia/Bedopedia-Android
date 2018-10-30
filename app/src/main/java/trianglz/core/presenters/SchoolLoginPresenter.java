package trianglz.core.presenters;

import trianglz.models.School;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public interface SchoolLoginPresenter {
    void onGetSchoolUrlSuccess(String url);
    void onGetSchoolUrlFailure();
    void onGetSchoolDataSuccess(School school);
    void onGetSchoolDataFailure();
}
