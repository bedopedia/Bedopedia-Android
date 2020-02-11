package trianglz.core.views;

import org.json.JSONArray;

import trianglz.core.presenters.SelectPeriodPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;

public class SelectPeriodView {
    private SelectPeriodPresenter selectPeriodPresenter;

    public SelectPeriodView(SelectPeriodPresenter selectPeriodPresenter) {
        this.selectPeriodPresenter = selectPeriodPresenter;
    }

    public void getGradingPeriods(int courseId) {
        UserManager.getGradingPeriods(courseId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                selectPeriodPresenter.onGetGradingPeriodsSuccess();
            }

            @Override
            public void onFailure(String message, int errorCode) {
                selectPeriodPresenter.onGetGradingPeriodsFailure(message, errorCode);
            }
        });
    }
}
