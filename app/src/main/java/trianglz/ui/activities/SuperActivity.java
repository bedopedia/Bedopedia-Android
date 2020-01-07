package trianglz.ui.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.skolera.skolera_android.R;

import trianglz.components.ErrorDialog;
import trianglz.components.LoadingDialog;
import trianglz.components.LocalHelper;
import trianglz.core.presenters.SuperPresenter;
import trianglz.core.views.SuperView;
import trianglz.managers.SessionManager;

public class SuperActivity extends AppCompatActivity implements SuperPresenter {
    // public ProgressDialog progress;
    public LoadingDialog progress;
    private SuperView superView;
    public ErrorDialog errorDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        superView = new SuperView(this, this);
        progress = new LoadingDialog(this, R.style.LoadingDialog);
        progress.setCancelable(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkVersionOnStore();
    }

    public void showLoadingDialog() {
      //  progress.setTitle(R.string.LoadDialogueTitle);
        //     progress.setMessage(getString(R.string.LoadDialogueBody));
        progress.show();
    }

    public void logoutUser(Context context) {
        SessionManager.getInstance().logoutUser();
        openSchoolLoginActivity(context);
        superView.updateToken();

    }

    private void openSchoolLoginActivity(Context context) {
        Intent intent = new Intent(context, SchoolLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void showErrorDialog(Context context, int errorCode, String content) {
        if (errorCode == 401 || errorCode == 500) {
            content = context.getResources().getString(R.string.system_error);
        } else if (errorCode != -3) {
            content = context.getResources().getString(R.string.something_went_wrong);
        }
        errorDialog = new ErrorDialog(context,content, ErrorDialog.DialogType.ERROR);
        errorDialog.show();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base, "en"));
    }


    private void checkVersionOnStore() {
        AppUpdater appUpdater = new AppUpdater(this)
                .setDisplay(Display.DIALOG)
                .showEvery(1)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setTitleOnUpdateAvailable(getResources().getString(R.string.update_is_available))
                .setContentOnUpdateAvailable(getResources().getString(R.string.check_latest_version))
                .setButtonUpdate(getResources().getString(R.string.cancel))
                .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setButtonDismiss("")
                .setButtonDoNotShowAgain(getResources().getString(R.string.update_now))
                .setButtonDoNotShowAgainClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openStore();
                    }
                })
                .setCancelable(false); // Dialog could not be;
        appUpdater.start();
    }

    private void openStore() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + getPackageName())));
        }
    }
}
