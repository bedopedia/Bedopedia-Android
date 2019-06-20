package trianglz.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ${Aly} on 6/10/2019.
 */
@SuppressLint("AppCompatCustomView")
public class LatoBoldEditText extends EditText {
    private Context context;

    public LatoBoldEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView(context);
    }
    private void createView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/lato_bold.ttf");
        setTypeface(tf);
    }
}
