package trianglz.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * This file is spawned by Gemy on 11/6/2018.
 */
@SuppressLint("AppCompatCustomView")
public class SfuiBoldTextView extends TextView {
    private Context context;

    public SfuiBoldTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView(context);
    }
    private void createView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/sfui_bold.ttf");
        setTypeface(tf);
    }
}
