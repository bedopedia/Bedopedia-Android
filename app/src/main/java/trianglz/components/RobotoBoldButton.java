package trianglz.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Farah A. Moniem on 22/07/2019.
 */

@SuppressLint("AppCompatCustomView")
public class RobotoBoldButton extends Button {
    private Context context;

    public RobotoBoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        CreateView(context);
    }

    private void CreateView(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/Roboto-Bold.ttf");
        setTypeface(tf);
    }
}