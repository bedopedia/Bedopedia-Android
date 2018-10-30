package trianglz.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * This file is spawned by Gemy on 10/30/2018.
 */
@SuppressLint("AppCompatCustomView")
public class SfproBlackEditText extends EditText {
    private Context context;

    public SfproBlackEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        CreatView(context);
    }

    private void CreatView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/sf_black.otf");
        setTypeface(tf);
    }
}
