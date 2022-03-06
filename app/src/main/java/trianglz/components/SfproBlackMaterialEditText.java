package trianglz.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * This file is spawned by Gemy on 10/30/2018.
 */
public class SfproBlackMaterialEditText extends MaterialEditText {
    private Context context;
    public SfproBlackMaterialEditText(Context context, AttributeSet attrs) {
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
