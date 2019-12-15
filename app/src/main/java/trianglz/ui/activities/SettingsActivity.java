package trianglz.ui.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import trianglz.components.ChangePasswordDialog;
import trianglz.components.ErrorDialog;
import trianglz.components.LocalHelper;

public class SettingsActivity extends SuperActivity implements View.OnClickListener, ErrorDialog.DialogConfirmationInterface, ChangePasswordDialog.DialogConfirmationInterface {

    private ConstraintLayout changeLanguageConstraintLayout;
    private ErrorDialog errorDialogue;
    private TextView versionTextView;
    private ImageButton backImageButton;
    private Button changePasswordButton, shareAppButton, rateAppButton, signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bindViews();
        setListeners();
    }

    private void bindViews() {
        changeLanguageConstraintLayout = findViewById(R.id.layout_change_language);
        changePasswordButton = findViewById(R.id.btn_change_password);
        backImageButton = findViewById(R.id.btn_back);
        versionTextView = findViewById(R.id.tv_version);
        shareAppButton = findViewById(R.id.btn_share_app);
        rateAppButton = findViewById(R.id.btn_rate_app);
        signOutButton = findViewById(R.id.btn_sign_out);
        String version = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = String.format(getResources().getString(R.string.version), pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!version.equals("")) {
            versionTextView.setText(version);
        } else {
            versionTextView.setVisibility(View.INVISIBLE);
        }

    }

    private void setListeners() {
        changeLanguageConstraintLayout.setOnClickListener(this);
        backImageButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
        shareAppButton.setOnClickListener(this);
        rateAppButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.layout_change_language:
                errorDialogue = new ErrorDialog(this, getResources().getString(R.string.restart_application), ErrorDialog.DialogType.CONFIRMATION, this);
                errorDialogue.show();
                break;
            case R.id.btn_change_password:
                ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(this, this);
                changePasswordDialog.show();
                break;
            case R.id.btn_share_app:
                shareApp();
                break;
            case R.id.btn_rate_app:
                rateApp();
                break;
            case R.id.btn_sign_out:
                logoutUser(this);
                break;
        }
    }

    @Override
    public void onConfirm() {
        changeLanguage();
    }

    @Override
    public void onCancel() {
        errorDialogue.dismiss();
    }

    private void changeLanguage() {
        if (LocalHelper.getLanguage(this).equals("ar")) {
            updateViews("en");
        } else {
            updateViews("ar");
        }
    }


    private void updateViews(String languageCode) {

        LocalHelper.setLocale(this, languageCode);
        LocalHelper.getLanguage(this);
        new Handler().postDelayed(this::restartApp, 500);
    }

    public void restartApp() {
        Intent intent = this.getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(this.getBaseContext().getPackageName());
        assert intent != null;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
        new Handler().postDelayed(() -> Runtime.getRuntime().exit(0), 0);
    }

    private void shareApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getPackageName());
            String sAux = "\n" + getResources().getString(R.string.try_app_now) + "\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, this.getResources().getString(R.string.share_via)));
        } catch (Exception e) {
            //e.toString();
        }
    }

    private void rateApp() {
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

    @Override
    public void onUpdate() {

    }
}