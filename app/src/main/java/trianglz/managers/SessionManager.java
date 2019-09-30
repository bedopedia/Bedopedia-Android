package trianglz.managers;

import android.content.SharedPreferences;

import java.util.HashMap;

import trianglz.utils.Constants;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class SessionManager {
    private static SessionManager mInstance;
    public SharedPreferences mPreferences;
    public SharedPreferences.Editor mEditor;
    private static final String PREF_NAME = App.TAG;
    private static final int PRIVATE_MODE = 0;

    //login
    private String accessToken = "accessToken";
    private String tokenType = "tokenType";
    private String clientCode = "clientCode";
    private String uid = "uid";
    private String userName = "userName";
    private String userId = "userId";
    private String id = "id";
    private String unSeenNotification = "unseen_notifications";

    //firebase
    String tokenKey = "token";


    private String userType = "";


    public static SessionManager getInstance() {
        if (mInstance == null) {
            mInstance = new SessionManager();
        }
        return mInstance;
    }

    private SessionManager() {
        mPreferences = App.getInstance().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPreferences.edit();
    }

    public void createLoginSession(String userName,
                                   String userId, String id, int unSeenNotficationCounter) {
        mEditor.putString(this.userName, userName);
        mEditor.putString(this.userId, userId);
        mEditor.putString(this.id, id);
        mEditor.putInt(this.unSeenNotification, unSeenNotficationCounter);
        mEditor.commit();
    }

    public void setHeadersValue(String accessToken, String tokenType, String clientCode, String uid) {
        mEditor.putString(this.accessToken, accessToken);
        mEditor.putString(this.tokenType, tokenType);
        mEditor.putString(this.clientCode, clientCode);
        mEditor.putString(this.uid, uid);
        mEditor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        mEditor.commit();

    }

    public void setFireBaseToken(String token) {
        mEditor.putString(tokenKey, token);
        mEditor.commit();
    }


    public String getId() {
        return mPreferences.getString(id, "");
    }

    public String getUserId() {
        return mPreferences.getString(userId, "");
    }

    public String getTokenKey() {
        return mPreferences.getString(tokenKey, "");
    }

    public HashMap<String, String> getHeaderHashMap() {
        String token = mPreferences.getString(this.accessToken, "");
        String uid = mPreferences.getString(this.uid, "");
        String client = mPreferences.getString(this.clientCode, "");
        String tokenType = mPreferences.getString(this.tokenType, "");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("access-token", token);
        hashMap.put("uid", uid);
        hashMap.put("client", client);
        hashMap.put("token-type", tokenType);
        return hashMap;
    }

    public void setBaseUrl(String url) {
        mEditor.putString(Constants.KEY_BASE_URL, url);
        mEditor.commit();
    }

    public String getBaseUrl() {
        return mPreferences.getString(Constants.KEY_BASE_URL, "");
    }

    public String getEmail() {
        return mPreferences.getString(Constants.KEY_EMAIL, "");
    }

    public void setloginValues(String schoolUrl, String email, String password) {
        mEditor.putString(Constants.KEY_SCHOOL_URL, schoolUrl);
        mEditor.putString(Constants.KEY_EMAIL, email);
        mEditor.putString(Constants.KEY_PASSWORD, password);
        mEditor.commit();
    }

    public boolean getIsLoggedIn() {
        return mPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }


    public String getSchoolUrl() {
        return mPreferences.getString(Constants.KEY_SCHOOL_URL, "");
    }

    public String getPassword() {
        return mPreferences.getString(Constants.KEY_PASSWORD, "");
    }

    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }

    public void setNotificationCounterToZero() {
        mEditor.putInt(this.unSeenNotification, 0);
        mEditor.commit();
    }

    public int getNotficiationCounter() {
        return mPreferences.getInt(this.unSeenNotification, 0);
    }


    public void setUserType(Actor actor) {
        mEditor.putString(Constants.KEY_USER_TYPE, actor.text);
        mEditor.commit();
    }

    public String getUserType() {
        return mPreferences.getString(Constants.KEY_USER_TYPE, "");
    }


    public enum Actor {
        TEACHER("teacher"),
        STUDENT("student"),
        PARENT("parent"),
        HOD("hod"),
        ADMIN("admin"),
        ;
        private final String text;
        Actor(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}
