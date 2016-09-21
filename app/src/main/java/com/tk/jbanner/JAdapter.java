package com.tk.jbanner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tk.jbanner.utils.JUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TK on 2016/9/20.
 * 无限循环的JAdapter for JViewPager
 */

public class JAdapter extends PagerAdapter {
    private Context mContext;
    private List<JBannerBean> mList = new ArrayList<JBannerBean>();
    private LinkedList<ImageView> mCacheList = new LinkedList<ImageView>();
    private JBanner.OnItemClickListener mOnItemClickListener;

    public JAdapter(Context mContext, List<JBannerBean> mList) {
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
        Glide.with(mContext)
                .load(mList.get(realPosition).getImgUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageview);
        container.addView(imageview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mOnItemClickListener != null) {
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(realPosition);
                }
            });
        }
        return imageview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mCacheList.add((ImageView) object);
    }


    public void setmOnItemClickListener(JBanner.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
