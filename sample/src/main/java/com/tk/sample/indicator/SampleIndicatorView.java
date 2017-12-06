package com.tk.sample.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tk.jbanner.Indicator;

/**
 * Created by TK on 2016/9/21.
 */

public class SampleIndicatorView extends View implements Indicator {
    public static final int PADDING = 30;
    public static final int RADIUS = 12;
    private int size;
    private Paint paint = new Paint();
    private boolean initSuccess;
    private int leftDistance;
    private int topDistance;
    private int current;

    public SampleIndicatorView(Context context) {
        super(context);
        initBase();
    }

    public SampleIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBase();
        setBackgroundColor(0x50FFFFFF);
    }

    private void initBase() {
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    /**
     * 更新ui
     *
     * @param size
     */
    public void refreshUI(int size) {
        this.size = size;
        this.initSuccess = true;
        this.leftDistance = (getWidth() - size * RADIUS - (size - 1) * PADDING) >> 1;
        this.topDistance = (getHeight() - (RADIUS << 1)) >> 1;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (int i = 0; i < size; i++) {
            if (i == current) {
                paint.setColor(0xFFFF6262);
            } else {
                paint.setColor(0xFFCECECE);
            }
            canvas.drawCircle(this.leftDistance + ((RADIUS << 1) + PADDING) * i + RADIUS,
                    topDistance + RADIUS,
                    RADIUS,
                    paint);
        }
    }


    @Override
    public void onScroll(int position, float offsetX) {
        if (!initSuccess) {
            return;
        }
        current = position;
        invalidate();
    }
}
