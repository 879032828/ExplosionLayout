package com.example.administrator.explosionlayout.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.explosionlayout.R;
import com.example.administrator.explosionlayout.interfaces.DoubleClickCallback;
import com.example.administrator.explosionlayout.interfaces.SingleClickCallback;

import java.util.Random;

/**
 * 单击和双击布局
 *
 * Created by lzw on 2018/1/9.
 */

public class ClickEventLayout extends RelativeLayout implements DoubleClickDetector.DoubleClickCallback,
        DoubleClickDetector.SingleClickCallback,
        DoubleClickDetector.DoubleClickTapEventCallback {

    private static String TAG = "ClickLayout";
    private Context context;
    /**
     * 图片资源
     */
    private int[] mImgResIds;
    /**
     * 用于判断单击时的前一个动作是否是双击，延迟双击和单击之间的时间间隔
     */
    private boolean isDoubleClick = false;
    /**
     * 双击和单击之间的时间间隔
     */
    private static final int clickInterval = 1000;
    /**
     * 动画持续时间
     */
    private static final int durationTime = 700;
    /**
     * 插值器
     */
    private Interpolator[] interpolators = new Interpolator[4];

    private DoubleClickCallback doubleClickCallback;
    private SingleClickCallback singleClickCallback;
    private SingleClickCallback singleClickCallbackTemp;

    public ClickEventLayout(Context context) {
        this(context, null);
    }

    public ClickEventLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickEventLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void setDoubleClickCallback(DoubleClickCallback doubleClickCallback) {
        this.doubleClickCallback = doubleClickCallback;
    }

    public void setSingleClickCallback(SingleClickCallback singleClickCallback) {
        this.singleClickCallback = singleClickCallback;
    }

    private void init() {
        initData();
        initClickLayout();
    }

    private void initData() {
        mImgResIds = new int[]{
                R.mipmap.ic_anim_model_p0,
                R.mipmap.ic_anim_model_p1,
                R.mipmap.ic_anim_model_p2,
                R.mipmap.ic_anim_model_p3,
                R.mipmap.ic_anim_model_p4,
                R.mipmap.ic_anim_model_p5,
                R.mipmap.ic_anim_model_p6,
                R.mipmap.ic_anim_model_p7,
                R.mipmap.ic_anim_model_p8,
                R.mipmap.ic_anim_model_p9,
                R.mipmap.ic_anim_model_p10,
                R.mipmap.ic_anim_model_p11,
                R.mipmap.ic_anim_model_p12,
                R.mipmap.ic_anim_model_p13,
                R.mipmap.ic_anim_model_p14,
                R.mipmap.ic_anim_model_p15,
                R.mipmap.ic_anim_model_p16,
                R.mipmap.ic_anim_model_p17,
                R.mipmap.ic_anim_model_p18,
                R.mipmap.ic_anim_model_p19,
                R.mipmap.ic_anim_model_p20,
                R.mipmap.ic_anim_model_p21,
                R.mipmap.ic_anim_model_p22,
                R.mipmap.ic_anim_model_p23,
                R.mipmap.ic_anim_model_p24,
                R.mipmap.ic_anim_model_p25,
                R.mipmap.ic_anim_model_p26,
                R.mipmap.ic_anim_model_p27,
                R.mipmap.ic_anim_model_p28,
                R.mipmap.ic_anim_model_p29,
                R.mipmap.ic_anim_model_p30,
                R.mipmap.ic_anim_model_p31,
                R.mipmap.ic_anim_model_p32,
                R.mipmap.ic_anim_model_p33,
                R.mipmap.ic_anim_model_p34
        };

        // 插值器
        interpolators[0] = new AccelerateDecelerateInterpolator(); // 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
        interpolators[1] = new AccelerateInterpolator();  // 在动画开始的地方速率改变比较慢，然后开始加速
        interpolators[2] = new DecelerateInterpolator(); // 在动画开始的地方快然后慢
        interpolators[3] = new LinearInterpolator();  // 以常量速率改变
    }

    /**
     * 初始化描述区域相关view
     */
    private void initClickLayout() {
        View view = new View(this.context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        DoubleClickDetector.newInstance(this.context).addView(view).addDoubleClickListener(this).addSingleClickListener(this).addDoubleClickDownListener(this);
        addView(view);
    }

    /**
     * 添加爆炸特效图片
     *
     * @return
     */
    private View addView(float x, float y) {
        ImageView likeView = new ImageView(this.context);
        likeView.setImageResource(mImgResIds[new Random().nextInt(mImgResIds.length)]);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) x, (int) y, 0, 0);
        likeView.setLayoutParams(layoutParams);
        addView(likeView);
        return likeView;
    }

    /**
     * 计算对应角度的坐标点
     *
     * @param angle
     * @param radius
     * @return
     */
    private int[] calPointValue(int angle, int radius) {
        int[] point = new int[2];
        if (0 < angle && angle < 90) {//第一象限
            point = getPointValue(angle, radius);
            point[0] = point[0];
            point[1] = point[1];
        }
        if (90 < angle && angle < 180) {//第二象限
            point = getPointValue(angle, radius);
            point[0] = -point[0];
            point[1] = point[1];
        }
        if (180 < angle && angle < 270) {//第三象限
            point = getPointValue(angle, radius);
            point[0] = -point[0];
            point[1] = -point[1];
        }
        if (270 < angle && angle < 360) {//第四象限
            point = getPointValue(angle, radius);
            point[0] = point[0];
            point[1] = -point[1];
        }
        if (angle > 360) {
            angle = angle - 360;
            point = getPointValue(angle, radius);
            point[0] = point[0];
            point[1] = point[1];
        }
        return point;
    }

    /**
     * 生成 5 - 85 的随机数，作为坐标系第一象限的第一个起始角度
     *
     * @return
     */
    private int createAngle() {
        int max = 85;
        int min = 5;
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 生成 200 - 600 的随机数，作为发散的半径
     *
     * @return
     */
    private int createRadius() {
        int max = 600;
        int min = 200;
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 生成 5 - 12 的随机数，作为发散的个数
     *
     * @return
     */
    private int createCount() {
        int max = 12;
        int min = 5;
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 获取x、y的值
     *
     * @param angle
     * @param radius
     * @return
     */
    private int[] getPointValue(int angle, int radius) {
        int[] point = new int[2];
        point[0] = (int) getValueX(angle, radius);
        point[1] = (int) getValueY(angle, radius);
        return point;
    }

    /**
     * 通过夹角和半径计算对边长度
     *
     * @param angle
     * @param radius
     */
    private double getValueX(int angle, int radius) {
        double sin = Math.sin(aTor(angle));
        if (sin < 0)
            sin = -sin;
        return radius * sin;
    }

    /**
     * 通过夹角和半径计算邻边长度
     *
     * @param angle
     * @param radius
     */
    private double getValueY(int angle, int radius) {
        double cos = Math.cos(aTor(angle));
        if (cos < 0)
            cos = -cos;
        return radius * cos;
    }

    /**
     * 角度转弧度
     *
     * @param angle
     * @return
     */
    private double aTor(int angle) {
        return Math.toRadians(angle);
    }

    @Override
    public void onDoubleClick(View view, float x, float y) {
        int initAngle = createAngle();//初始的角度
        int initCount = createCount();//初始的个数
        int increasingAngle = 360 / initCount;//根据个数设置递增角度
        for (int i = 0; i < initCount; i++) {
            View likeView = addView(x, y);
            int initRadius = createRadius();//初始化半径
            int angle = initAngle + (i * increasingAngle);
            int[] point = calPointValue(angle, initRadius);
            startAnimation(likeView, 0, point[0], 0, point[1]);
        }
        if (doubleClickCallback != null) {
            doubleClickCallback.onDoubleClick(view, x, y);
        }
    }

    public void startAnimation(View view, int startX, int endX, int startY, int endY) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.5f);
        ObjectAnimator transX = ObjectAnimator.ofFloat(view, "translationX", startX, endX);
        ObjectAnimator transY = ObjectAnimator.ofFloat(view, "translationY", startY, endY);
        AnimatorSet enterAnimation = new AnimatorSet();
        enterAnimation.playTogether(alpha, transX, transY);
        enterAnimation.setDuration(durationTime);
        enterAnimation.setInterpolator(interpolators[2]);
        enterAnimation.setTarget(view);
        AnimatorSet finalAnimatorSet = new AnimatorSet();
        finalAnimatorSet.setTarget(view);
        finalAnimatorSet.playSequentially(enterAnimation);
        finalAnimatorSet.addListener(new AnimationEndListener(view));
        finalAnimatorSet.start();
    }

    /**
     * 动画结束监听器,用于释放无用的资源
     */
    private class AnimationEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimationEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            Log.w(TAG, "onAnimationEnd动画结束");
            removeView(target);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            Log.w(TAG, "onAnimationStart动画开始");
        }

    }

    @Override
    public void onDoubleTapEventClick(View view, float x, float y) {
        Log.w(TAG, "双击时不能单击");
        isDoubleClick = true;
    }

    @Override
    public void onSingleClick(View view) {
        delaySingleClickInterval();
        if (singleClickCallback != null) {
            singleClickCallback.onSingleClick(view);
            isDoubleClick = false;
        }
    }

    /**
     * 延迟双击和单击之间的时间间隔
     */
    private void delaySingleClickInterval() {
        if (isDoubleClick) {
            clearSingleClickCallback();
            ((Activity) context).getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    resumeSingleClickCallback();
                    isDoubleClick = false;
                }
            }, clickInterval);
        }
    }

    private void resumeSingleClickCallback() {
        if (singleClickCallbackTemp != null) {
            singleClickCallback = singleClickCallbackTemp;
        }
    }

    private void clearSingleClickCallback() {
        if (singleClickCallback != null) {
            singleClickCallbackTemp = singleClickCallback;
            singleClickCallback = null;
        }
    }
}
