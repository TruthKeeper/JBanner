package com.tk.jbanner.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by TK on 2016/5/13.
 */
public class FadeInTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(View view, float position) {
//        Log.e("position", "" + position);
        if (position < -2 || position > 2) {
            return;
        }
        if (position < -1 || position > 1) {
            view.setAlpha(0);
            return;
        }
        if (position < 0) {
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
            return;
        }
        if (position < 1) {
            view.setAlpha(1 - position);
            view.setTranslationX(view.getMeasuredWidth() * -position);
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
    }
}
