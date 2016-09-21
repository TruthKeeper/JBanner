package com.tk.jbanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tk.jbanner.utils.JUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TK on 2016/9/20.
 */

public class JBanner extends RelativeLayout {
    //默认最大限制页数
    public static final int MAX_PAGER = 8;
    //默认最小限制页数
    public static final int MIN_PAGER = 1;
    //播放速度,fast to slow
    public static final int[] PLAY_SPEED = new int[]{
            3_000,
            5_000,
            10_000
    };
    //翻页速度,fast to slow
    public static final int[] SWITCH_SPEED = new int[]{
            500,
            1_000,
            1500
    };
    //实际播放速度
    private int mPlaySpeed = PLAY_SPEED[1];
    //实际切换速度
    private int mSwitchSpeed = SWITCH_SPEED[1];
    //实际页数限制
    private int mPageLimit = MAX_PAGER;
    //内部ViewPager
    private ViewPager mViewPager;
    //指示器
    private Indicator mIndicator;
    //adapter包装类
    private JAdapter jAdapter;
    //轮播bean list
    private List<JBannerBean> mList = new ArrayList<JBannerBean>();
    //是否自动轮播，默认开启
    private boolean mAutoScroll = true;
    //当前索引位置
    private int mCurrentPositon = MAX_PAGER;
    //点击监听回调
    private OnItemClickListener mOnItemClickListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mAutoScroll) {
                mViewPager.setCurrentItem(++mCurrentPositon);
                mHandler.sendEmptyMessageDelayed(0, mPlaySpeed);
            }
        }
    };

    public JBanner(Context context) {
        this(context, null);
    }

    public JBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                if (mIndicator != null) {
                    mIndicator.onScroll(JUtils.findRealPosition(mList.size(), position), positionOffset);
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
            mHandler.sendEmptyMessageDelayed(0, mPlaySpeed);
        }
    }

    /**
     * 初始化基本属性
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        String str = "";
        boolean hasValue = false;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.JBanner);
            mPageLimit = typedArray.getInteger(R.styleable.JBanner_pagerLimit, MAX_PAGER);
            mAutoScroll = typedArray.getBoolean(R.styleable.JBanner_autoPlay, true);
            mPlaySpeed = PLAY_SPEED[typedArray.getInt(R.styleable.JBanner_playSpeed, 1)];
            mSwitchSpeed = SWITCH_SPEED[typedArray.getInt(R.styleable.JBanner_switchSpeed, 1)];
            hasValue = typedArray.hasValue(R.styleable.JBanner_transformer);
            str = typedArray.getString(R.styleable.JBanner_transformer);
            typedArray.recycle();
        }
//        ViewPager.PageTransformer transformer = JUtils.parseTransformer(
//                getContext(),
//                hasValue ? str
//                        //默认模式
//                        : getContext().getString(R.string.page_transformer_fadeIn));
//        if (transformer != null) {
//            mViewPager.setPageTransformer(true, transformer);
//        }
        JScroller jScroller = null;
        try {
            Field mField;
            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            jScroller = new JScroller(getContext());
            jScroller.setmDuring(mSwitchSpeed);
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
    public void setJBannerList(List<JBannerBean> mList) {
        if (mList.size() < 1) {
            return;
        }
        this.mList.clear();
        if (mList.size() > mPageLimit) {
            this.mList.addAll(mList.subList(0, mPageLimit));
        } else {
            this.mList.addAll(mList);
        }
        jAdapter = new JAdapter(getContext(), mList);
        jAdapter.setmOnItemClickListener(this.mOnItemClickListener);
        mViewPager.setAdapter(jAdapter);
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
            mHandler.sendEmptyMessageDelayed(0, mPlaySpeed);
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


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 设置指示器
     *
     * @param mIndicator
     */
    public void setIndicator(Indicator mIndicator) {
        this.mIndicator = mIndicator;
    }

    /**
     * 设置点击监听
     *
     * @param mOnItemClickListener
     */
    public void setOmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }


}
