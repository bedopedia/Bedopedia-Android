package login;



import com.skolera.skolera_android.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;



/**
 * Created by ali on 09/07/17.
 */

@ReportsCrashes(mailTo = "skoleraulp@gmail.com", // my email here
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

public class MyApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }



}
