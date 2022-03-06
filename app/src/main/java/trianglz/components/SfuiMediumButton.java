package trianglz.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class SfuiMediumButton extends Button {
    private Context context;

    public SfuiMediumButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView(context);
    }
    private void createView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/sfui_medium.ttf");
        setTypeface(tf);
    }
}
