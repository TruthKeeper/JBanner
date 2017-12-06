package com.tk.sample.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by TK on 2016/9/21.
 * 折叠式
 */
public class FoldTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        if (position < -1 || position > 1) {
            return;
        }
        if (position < 0) {
            //从右向左滑动为当前View
            page.setPivotX(page.getMeasuredWidth());
            page.setPivotY(page.getMeasuredHeight() >> 1);
            page.setScaleX(1 + position);
            page.setTranslationX(0f);
        } else if (position < 1) {
            //从左向右滑动为当前View
            page.setPivotX(0f);
            page.setPivotY(page.getMeasuredHeight() >> 1);
            page.setScaleX(1 - position);
        }
    }

}
