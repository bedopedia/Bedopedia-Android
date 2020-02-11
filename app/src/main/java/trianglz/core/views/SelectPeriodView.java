package trianglz.core.views;

import org.json.JSONArray;

import java.util.ArrayList;

import trianglz.core.presenters.SelectPeriodPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.GradingPeriod;

public class SelectPeriodView {
    private SelectPeriodPresenter selectPeriodPresenter;

    public SelectPeriodView(SelectPeriodPresenter selectPeriodPresenter) {
        this.selectPeriodPresenter = selectPeriodPresenter;
    }

    public void getGradingPeriods(int courseId) {
        UserManager.getGradingPeriods(courseId, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                ArrayList<GradingPeriod> gradingPeriods = new ArrayList<>();
                for (int i = 0; i < responseArray.length(); i++) {
                    gradingPeriods.add(GradingPeriod.create(responseArray.opt(i).toString()));
                }
                selectPeriodPresenter.onGetGradingPeriodsSuccess(gradingPeriods);
            }

            @Override
            public void onFailure(String message, int errorCode) {
                selectPeriodPresenter.onGetGradingPeriodsFailure(message, errorCode);
            }
        });
    }
}
