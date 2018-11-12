package trianglz.core.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.skolera.skolera_android.MessageThreadActivity;

import java.util.HashMap;
import java.util.Map;

import Models.MessageAttributes;
import Tools.SharedPreferenceUtils;
import login.Services.ApiClient;
import login.Services.ApiInterface;
import trianglz.core.presenters.ChatPresenter;

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


    public void sendMessage(trianglz.models.MessageAttributes messageAttributes) {

    }
}
