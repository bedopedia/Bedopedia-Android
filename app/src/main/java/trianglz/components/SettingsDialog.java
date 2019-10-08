package trianglz.components;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.skolera.skolera_android.R;

/**
 * Created by ${Aly} on 11/14/2018.
 */
public class SettingsDialog extends BottomSheetDialog implements View.OnClickListener, DialogInterface.OnShowListener {
    private Button languageBtn, signoutBtn, cancelBtn;
    private Context context;
    private SettingsDialogInterface settingsDialogInterface;
    public LinearLayout itemLayout;


    public SettingsDialog(@NonNull Context context, @StyleRes int themeResId, SettingsDialogInterface settingsDialogInterface) {
        super(context, themeResId);
        this.context = context;
        this.settingsDialogInterface = settingsDialogInterface;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_settings);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setBackgroundDrawableResource((R.color.dialog_background_color));
//        }
        bindViews();
        setListeners();
        getWindow().findViewById(R.id.design_bottom_sheet);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setDimAmount((float) 0.3);

    }


    private void bindViews() {

        languageBtn = findViewById(R.id.btn_language);
        signoutBtn = findViewById(R.id.btn_sign_out);
        cancelBtn = findViewById(R.id.btn_cancel);
        itemLayout = findViewById(R.id.item_layout);

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

    @Override
    public void onShow(DialogInterface dialog) {
        BottomSheetDialog d = (BottomSheetDialog) dialog;
        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
        CoordinatorLayout layout = (CoordinatorLayout) bottomSheet.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(bottomSheet.getHeight());
        layout.getParent().requestLayout();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //   getWindow().setBackgroundDrawableResource(R.color.transparent);

    }

    public interface SettingsDialogInterface {

        void onChangeLanguageClicked();

        void onSignOutClicked();

    }


}