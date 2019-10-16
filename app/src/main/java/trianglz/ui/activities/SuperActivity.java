package trianglz.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
}
