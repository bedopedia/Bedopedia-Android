package trianglz.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skolera.skolera_android.R;

/**
 * Created by ${Aly} on 11/14/2018.
 */
public class SettingsDialog extends Dialog implements View.OnClickListener {
    private Button languageBtn, signoutBtn, cancelBtn;
    private Context context;
    private SettingsDialogInterface settingsDialogInterface;
    public LinearLayout itemLayout;
    private android.os.Handler handler;


    public SettingsDialog(@NonNull Context context, @StyleRes int themeResId, SettingsDialogInterface settingsDialogInterface) {
        super(context, themeResId);
        this.context = context;
        this.settingsDialogInterface = settingsDialogInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_settings);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setBackgroundDrawableResource((R.color.dialog_background_color));
        }
        bindViews();
        setListeners();

    }


    private void bindViews() {

        languageBtn = findViewById(R.id.btn_language);
        signoutBtn = findViewById(R.id.btn_sign_out);
        cancelBtn = findViewById(R.id.btn_cancel);
        itemLayout = findViewById(R.id.item_layout);
        handler = new Handler();
    }


    private void setListeners() {
        cancelBtn.setOnClickListener(this);
        languageBtn.setOnClickListener(this);
        signoutBtn.setOnClickListener(this);
        itemLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                cancel();
                break;
            case R.id.btn_language:
                settingsDialogInterface.onChangeLanguageClicked();
                cancel();
                break;
            case R.id.btn_sign_out:
                settingsDialogInterface.onSignOutClicked();
                cancel();
                break;
            case R.id.item_layout:
                cancel();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(context.getResources().getColor(R.color.dialog_background_color));
                }
                break;


        }
    }


    public interface SettingsDialogInterface {

        void onChangeLanguageClicked();

        void onSignOutClicked();

    }


}