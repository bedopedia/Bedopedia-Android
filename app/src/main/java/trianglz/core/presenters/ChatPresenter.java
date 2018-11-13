package trianglz.core.presenters;

import trianglz.models.MessageThread;

/**
 * Created by ${Aly} on 11/12/2018.
 */
public interface ChatPresenter {
    void onSendMessageSuccess();
    void onSendMessageFailure(String message,int errorCode);
    void onFirstMessageSuccess(MessageThread messageThread);
    void onFirstMessageFailure(String message,int errorCode);
}
