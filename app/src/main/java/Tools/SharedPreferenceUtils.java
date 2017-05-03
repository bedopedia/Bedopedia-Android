package Tools;

import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ali on 02/05/17.
 */

public class SharedPreferenceUtils {

    public static SharedPreferences getSharedPreference(Context context, String name) {
        return context.getSharedPreferences(name, MODE_PRIVATE);
    }


    public static String getStringValue(String key, String defaultStr, SharedPreferences sharedPreferences) {

        return sharedPreferences.getString(key , defaultStr);
    }



}
