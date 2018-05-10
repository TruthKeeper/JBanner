package com.tk.jbanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/4
 *      desc : Banner
 *    ViewPager Index      0 1 2 3 4 5 6
 *    ViewPager RealIndex  5 1 2 3 4 5 1
 *    RealIndex            4 0 1 2 3 4 0
 *    NextIndex            0 1 2 3 4 0 1
 *
 * </pre>
 */
public class JBanner extends FrameLayout implements JTimer.JTimerListener {
    /**
     * 最大限制页数
     */
    public static final int MAX_PAGER = 10;
    /**
     * 最小限制页数
     */
    public static final int MIN_PAGER = 1;
    /**
     * 默认轮播速度 5_000ms 切换分页
     */
    public static final int DEFAULT_INTERVAL_TIME = 5_000;
    /**
     * 默认切换速度 600ms 页面切换过渡
     */
    public static final int DEFAULT_FADE_TIME = 600;
    /**
     * 实际播放速度
     */
    private int mIntervalTime = DEFAULT_INTERVAL_TIME;
    /**
     * 实际切换速度
     */
    private int mFadeTime = DEFAULT_FADE_TIME;
    /**
     * 实际页数限制
     */
    private int mPageLimit = MAX_PAGER;
    /**
     * 内部ViewPager
     */
    private JViewPager mViewPager;
    /**
     * 指示器
     */
    private List<Indicator> mIndicatorList = new LinkedList<>();
    /**
     * adapter包装类
     */
    private JAdapter jAdapter;
    /**
     * 数据实体
     */
    private List<Object> mList = new ArrayList<>();
    /**
     * 是否自动轮播
     */
    private boolean mAutoPlay = true;
    /**
     * 是否禁止触摸滑动
     */
    private boolean mDisableScroll = true;
    /**
     * 定时器
     */
    private JTimer jTimer;
    /**
     * 点击监听回调
     */
    private OnJBannerListener onJBannerListener;


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
        initAttrs(attrs);

        mViewPager = new JViewPager(getContext());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mIndicatorList != null) {
                    int realSize = mViewPager.getAdapter().getCount() - 2;
                    int viewPagerIndex = position;
                    if (position == realSize + 1) {
                        viewPagerIndex = 1;
                    } else if (position == 0) {
                        viewPagerIndex = realSize;
                    }
                    for (Indicator i : mIndicatorList) {
                        if (i != null) {
                            i.onScroll(viewPagerIndex - 1, positionOffset);
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int current = mViewPager.getCurrentItem();
                    int realViewPagerIndex = getRealViewPagerIndex(mViewPager);
                    if (realViewPagerIndex != current) {
                        //偷偷摸摸滚动
                        mViewPager.setCurrentItem(realViewPagerIndex, false);
                    }
                }
            }
        });
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mViewPager, p);

        jTimer = new JTimer(this, this, mIntervalTime);
        mViewPager.initJScroller(mFadeTime);
        setDisableTouch(mDisableScroll);
    }

    /**
     * 初始化基本属性
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.JBanner);
            mPageLimit = typedArray.getInteger(R.styleable.JBanner_pagerLimit, MAX_PAGER);
            if (mPageLimit < MIN_PAGER
                    || mPageLimit > MAX_PAGER) {
                throw new IllegalArgumentException("page limit size not standard");
            }
            mAutoPlay = typedArray.getBoolean(R.styleable.JBanner_autoPlay, true);
            mIntervalTime = typedArray.getInt(R.styleable.JBanner_intervalTime, DEFAULT_INTERVAL_TIME);
            mFadeTime = typedArray.getInt(R.styleable.JBanner_fadeTime, DEFAULT_FADE_TIME);
            mDisableScroll = typedArray.getBoolean(R.styleable.JBanner_disableScroll, false);
            typedArray.recycle();
        }
    }

    /**
     * 设置JBanner list集合超出limit部分自动截取
     *
     * @param data
     */
    public void initData(@Nullable List<?> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        this.mList.clear();
        if (data.size() > mPageLimit) {
            this.mList.addAll(data.subList(0, mPageLimit));
        } else {
            this.mList.addAll(data);
        }
        jAdapter = new JAdapter(getContext(), mList);
        jAdapter.setOnJBannerListener(onJBannerListener);
        mViewPager.setAdapter(jAdapter);
        mViewPager.setOffscreenPageLimit(Math.min(jAdapter.getCount(), 5));
        mViewPager.setCurrentItem(1, false);
        //设置数据源后开始滚动
        stopTimer();
        if (mAutoPlay) {
            startTimer(false);
        }
    }

    /**
     * 设置页面切换器
     *
     * @param transformer
     */
    public void setTransformer(ViewPager.PageTransformer transformer) {
        if (mViewPager != null) {
            mViewPager.setPageTransformer(true, transformer);
        }
    }

    /**
     * 获取数据集合
     *
     * @return
     */
    @Nullable
    public List<Object> getListData() {
        return mList;
    }

    /**
     * 开始自动轮播
     *
     * @param immediately 立即
     */
    public void startAutoPlay(boolean immediately) {
        startAutoPlay(immediately, null);
    }

    /**
     * 开始自动轮播
     *
     * @param immediately        立即
     * @param customIntervalTime 自定义的切换页面间隔
     */
    public void startAutoPlay(boolean immediately, @Nullable SparseIntArray customIntervalTime) {
        jTimer.setCustomInterval(customIntervalTime);
        if (mAutoPlay) {
            return;
        }
        this.mAutoPlay = true;
        startTimer(immediately);
    }

    /**
     * 停止轮播
     */
    public void stopAutoPlay() {
        if (!mAutoPlay) {
            return;
        }
        this.mAutoPlay = false;
        stopTimer();
    }

    private void startTimer(boolean immediately) {
        if (jTimer != null && jTimer.isStopped()) {
            jTimer.startTimerBy(immediately, getRealIndex(mViewPager));
            jTimer.setStopped(false);
        }
    }

    private void stopTimer() {
        if (jTimer != null && !jTimer.isStopped()) {
            jTimer.removeCallbacksAndMessages(null);
            jTimer.setStopped(true);
        }
    }

    /**
     * 是否禁止触摸滑动
     *
     * @param disableScroll
     */
    public void setDisableTouch(boolean disableScroll) {
        if (mViewPager != null && mDisableScroll != disableScroll) {
            mViewPager.setDisableTouch(disableScroll);
            mDisableScroll = disableScroll;
        }
    }

    /**
     * 触摸时暂停播放
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAutoPlay) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                stopTimer();
            } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
                startTimer(false);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
        if (mIndicatorList != null) {
            mIndicatorList.clear();
        }
    }

    /**
     * 添加指示器
     *
     * @param indicator
     */
    public void addIndicator(@NonNull Indicator indicator) {
        mIndicatorList.add(indicator);
    }

    /**
     * 清除某个指示器
     *
     * @param indicator
     * @return
     */
    public boolean removeIndicator(@NonNull Indicator indicator) {
        return mIndicatorList.remove(indicator);
    }

    /**
     * 清除所有指示器
     *
     * @return
     */
    public void removeAllIndicators() {
        mIndicatorList.clear();
    }

    /**
     * 获取ViewPager当前的正确索引
     *
     * @param viewPager
     * @return
     */
    private static int getRealViewPagerIndex(ViewPager viewPager) {
        if (viewPager.getAdapter() == null) {
            return -1;
        }
        int current = viewPager.getCurrentItem();
        int realSize = viewPager.getAdapter().getCount() - 2;
        if (current == realSize + 1) {
            return 1;
        } else if (current == 0) {
            return realSize;
        }
        return current;
    }

    /**
     * 获取ViewPager当前的数据索引
     *
     * @param viewPager
     * @return
     */
    private static int getRealIndex(ViewPager viewPager) {
        return getRealViewPagerIndex(viewPager) - 1;
    }

    /**
     * 获取ViewPager当前的数据索引
     *
     * @param listDataSize
     * @param viewPagerIndex
     * @return
     */
    static int getRealIndex(int listDataSize, int viewPagerIndex) {
        if (viewPagerIndex == listDataSize + 1) {
            return 0;
        } else if (viewPagerIndex == 0) {
            return listDataSize - 1;
        }
        return viewPagerIndex - 1;
    }

    /**
     * 获取ViewPager的下一个数据索引
     *
     * @return
     */
    public int getNextIndex() {

        if (mViewPager.getAdapter() == null) {
            return -1;
        }
        int current = mViewPager.getCurrentItem();
        int realSize = mViewPager.getAdapter().getCount() - 2;
        return current % realSize;
    }

    /**
     * 设置JBanner监听
     *
     * @param onJBannerListener
     */
    public void setOnJBannerListener(OnJBannerListener onJBannerListener) {
        this.onJBannerListener = onJBannerListener;
        if (jAdapter != null) {
            jAdapter.setOnJBannerListener(this.onJBannerListener);
        }
    }

    @Override
    public void onTimer() {
        if (mViewPager.getAdapter() != null) {
            int viewPagerIndex = mViewPager.getCurrentItem();
            viewPagerIndex++;
            if (viewPagerIndex > mViewPager.getAdapter().getCount() - 1) {
                viewPagerIndex = 0;
            }
            mViewPager.setCurrentItem(viewPagerIndex, true);
        }
    }

    public interface OnJBannerListener {
        void onClick(int position);

        void onLoad(ImageView imageView, int position);
    }


}
