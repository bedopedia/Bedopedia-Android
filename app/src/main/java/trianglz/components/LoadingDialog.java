package trianglz.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.WindowManager;

import com.skolera.skolera_android.R;
import com.wang.avi.AVLoadingIndicatorView;

import trianglz.managers.SessionManager;

/**
 * Created by Farah A. Moniem on 09/10/2019.
 */
public class LoadingDialog extends Dialog {
    private Context mContext;
    private AVLoadingIndicatorView avLoadingIndicatorView;

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.layout_loading_dialog);
        avLoadingIndicatorView = findViewById(R.id.avi);
        setIndicatorColor();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setDimAmount((float) 0.0);
        getWindow().setBackgroundDrawableResource(R.color.transparent_white);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void onBackPressed() {
    }

    public void setIndicatorColor() {
        Log.d(SessionManager.getInstance().getUserType() + "  ", "setIndicatorColor: ");
        if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.STUDENT.toString())) {
            avLoadingIndicatorView.setIndicatorColor(mContext.getResources().getColor(R.color.salmon));
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.PARENT.toString())) {
            avLoadingIndicatorView.setIndicatorColor(mContext.getResources().getColor(R.color.turquoise_blue_two));
        } else if (SessionManager.getInstance().getUserType().equals(SessionManager.Actor.TEACHER.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.ADMIN.toString()) || SessionManager.getInstance().getUserType().equals(SessionManager.Actor.HOD.toString())) {
            avLoadingIndicatorView.setIndicatorColor(mContext.getResources().getColor(R.color.cerulean_blue));
        } else {
            avLoadingIndicatorView.setIndicatorColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
    }
}
