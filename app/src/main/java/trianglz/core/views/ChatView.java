package trianglz.core.views;

import android.content.Context;

import org.json.JSONObject;

import trianglz.core.presenters.ChatPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.MessageAttributes;

/**
 * Created by ${Aly} on 11/12/2018.
 */
public class ChatView {
    private Context context;
    private ChatPresenter chatPresenter;

    public ChatView(Context context, ChatPresenter chatPresenter) {
        this.context = context;
        this.chatPresenter = chatPresenter;
    }


    public void sendMessage(String url, String body, String messageThreadId, String userId,
                            String id, String threadName) {

        UserManager.sendMessage(url, body, messageThreadId, userId, id, threadName, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String message, int errorCode) {

            }
        });

    }
}
