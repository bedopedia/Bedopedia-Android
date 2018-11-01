package trianglz.managers;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.androidnetworking.AndroidNetworking;
import com.skolera.skolera_android.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.io.File;

/**
 * Created by ${Aly} on 10/24/2018.
 */

@ReportsCrashes(mailTo = "aliabdelrahmanweka74@gmail.com", // my email here
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
        ACRA.init(this);
    }

}