package com.example.administrator.explosionlayout.widget;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 单击和双击监听
 *
 * Created by lzw on 2018/1/6.
 */

public class DoubleClickDetector implements View.OnTouchListener {

    private static final String TAG = "DoubleClickDetector";

    private GestureDetector mGestureDetector;
    private DoubleClickCallback doubleClickCallback;
    private DoubleClickTapEventCallback doubleClickDownCallback;
    private SingleClickCallback singleClickCallback;
    private Context context;
    private View view;

    private DoubleClickDetector(Context context) {
        this.context = context;
    }

    public static DoubleClickDetector newInstance(Context context) {
        return new DoubleClickDetector(context);
    }

    public DoubleClickDetector addView(View view) {
        this.view = view;
        this.view.setOnTouchListener(this);
        this.view.setFocusable(true);
        this.view.setClickable(true);
        this.view.setLongClickable(true);
        mGestureDetector = new GestureDetector(context, new GestureListener(this.view));
        return this;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public DoubleClickDetector addDoubleClickListener(DoubleClickCallback doubleClickCallback) {
        if (this.view == null) {
            return null;
        }
        this.doubleClickCallback = doubleClickCallback;
        return this;
    }

    public DoubleClickDetector addDoubleClickDownListener(DoubleClickTapEventCallback doubleClickCallback) {
        if (this.view == null) {
            return null;
        }
        this.doubleClickDownCallback = doubleClickCallback;
        return this;
    }

    public DoubleClickDetector addSingleClickListener(SingleClickCallback singleClickCallback) {
        if (view == null) {
            return null;
        }
        this.singleClickCallback = singleClickCallback;
        return this;
    }

    public interface SingleClickCallback {
        void onSingleClick(View view);
    }

    public interface DoubleClickCallback {
        void onDoubleClick(View view, float x, float y);
    }

    public interface DoubleClickTapEventCallback {
        void onDoubleTapEventClick(View view, float x, float y);
    }

    private void onSingleClicked(View view) {
        if (singleClickCallback != null) {
            singleClickCallback.onSingleClick(view);
        }
    }

    private void onDoubleClicked(View view, float x, float y) {
        if (doubleClickCallback != null) {
            doubleClickCallback.onDoubleClick(view, x, y);
        }
    }

    private void onDoubleTapUpClicked(View view, float x, float y) {
        if (doubleClickDownCallback != null) {
            doubleClickDownCallback.onDoubleTapEventClick(view, x, y);
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private View view;

        public GestureListener(View view) {
            this.view = view;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.w(TAG, "onSingleTapConfirmed" + e.getAction());
            onSingleClicked(this.view);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.w(TAG, "onDoubleTap" + e.getAction() + e.getDownTime());
            onDoubleClicked(this.view, e.getX(), e.getY());
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.w(TAG, "onDoubleTapEvent" + e.getAction());
            if (e.getAction() == MotionEvent.ACTION_UP && doubleClickDownCallback != null) {
                onDoubleTapUpClicked(this.view, e.getX(), e.getY());
            }
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.w(TAG, "onSingleTapUp" + e.getAction());
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.w(TAG, "onDown" + e.getAction());
            return super.onDown(e);
        }
    }
}
