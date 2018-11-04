package trianglz.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * This file is spawned by Gemy on 10/30/2018.
 */
@SuppressLint("AppCompatCustomView")
public class SfproRegularTextView extends AutoResizeTextView {
    private Context context;

    public SfproRegularTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView(context);
    }
    private void createView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/sf_regular.otf");
        setTypeface(tf);
    }
}
