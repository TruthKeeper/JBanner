package com.tk.jbanner.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by TK on 2016/9/21
 * 显式.
 */
public class FadeInTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1 || position > 1) {
            return;
        }
        if (position < 0) {
            page.setAlpha(1f);
            page.setTranslationX(0f);
            page.setScaleX(1f);
            page.setScaleY(1f);
            return;
        } else if (position < 1) {
            page.setAlpha(1 - position);
            page.setTranslationX(page.getMeasuredWidth() * -position);
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        }
    }

}
