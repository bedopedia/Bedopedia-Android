package trianglz.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skolera.skolera_android.R;

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
}
