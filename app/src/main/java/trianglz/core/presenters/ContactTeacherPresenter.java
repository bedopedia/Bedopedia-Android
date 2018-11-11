package trianglz.core.presenters;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Map;

import trianglz.models.MessageThread;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public interface ContactTeacherPresenter {
    void onGetMessagesSuccess( ArrayList<MessageThread> messageThreadArrayList);
    void onGetMessagesFailure(String message,int errorCode);
}
