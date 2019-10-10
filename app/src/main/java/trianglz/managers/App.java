package trianglz.managers;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.androidnetworking.AndroidNetworking;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.skolera.skolera_android.R;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;
//import com.vanniktech.emoji.google.GoogleEmojiProvider;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.io.File;

import trianglz.components.LocalHelper;

/**
 * Created by ${Aly} on 10/24/2018.
 */

@ReportsCrashes(mailTo = "skoleraulp@gmail.com", 
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

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
        ACRA.init(this);
        Fresco.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocalHelper.onAttach(context,"en"));
        MultiDex.install(this);
    }

}
