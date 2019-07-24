package trianglz.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Farah A. Moniem on 24/07/2019.
 */
@SuppressLint("AppCompatCustomView")

public class SfuiRegularEditTextView extends EditText {

    private Context context;

    public SfuiRegularEditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView(context);
    }

    private void createView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/sfui_regular.ttf");
        setTypeface(tf);
    }
}
