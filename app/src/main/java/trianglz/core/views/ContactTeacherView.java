package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.ContactTeacherPresenter;
import trianglz.managers.api.ArrayResponseListener;
import trianglz.managers.api.UserManager;
import trianglz.models.Message;
import trianglz.models.MessageThread;
import trianglz.models.Participant;
import trianglz.models.User;
import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 11/11/2018.
 */
public class ContactTeacherView {
    private Context context;
    private ContactTeacherPresenter contactTeacherPresenter;

    public ContactTeacherView(Context context, ContactTeacherPresenter contactTeacherPresenter) {
        this.context = context;
        this.contactTeacherPresenter = contactTeacherPresenter;
    }

    public void getMessages(String url, String id) {
        UserManager.getMessages(url, id, new ArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray responseArray) {
                contactTeacherPresenter.onGetMessagesSuccess(parseGetMessagesResponse(responseArray));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                contactTeacherPresenter.onGetMessagesFailure(message,errorCode);
            }
        });
    }

    private ArrayList<MessageThread> parseGetMessagesResponse(JSONArray threads) {
        ArrayList<MessageThread> messageThreadArrayList = new ArrayList<>();
        for (int i = 0; i < threads.length(); i++) {
            JSONObject messageThread = threads.optJSONObject(i);
            int courseId = messageThread.optInt(Constants.KEY_COURSE_ID);
            String courseName = messageThread.optString(Constants.KEY_COURSE_NAME);
            int id = messageThread.optInt(Constants.KEY_ID);
            boolean isRead = messageThread.optBoolean(Constants.KEY_IS_READ);
            String otherAvatar = messageThread.optJSONArray(Constants.KEY_OTHER_AVATARS).toString();
            String otherNames = messageThread.optString(Constants.KEY_OTHER_NAMES);
            String tag = messageThread.optString(Constants.KEY_TAG);
            String lastAddedDate = messageThread.optString(Constants.KEY_LAST_ADDED_DATE);
            String name = messageThread.optString(Constants.KEY_NAME);
            JSONArray participantsJsonArray = messageThread.optJSONArray(Constants.KEY_PARTICIPANTS);
            ArrayList<Participant> participantArrayList = new ArrayList<>();
            for(int p = 0; p < participantsJsonArray.length(); p++){
                JSONObject participantJsonObject = participantsJsonArray.optJSONObject(p);
                String participantName = participantJsonObject.optString(Constants.KEY_NAME);
                int threadId = participantJsonObject.optInt(Constants.KEY_THREAD_ID);
                int userId = participantJsonObject.optInt(Constants.KEY_USER_ID);
                String userAvatarUrl = participantJsonObject.optString(Constants.KEY_USER_AVATAR_URL);
                participantArrayList.add(new Participant(participantName,threadId,userId,userAvatarUrl));
            }
            JSONArray messages = messageThread.optJSONArray(Constants.KEY_MESSAGES);
            ArrayList<Message> messageArrayList = new ArrayList<>();
            for (int j = 0; j < messages.length(); j++) {
                JSONObject messageObj = messages.optJSONObject(j);
                JSONObject user = messageObj.optJSONObject(Constants.KEY_USER);
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
                Message message = new Message(attachmentUrl,body, createdAt, ext,fileName,userId,messageThreadId,updatedAt, sender);
                messageArrayList.add(message);
            }

            messageThreadArrayList.add(new MessageThread(courseId,courseName,id,isRead,lastAddedDate
                    ,messageArrayList,name,otherAvatar,otherNames,participantArrayList,tag));
        }

        return messageThreadArrayList;
    }


}

