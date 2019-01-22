package trianglz.core.views;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import trianglz.core.presenters.ChatPresenter;
import trianglz.managers.api.ResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Message;
import trianglz.models.MessageThread;
import trianglz.models.Participant;
import trianglz.models.User;
import trianglz.utils.Constants;

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
                chatPresenter.onSendMessageFailure(message,errorCode);
            }
        });

    }

    public void sendImage(String url, String fileName, Uri uri) {
        URI uri1 = null;
        try {
            uri1 = new URI(uri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        UserManager.sendImage(url, fileName, uri1, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String message, int errorCode) {
            }
        });
    }
    private void vibrate () {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    public void sendFirstMessage(String url, String teacherId, String userId, String body, String courseId) {
        UserManager.sendFirstMessage(url, teacherId, userId, body, courseId, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                chatPresenter.onFirstMessageSuccess(parseFirstMessageResponse(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                chatPresenter.onFirstMessageFailure(message, errorCode);
            }
        });
    }


    private MessageThread parseFirstMessageResponse(JSONObject messageThread) {
        int courseId = messageThread.optInt(Constants.KEY_COURSE_ID);
        String courseName = messageThread.optString(Constants.KEY_COURSE_NAME);
        int id = messageThread.optInt(Constants.KEY_ID);
        boolean isRead = messageThread.optBoolean(Constants.KEY_IS_READ);
        String otherAvatar = messageThread.optJSONArray(Constants.KEY_OTHER_AVATARS).toString();
        String otherNames = messageThread.optString(Constants.KEY_OTHER_NAMES);
        String tag = messageThread.optString(Constants.KEY_TAG);
        String lastAddedDate = messageThread.optString(Constants.KEY_LAST_ADDED_DATE);
        String name = messageThread.optString(Constants.KEY_NAME);
        JSONObject user = messageThread.optJSONObject(Constants.KEY_CREATOR);
        JSONArray participantsJsonArray = messageThread.optJSONArray(Constants.KEY_PARTICIPANTS);
        ArrayList<Participant> participantArrayList = new ArrayList<>();
        for (int p = 0; p < participantsJsonArray.length(); p++) {
            JSONObject participantJsonObject = participantsJsonArray.optJSONObject(p);
            String participantName = participantJsonObject.optString(Constants.KEY_NAME);
            int threadId = participantJsonObject.optInt(Constants.KEY_THREAD_ID);
            int userId = participantJsonObject.optInt(Constants.KEY_USER_ID);
            String userAvatarUrl = participantJsonObject.optString(Constants.KEY_USER_AVATAR_URL);
            participantArrayList.add(new Participant(participantName, threadId, userId, userAvatarUrl));
        }
        JSONArray messages = messageThread.optJSONArray(Constants.KEY_MESSAGES);
        ArrayList<Message> messageArrayList = new ArrayList<>();
        for (int j = 0; j < messages.length(); j++) {
            JSONObject messageObj = messages.optJSONObject(j);
            int userId = user.optInt(Constants.KEY_ID);
            String firstName = user.optString(Constants.KEY_FIRST_NAME);
            String lastName = user.optString(Constants.KEY_LAST_NAME);
            String gender = user.optString(Constants.KEY_GENDER);
            String avatarUrl = user.optString(Constants.KEY_AVATER_URL);
            String userType = user.optString(Constants.KEY_USER_TYPE);
            User sender = new User(userId, firstName, lastName, gender, "",
                    avatarUrl, userType);
            String attachmentUrl = messageObj.opt(Constants.KEY_ATTACHMENT_URL).toString();
            String fileName = messageObj.opt(Constants.KEY_FILE_NAME).toString();
            String ext = messageObj.opt(Constants.KEY_EXT).toString();
            String body = messageObj.optString(Constants.KEY_BODY);
            String createdAt = messageObj.optString(Constants.KEY_CREATED_AT);
            String updatedAt = messageObj.optString(Constants.KEY_UPADTED_AT);
            int messageThreadId = messageObj.optInt(Constants.KEY_ID);
            Message message = new Message(attachmentUrl, body, createdAt, ext, fileName, userId, messageThreadId, updatedAt, sender);
            messageArrayList.add(message);
        }

        return new MessageThread(courseId, courseName, id, isRead, lastAddedDate
                , messageArrayList, name, otherAvatar, otherNames, participantArrayList, tag);
    }

}
