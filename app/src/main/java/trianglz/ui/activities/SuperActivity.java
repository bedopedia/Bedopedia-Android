package trianglz.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.skolera.skolera_android.R;

import trianglz.components.LocalHelper;
import trianglz.managers.SessionManager;

public class SuperActivity extends AppCompatActivity {
    public ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
    }


    public void showLoadingDialog(){
        progress.setTitle(R.string.LoadDialogueTitle);
        progress.setMessage(getString(R.string.LoadDialogueBody));
        progress.show();
    }
    public void logoutUser(Context context) {
        SessionManager.getInstance().logoutUser();
        openSchoolLoginActivity(context);

    }

    private void openSchoolLoginActivity(Context context) {
        Intent intent = new Intent(context, SchoolLoginActivity.class);
        startActivity(intent);
    }




    public static void showErrorDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title("Skolera")
                .content(context.getResources().getString(R.string.system_error))
                .titleColor(context.getResources().getColor(R.color.jade_green))
                .neutralText(context.getResources().getString(R.string.ok))
                .neutralColor(context.getResources().getColor(R.color.jade_green))
                .contentColor(context.getResources().getColor(R.color.steel))
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                    }
                })
                .show();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base,"en"));
    }
}
