package trianglz.core.presenters;

/**
 * Created by Farah A. Moniem on 30/07/2019.
 */
public interface CreatePersonalEventPresenter {
    void onCreateEventSuccess();
    void onCreateEventFailure(String message, int errorCode);

}
