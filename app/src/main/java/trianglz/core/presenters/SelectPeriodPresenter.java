package trianglz.core.presenters;

public interface SelectPeriodPresenter {
    void onGetGradingPeriodsSuccess();
    void onGetGradingPeriodsFailure (String message, int errorCode);
}
