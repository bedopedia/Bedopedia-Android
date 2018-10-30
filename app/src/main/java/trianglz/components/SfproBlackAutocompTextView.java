package trianglz.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * This file is spawned by Gemy on 10/30/2018.
 */
@SuppressLint("AppCompatCustomView")
public class SfproBlackAutocompTextView extends AutoCompleteTextView {
    private Context context;
    public SfproBlackAutocompTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        CreateView(context);
    }

    private void CreateView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/sf_black.otf");
        setTypeface(tf);
    }
}
