package com.tk.jbanner.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by TK on 2016/5/13.
 * 3d盒子翻转
 */
public class ThreeDRotatePageTransformer implements ViewPager.PageTransformer {
    private static final float MAX = 90f;

    @Override
    public void transformPage(View view, float position) {
//        if (position <= 0) {
//            //从右向左滑动为当前View
//
//            //设置旋转中心点；
//            ViewHelper.setPivotX(view, view.getMeasuredWidth());
//            ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
//
//            //只在Y轴做旋转操作
//            ViewHelper.setRotationY(view, MAX * position);
//        } else if (position <= 1) {
//            //从左向右滑动为当前View
//            ViewHelper.setPivotX(view, 0);
//            ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
//            ViewHelper.setRotationY(view, MAX * position);
//        }
    }
}
