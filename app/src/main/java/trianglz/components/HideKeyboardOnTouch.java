package trianglz.components;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ${Aly} on 11/21/2018.
 */
public class HideKeyboardOnTouch implements View.OnTouchListener {

    private Activity mActivity;

    public HideKeyboardOnTouch (Activity activity){
        mActivity = activity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mActivity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) mActivity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    mActivity.getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }

}

