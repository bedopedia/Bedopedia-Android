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
    private String userName ="userName";
    private String userId = "userId";
    private String id = "id";

    //firebase
    String tokenKey = "token";
    String token_changedKey = "token_changed";
    boolean TrueKey = true;



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

    public void createLoginSession(String accessToken, String tokenType, String clientCode, String uid, String userName,
                                   String userId, String id){
//        mEditor.putString(this.accessToken, accessToken);
//        mEditor.putString(this.tokenType, tokenType);
//        mEditor.putString(this.clientCode, clientCode);
//        mEditor .putString(this.uid,uid);
        mEditor.putString(this.userName, userName);
        mEditor.putString(this.userId, userId);
        mEditor.putString(this.id, id);
        mEditor.commit();
    }

    public void setHeadersValue(String accessToken, String tokenType, String clientCode, String uid){
        mEditor.putString(this.accessToken, accessToken);
        mEditor.putString(this.tokenType, tokenType);
        mEditor.putString(this.clientCode, clientCode);
        mEditor .putString(this.uid,uid);
        mEditor.commit();

    }

    public void setFireBaseToken(String token){
        mEditor.putString(tokenKey,token);
        mEditor.putBoolean(token_changedKey,TrueKey);
        mEditor.commit();
    }

    public boolean getTokenChangedValue(){
        return mPreferences.getBoolean(token_changedKey,true);
    }

    public void setTokenChangedValue(boolean trueKey){
        mEditor.putBoolean(token_changedKey,trueKey);
        mEditor.commit();
    }

    public String getId(){
        return mPreferences.getString(id,"");
    }
    public String getTokenKey(){
        return mPreferences.getString(tokenKey,"");
    }

    public HashMap<String,String> getHeaderHashMap(){
        String token = mPreferences.getString(this.accessToken,"");
        String uid = mPreferences.getString(this.uid,"");
        String client = mPreferences.getString(this.clientCode,"");
        String tokenType = mPreferences.getString(this.tokenType,"");
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("access-token", token);
        hashMap.put("uid", uid);
        hashMap.put("client", client);
        hashMap.put("token-type", tokenType);
        return hashMap;
    }

    public  void setBaseUrl(String url) {
        mEditor.putString(Constants.KEY_BASE_URL, url);
        mEditor.commit();
    }
    public String getBaseUrl(){
        return mPreferences.getString(Constants.KEY_BASE_URL,"");
    }

}