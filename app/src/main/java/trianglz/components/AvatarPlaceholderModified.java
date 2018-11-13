package trianglz.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import agency.tango.android.avatarview.AvatarPlaceholder;
import trianglz.utils.Util;

/**
 * Created by ${Aly} on 10/31/2018.
 */
public class  AvatarPlaceholderModified extends AvatarPlaceholder {

    public static final String DEFAULT_PLACEHOLDER_STRING = "-";
    private static final String DEFAULT_PLACEHOLDER_COLOR = "#4cc21e58";
    public static final int DEFAULT_TEXT_SIZE_PERCENTAGE = 33;

    private static final String [] COLORS = {"#4c000000"};

    private Paint textPaint;
    private Paint backgroundPaint;
    private RectF placeholderBounds;

    private String avatarText;
    private int textSizePercentage;
    private String defaultString;

    private float textStartXPoint;
    private float textStartYPoint;

    public AvatarPlaceholderModified(String name) {
        this(name, DEFAULT_TEXT_SIZE_PERCENTAGE, DEFAULT_PLACEHOLDER_STRING);
    }

    public AvatarPlaceholderModified(String name, String placeholderColor) {
        this(name, DEFAULT_TEXT_SIZE_PERCENTAGE, DEFAULT_PLACEHOLDER_STRING);
        backgroundPaint.setColor(Color.parseColor(placeholderColor));
    }

    public AvatarPlaceholderModified(String name, @IntRange int textSizePercentage, @NonNull String defaultString) {

        super(name, textSizePercentage, defaultString);

        this.defaultString = resolveStringWhenNoName(defaultString);
        this.avatarText = convertNameToAvatarText(name);
        this.textSizePercentage = textSizePercentage;

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#FFFFFF"));
        textPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.BOLD));

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.clearShadowLayer();
        backgroundPaint.setColor(Color.parseColor("#28bb4e"));
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (placeholderBounds == null) {
            placeholderBounds = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
            setAvatarTextValues();
        }

        canvas.drawRect(placeholderBounds, backgroundPaint);
        canvas.drawText(avatarText, textStartXPoint, textStartYPoint, textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
        backgroundPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        textPaint.setColorFilter(colorFilter);
        backgroundPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private void setAvatarTextValues() {
        textPaint.setTextSize(calculateTextSize());
        textStartXPoint = calculateTextStartXPoint();
        textStartYPoint = calculateTextStartYPoint();
    }

    private float calculateTextStartXPoint() {
        float stringWidth = textPaint.measureText(avatarText);
        return (getBounds().width() / 2f) - (stringWidth / 2f);
    }

    private float calculateTextStartYPoint() {
        return (getBounds().height() / 2f) - ((textPaint.ascent() + textPaint.descent()) / 2f);
    }

    private String resolveStringWhenNoName(String stringWhenNoName) {
        return Util.isNotNullOrEmpty(stringWhenNoName) ? stringWhenNoName : DEFAULT_PLACEHOLDER_STRING;
    }

    private String convertNameToAvatarText(String name) {

        name = name.trim();

        if(Util.isNotNullOrEmpty(name)){

            if(name.contains(" ")){
                String [] nameArray = name.split(" ");
                return nameArray[0].substring(0,1).toUpperCase() +
                        nameArray[1].substring(0,1).toUpperCase();
            }else{
                return name.substring(0,2).toUpperCase();
            }
        }

        return defaultString;
    }

    private String convertStringToColor(String text) {
        return Util.isNullOrEmpty(text) ? DEFAULT_PLACEHOLDER_COLOR : COLORS[text.charAt(0) % COLORS.length];
    }


    private float calculateTextSize() {
        if (textSizePercentage < 0 || textSizePercentage > 100) {
            textSizePercentage = DEFAULT_TEXT_SIZE_PERCENTAGE;
        }
        return getBounds().height() * (float) textSizePercentage / 100;
    }

}
