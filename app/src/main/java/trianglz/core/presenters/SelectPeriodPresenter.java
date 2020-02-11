package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.GradingPeriod;

public interface SelectPeriodPresenter {
    void onGetGradingPeriodsSuccess(ArrayList<GradingPeriod> gradingPeriods);
    void onGetGradingPeriodsFailure (String message, int errorCode);
}
