package trianglz.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.skolera.skolera_android.R;

import trianglz.components.LoadingDialog;
import trianglz.components.LocalHelper;
import trianglz.core.presenters.SuperPresenter;
import trianglz.core.views.SuperView;
import trianglz.managers.SessionManager;

public class SuperActivity extends AppCompatActivity implements SuperPresenter {
    // public ProgressDialog progress;
    public LoadingDialog progress;
    private SuperView superView;

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
        new MaterialDialog.Builder(context)
                .title("Skolera")
                .content(content)
                .titleColor(context.getResources().getColor(R.color.jade_green))
                .neutralText(context.getResources().getString(R.string.ok))
                .neutralColor(context.getResources().getColor(R.color.jade_green))
                .contentColor(context.getResources().getColor(R.color.steel))
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        if (errorCode == 401) {
                            logoutUser(context);
                        }
                    }
                })
                .show();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base, "en"));
    }
}
