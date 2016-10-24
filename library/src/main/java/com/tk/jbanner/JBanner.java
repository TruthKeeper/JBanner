package com.tk.jbanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tk.jbanner.utils.JUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TK on 2016/9/20.
 */

public class JBanner extends RelativeLayout {
    //最大限制页数
    public static final int MAX_PAGER = 10;
    //最小限制页数
    public static final int MIN_PAGER = 1;
    //默认轮播速度
    public static final int DEFAULT_PLAY_TIME = 5_000;
    //默认切换速度
    public static final int DEFAULT_SWITCH_TIME = 1_000;
    //实际播放速度
    private int mPlayTime = DEFAULT_PLAY_TIME;
    //实际切换速度
    private int mSwitchTime = DEFAULT_SWITCH_TIME;
    //实际页数限制
    private int mPageLimit = MAX_PAGER;
    //内部ViewPager
    private ViewPager mViewPager;
    //指示器
    private List<Indicator> mIndicatorList;
    //pagetransformer，动态切换view属性不一定能重置，会有bug
    private ViewPager.PageTransformer mTransformer;
    //adapter包装类
    private JAdapter jAdapter;
    //轮播bean list
    private List<Object> mList = new ArrayList<>();
    //是否自动轮播，默认开启
    private boolean mAutoScroll = true;
    //当前索引位置
    private int mCurrentPositon;
    //触摸锁
    private boolean touchLock;
    //点击监听回调
    private OnJBannerListener mOnJBannerListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!touchLock && mAutoScroll) {
                mViewPager.setCurrentItem(++mCurrentPositon);
                mHandler.sendEmptyMessageDelayed(0, mPlayTime);
            }
        }
    };

    public JBanner(Context context) {
        this(context, null);
    }

    public JBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        mViewPager = new ViewPager(getContext());
        mViewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        initAttrs(attrs);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mIndicatorList != null) {
                    for (Indicator i : mIndicatorList) {
                        if (i != null) {
                            i.onScroll(JUtils.findRealPosition(mList.size(), position), positionOffset);
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPositon = position % (mList.size() + 2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int current = mViewPager.getCurrentItem();
                    int realSize = mViewPager.getAdapter().getCount() - 2;
                    if (current == realSize + 1) {
                        //偷偷摸摸滚动到真实列表的头部
                        mViewPager.setCurrentItem(1, false);
                    } else if (current == 0) {
                        //偷偷摸摸滚动到真实列表的末尾
                        mViewPager.setCurrentItem(realSize, false);
                    }
                }
            }
        });
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mViewPager, p);
        mViewPager.setCurrentItem(1, false);
        if (mAutoScroll) {
            mHandler.sendEmptyMessageDelayed(0, mPlayTime);
        }
    }

    /**
     * 初始化基本属性
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        String str = "";
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.JBanner);
            mPageLimit = typedArray.getInteger(R.styleable.JBanner_pagerLimit, MAX_PAGER);
            if (mPageLimit < MIN_PAGER
                    || mPageLimit > MAX_PAGER) {
                throw new IllegalArgumentException("page limit size not standard");
            }
            mAutoScroll = typedArray.getBoolean(R.styleable.JBanner_autoPlay, true);
            mPlayTime = typedArray.getInt(R.styleable.JBanner_playTime, DEFAULT_PLAY_TIME);
            mSwitchTime = typedArray.getInt(R.styleable.JBanner_switchTime, DEFAULT_SWITCH_TIME);
            str = typedArray.getString(R.styleable.JBanner_transformer);
            typedArray.recycle();
        }
        mTransformer = JUtils.parseTransformer(
                getContext(),
                TextUtils.isEmpty(str) ?
                        //默认模式
                        getContext().getString(R.string.page_transformer_fadeIn)
                        : str);
        if (mTransformer != null) {
            mViewPager.setPageTransformer(true, mTransformer);
        }
        initJScroller();
    }

    private void initJScroller() {
        JScroller jScroller = null;
        try {
            Field mField;
            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            jScroller = new JScroller(getContext());
            jScroller.setmDuring(mSwitchTime);
            mField.set(mViewPager, jScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置JBanner list集合超出limit部分自动截取
     *
     * @param mList
     */
    public void setJBannerList(List<? extends Object> mList) {
        if (mList == null
                || mList.size() < 1) {
            return;
        }
        this.mList.clear();
        if (mList.size() > mPageLimit) {
            this.mList.addAll(mList.subList(0, mPageLimit));
        } else {
            this.mList.addAll(mList);
        }
        jAdapter = new JAdapter(getContext(), mList);
        jAdapter.setmOnJBannerListener(this.mOnJBannerListener);
        mViewPager.setAdapter(jAdapter);
        mViewPager.setOffscreenPageLimit(jAdapter.getCount());
        mCurrentPositon = 1;
        mViewPager.setCurrentItem(mCurrentPositon, false);
    }

    /**
     * 开始自动轮播
     *
     * @param immediately 立即
     */
    public void startAutoPlay(boolean immediately) {
        if (this.mAutoScroll) {
            return;
        }
        this.mAutoScroll = true;
        if (immediately) {
            mHandler.sendEmptyMessage(0);
        } else {
            mHandler.sendEmptyMessageDelayed(0, mPlayTime);
        }

    }

    /**
     * 停止轮播
     */
    public void stopAutoPlay() {
        if (!this.mAutoScroll) {
            return;
        }
        this.mAutoScroll = false;
        this.mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 触摸时暂停播放
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAutoScroll) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN && !touchLock) {
                touchLock = true;
                mHandler.removeCallbacksAndMessages(null);
            } else if (ev.getAction() == MotionEvent.ACTION_UP && touchLock) {
                touchLock = false;
                mHandler.sendEmptyMessageDelayed(0, mPlayTime);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
        if (mIndicatorList != null) {
            mIndicatorList.clear();
        }
    }

    /**
     * 添加指示器
     *
     * @param indicator
     */
    public void addIndicator(Indicator indicator) {
        if (mIndicatorList == null) {
            mIndicatorList = new ArrayList<Indicator>();
        }
        mIndicatorList.add(indicator);
    }

    /**
     * 清除某个指示器
     *
     * @param indicator
     * @return
     */
    public boolean clearIndicator(Indicator indicator) {
        if (mIndicatorList != null) {
            return mIndicatorList.remove(indicator);
        }
        return false;
    }

    /**
     * 清除所有指示器
     *
     * @return
     */
    public void clearAllIndicator() {
        if (mIndicatorList != null) {
            mIndicatorList.clear();
        }
    }

    /**
     * 设置JBanner监听
     *
     * @param mOnJBannerListener
     */
    public void setmOnJBannerListener(OnJBannerListener mOnJBannerListener) {
        this.mOnJBannerListener = mOnJBannerListener;
        if (jAdapter != null) {
            jAdapter.setmOnJBannerListener(this.mOnJBannerListener);
        }
    }

    public interface OnJBannerListener {
        void onClick(int position);

        void onLoad(ImageView imageView, int position);
    }


}
