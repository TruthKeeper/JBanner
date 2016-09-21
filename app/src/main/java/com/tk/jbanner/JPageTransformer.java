package com.tk.jbanner;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by TK on 2016/9/21.
 */

public abstract class JPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
//        transformer(page, JUtils.findRealPosition(position));
    }

    abstract void transformer(View page, float position);
}
