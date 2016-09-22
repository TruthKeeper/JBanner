package com.tk.jbanner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tk.jbanner.utils.JUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TK on 2016/9/20.
 * 无限循环的JAdapter for ViewPager
 */

public class JAdapter extends PagerAdapter {
    private Context mContext;
    private List<? extends Object> mList = new ArrayList<>();
    private LinkedList<ImageView> mCacheList = new LinkedList<ImageView>();
    private JBanner.OnJBannerListener mOnJBannerListener;

    public JAdapter(Context mContext, List<? extends Object> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size() == 0 ? 0 : mList.size() + 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageview = null;
        if (mCacheList.size() == 0) {
            imageview = new ImageView(mContext);
            imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
//            //从缓存集合中取
            imageview = mCacheList.removeFirst();
        }
        final int realPosition = JUtils.findRealPosition(mList.size(), position);
        if (mOnJBannerListener != null) {
            mOnJBannerListener.onLoad(imageview, realPosition);
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnJBannerListener.onClick(realPosition);
                }
            });
        }
        container.addView(imageview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return imageview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mCacheList.add((ImageView) object);
    }

    public void setmOnJBannerListener(JBanner.OnJBannerListener mOnJBannerListener) {
        this.mOnJBannerListener = mOnJBannerListener;
    }
}
