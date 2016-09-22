package com.tk.sample.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by TK on 2016/9/21.
 * 盒子翻转
 */
public class BoxPageTransformer implements ViewPager.PageTransformer {
    private static final float MAX = 90f;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1 || position > 1) {
            return;
        }
        if (position < 0) {
            //从右向左滑动为当前View
            //设置旋转中心点；
            page.setPivotX(page.getMeasuredWidth());
            page.setPivotY(page.getMeasuredHeight() >> 1);
            //只在Y轴做旋转操作
            ButterKnife.apply(page, View.ROTATION_Y, MAX * position);
        } else if (position < 1) {
            //从左向右滑动为当前View
            //设置旋转中心点；
            page.setPivotX(0f);
            page.setPivotY(page.getMeasuredHeight() >> 1);
            //只在Y轴做旋转操作
            ButterKnife.apply(page, View.ROTATION_Y, MAX * position);
        }
    }
}
