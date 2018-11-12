package trianglz.core.presenters;

/**
 * Created by ${Aly} on 11/12/2018.
 */
public interface ChatPresenter {
    void onSendMessageSuccess();
    void onSendMessageFailure(String message,int errorCode);
}
