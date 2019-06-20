package trianglz.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by ${Aly} on 6/10/2019.
 */
@SuppressLint("AppCompatCustomView")
public class LatoBoldButton extends Button {
    private Context context;
    public LatoBoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        CreateView(context);
    }

    private void CreateView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/lato_bold.ttf");
        setTypeface(tf);
    }
}
