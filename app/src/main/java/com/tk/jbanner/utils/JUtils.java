package com.tk.jbanner.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

/**
 * Created by TK on 2016/9/21.
 */

public final class JUtils {
    /**
     * 得到list集合中的的真实position
     *
     * @param listSize
     * @param position
     * @return
     */
    public static final int findRealPosition(int listSize, int position) {
        int realPosition = 0;
        if (listSize != 0) {
            realPosition = (position - 1) % listSize;
            if (realPosition < 0)
                realPosition += listSize;
            return realPosition;
        }
        return realPosition;
    }

    /**
     * 反射生成transformer
     *
     * @param context
     * @param value
     * @return
     */
    public static ViewPager.PageTransformer parseTransformer(Context context, String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        final String fullName;
        if (value.equals("null")) {
            //不需要加载
            return null;
        } else if (value.startsWith(".")) {
            //当前包目录下
            fullName = context.getPackageName() + value;
        } else if (value.indexOf('.') >= 0) {
            //全路径
            fullName = value;
        } else {
            // 根目录下
            fullName = context.getPackageName() + '.' + value;
        }
        try {
            final Class<ViewPager.PageTransformer> clazz = (Class<ViewPager.PageTransformer>) Class.forName(
                    fullName, true, context.getClassLoader());
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("could not find transformer by " + fullName, e);
        }
    }
}
