package com.example.administrator.explosionlayout.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.example.administrator.explosionlayout.utils.ScreenUtils;
import com.example.administrator.explosionlayout.widget.ExplosionLayout;


/**
 * 点击特效管理类
 * Created by c on 2018/1/4.
 */

public class ClickEffectsManager {
    private static final int CLICK_INTERVAL = 2000;
    private static final int DELAY_DISMISS = 1200;
    private static final int WHAT_TIME_COUNT = 10;
    private ExplosionLayout explosionLayout;
    private long lastTimeMillis = 0;
    /**
     * 连击数
     */
    private int clickCount;
    private Handler handler;
    private int[] windowLocations = new int[2];
    private boolean located = false;

    private ClickEffectsManager(ExplosionLayout explosionLayout) {
        this.explosionLayout = explosionLayout;
        initHandler();
    }

    private void initHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                explosionLayout.reset();
            }
        };
    }

    public static ClickEffectsManager newInstance(ExplosionLayout explosionLayout) {
        return new ClickEffectsManager(explosionLayout);
    }

    public boolean onClickEvent(View view) {
        long currentTimeMillis = System.currentTimeMillis();
        boolean consumed = true;
        if (lastTimeMillis <= 0l) {
            //第一次点击
            clickConsumed();
            lastTimeMillis = currentTimeMillis;
        } else if (currentTimeMillis - lastTimeMillis <= CLICK_INTERVAL) {
            clickConsumed();
            lastTimeMillis = currentTimeMillis;
        } else {
            consumed = false;
            clickCanceled();
        }

        return consumed;
    }

    private void clickCanceled() {
        lastTimeMillis = 0l;
        clickCount = 0;
    }

    private void clickConsumed() {
        clickCount++;
        explosionLayout.explode();
        explosionLayout.showDescription(clickCount);

        if (handler.hasMessages(WHAT_TIME_COUNT)) {
            handler.removeMessages(WHAT_TIME_COUNT);
        }
        handler.sendEmptyMessageDelayed(WHAT_TIME_COUNT, DELAY_DISMISS);
    }

    private void setExplosionHeight(int height) {
        explosionLayout.setHeight(height);
    }

    /**
     * 爆炸区域可能需要重新布局
     *
     * @param view
     */
    public void reLayout(View view) {
        if (!located) {
            view.getLocationInWindow(windowLocations);
            int height = windowLocations[1] + view.getPaddingTop() + ScreenUtils.dp2px(view.getContext(), 40);
//            LogUtils.w(TAG, "高度：" + height + ", " + windowLocations[0] + "*" + windowLocations[1]);
            setExplosionHeight(height);
            located = true;
        }
    }
}
