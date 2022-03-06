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
public class CircularBookTextView extends TextView {
    private Context context;

    public CircularBookTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView(context);
    }
    private void createView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/circular_book.ttf");
        setTypeface(tf);
    }
}
