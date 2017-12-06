package com.tk.jbanner;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseIntArray;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/4
 *      desc : 定时切换器
 * </pre>
 */

class JTimer extends Handler {

    interface JTimerListener {
        void onTimer();
    }

    private JBanner jBanner;
    private JTimerListener mJTimerListener;
    private SparseIntArray mCustomInterval;
    private long mIntervalTime;
    private boolean isStopped = true;

    static final int MSG_INTERVAL_WHAT = 0x01;

    JTimer(JBanner jBanner, JTimerListener jTimerListener, long intervalTime) {
        super(Looper.getMainLooper());
        this.jBanner = jBanner;
        this.mJTimerListener = jTimerListener;
        this.mIntervalTime = intervalTime;
    }

    void setCustomInterval(SparseIntArray customInterval) {
        this.mCustomInterval = customInterval;
    }

    boolean isStopped() {
        return isStopped;
    }

    void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    @Override
    public void handleMessage(Message msg) {
        if (MSG_INTERVAL_WHAT == msg.what) {
            //获取JBanner的下一页，延迟发送
            int nextIndex = jBanner.getNextIndex();
            Log.e("nextIndex",nextIndex+"");
            mJTimerListener.onTimer();
            startTimerBy(false, nextIndex);
        }
    }

    void startTimerBy(boolean immediately, int index) {
        removeMessages(MSG_INTERVAL_WHAT);
        if (immediately) {
            sendEmptyMessage(MSG_INTERVAL_WHAT);
        } else {
            sendEmptyMessageDelayed(MSG_INTERVAL_WHAT, getNextInterval(index));
        }
    }

    /**
     * 获取间隔时长
     *
     * @param index
     * @return
     */
    private long getNextInterval(int index) {
        if (mCustomInterval != null) {
            int interval = mCustomInterval.get(index, -1);
            if (interval > 0) {
                return interval;
            }
        }
        return mIntervalTime;
    }
}
