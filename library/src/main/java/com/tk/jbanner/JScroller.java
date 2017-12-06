package com.tk.jbanner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/4
 *      desc : 自定义滑动时间
 * </pre>
 */
public class JScroller extends Scroller {
    private int mDuring = JBanner.DEFAULT_FADE_TIME;
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

    public int getDuring() {
        return mDuring;
    }

    public void setDuring(int during) {
        this.mDuring = during;
    }
}