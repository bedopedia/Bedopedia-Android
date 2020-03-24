package trianglz.managers;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.androidnetworking.AndroidNetworking;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import trianglz.components.LocalHelper;

//import com.vanniktech.emoji.google.GoogleEmojiProvider;

/**
 * Created by ${Aly} on 10/24/2018.
 */


public class App extends Application {
    public static final String TAG = App.class
            .getSimpleName();

    private static App mApp;

    public static App getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        AndroidNetworking.initialize(getApplicationContext());
        EmojiManager.install(new GoogleEmojiProvider());
        Fresco.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocalHelper.onAttach(context,"en"));
        MultiDex.install(this);
    }

}
