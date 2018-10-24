package trianglz.managers;

import android.content.SharedPreferences;

/**
 * Created by ${Aly} on 10/24/2018.
 */
public class SessionManager {
    private static SessionManager mInstance;
    public SharedPreferences mPreferences;
    public SharedPreferences.Editor mEditor;
    private static final String PREF_NAME = App.TAG;
    private static final int PRIVATE_MODE = 0;


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

}
