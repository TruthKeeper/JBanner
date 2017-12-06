package com.tk.jbanner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.reflect.Field;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/4
 *      desc :
 * </pre>
 */

public class JViewPager extends ViewPager {
    private boolean mDisableScroll;

    public JViewPager(Context context) {
        this(context, null);
    }

    public JViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void initJScroller(int fadeTime) {
        JScroller jScroller = null;
        try {
            Field mField;
            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            jScroller = new JScroller(getContext());
            jScroller.setDuring(fadeTime);
            mField.set(this, jScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDisableTouch(boolean disableScroll) {
        mDisableScroll = disableScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !mDisableScroll && super.onInterceptTouchEvent(ev);
    }
}
