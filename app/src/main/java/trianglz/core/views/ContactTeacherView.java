package trianglz.core.views;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import trianglz.core.presenters.ContactTeacherPresenter;
import trianglz.managers.api.ResponseListener;
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
        UserManager.getMessages(url, id, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                contactTeacherPresenter.onGetMessagesSuccess(parseGetMessagesResponse(response));
            }

            @Override
            public void onFailure(String message, int errorCode) {
                contactTeacherPresenter.onGetMessagesFailure(message,errorCode);
            }
        });
    }

    private ArrayList<MessageThread> parseGetMessagesResponse(JSONObject threads) {
        ArrayList<MessageThread> messageThreadArrayList = new ArrayList<>();
            JSONArray messageThreads = threads.optJSONArray(Constants.KEY_MESSAGE_THREADS);
        for (int i = 0; i < messageThreads.length(); i++) {
            JSONObject thread = messageThreads.optJSONObject(i);
            int courseId = thread.optInt(Constants.KEY_ID);
            String courseName = thread.optString(Constants.KEY_COURSE_NAME);
            int id = thread.optInt(Constants.KEY_ID);
            boolean isRead = thread.optBoolean(Constants.KEY_IS_READ);
            JSONArray avatarJsonArray =  thread.optJSONArray(Constants.KEY_OTHER_AVATARS);
            String otherAvatar;
            if(avatarJsonArray.length()>0){
                otherAvatar = avatarJsonArray.optString(avatarJsonArray.length()-1);
            }else {
                otherAvatar = avatarJsonArray.toString();
            }
            String otherNames = thread.optString(Constants.KEY_OTHER_NAMES);
            String tag = thread.optString(Constants.KEY_TAG);
            String lastAddedDate = thread.optString(Constants.KEY_LAST_ADDED_DATE);
            String name = thread.optString(Constants.KEY_NAME);
            JSONArray participantsJsonArray = thread.optJSONArray(Constants.KEY_PARTICIPANTS);
            ArrayList<Participant> participantArrayList = new ArrayList<>();
            if(!participantArrayList.isEmpty()){
            for(int p = 0; p < participantsJsonArray.length(); p++){
                JSONObject participantJsonObject = participantsJsonArray.optJSONObject(p);
                String participantName = participantJsonObject.optString(Constants.KEY_NAME);
                int threadId = participantJsonObject.optInt(Constants.KEY_THREAD_ID);
                int userId = participantJsonObject.optInt(Constants.KEY_USER_ID);
                String userAvatarUrl = participantJsonObject.optString(Constants.KEY_USER_AVATAR_URL);
                participantArrayList.add(new Participant(participantName,threadId,userId,userAvatarUrl));
            }}
            JSONArray messages = thread.optJSONArray(Constants.KEY_MESSAGES);
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

