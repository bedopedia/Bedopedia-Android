package trianglz.components;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ${Aly} on 11/12/2018.
 */
public class SfproRegularEditTextView extends EditText {
    private Context context;

    public SfproRegularEditTextView(Context context, @Nullable AttributeSet attrs) {
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
