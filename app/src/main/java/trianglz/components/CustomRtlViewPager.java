package trianglz.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.duolingo.open.rtlviewpager.RtlViewPager;

/**
 * Created by ${Aly} on 11/19/2018.
 */
public class CustomRtlViewPager extends RtlViewPager {

    private boolean isPagingEnabled = false;

    public CustomRtlViewPager(Context context) {
        super(context);
    }

    public CustomRtlViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

}