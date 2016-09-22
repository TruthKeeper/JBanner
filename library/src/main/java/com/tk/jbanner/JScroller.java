package com.tk.jbanner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by TK on 2016/9/21.
 * 自定义滑动时间
 */

public class JScroller extends Scroller {

    private int mDuring = JBanner.DEFAULT_SWITCH_TIME;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    public JScroller(Context context) {
        this(context, null);
    }

    public JScroller(Context context, Interpolator interpolator) {
        super(context, sInterpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuring);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuring);
    }

    public int getmDuring() {
        return mDuring;
    }

    public void setmDuring(int mDuring) {
        this.mDuring = mDuring;
    }

}
