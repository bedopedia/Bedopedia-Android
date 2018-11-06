package trianglz.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * This file is spawned by Gemy on 11/6/2018.
 */
@SuppressLint("AppCompatCustomView")
public class SfuiSemiBoldTextView extends TextView {
    private Context context;

    public SfuiSemiBoldTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView(context);
    }
    private void createView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/sfui_semibold.ttf");
        setTypeface(tf);
    }
}
