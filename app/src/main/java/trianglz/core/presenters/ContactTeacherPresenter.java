package trianglz.core.presenters;

import java.util.ArrayList;

import trianglz.models.Message;
import trianglz.models.MessageThread;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public interface ContactTeacherPresenter {
    void onGetMessagesSuccess(ArrayList<MessageThread> messageThreadArrayList);

    void onGetMessagesFailure(String message, int errorCode);

    void onGetSingleThreadSuccess(ArrayList<Message> messages, int position);

    void onGetSingleThreadFailure(String message, int errorCode);
}
