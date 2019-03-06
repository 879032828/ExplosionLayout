package com.example.administrator.explosionlayout.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.explosionlayout.R;

import java.util.Random;

/**
 * 爆炸特效
 */

public class ExplosionLayout extends RelativeLayout {
    private static final String TAG = "ExplosionLayout";
    /** 每次显示子view个数 */
    private static final int DEFAULT_EXPLOSION_COUNT = 6;
    /** 生成子View后，显示时的动画时长 */
    private static final int DURATION_SHOW = 100;
    /** 子View被抛出的动画时长 */
    private static final int DURATION_MOVE = 600;

    private Context context;
    private LayoutParams params;
    private int mWidth;
    private int mHeight;

    private int[] mImgResIds;
    /** 数字 */
    private int[] mNumResIds;
    /** 文字 */
    private int[] mTextResIds;
    private Interpolator[] interpolators = new Interpolator[4];
    private ViewGroup mDescriptionLayout;
    private LinearLayout mDescriptionNumber;
    private ImageView mDescriptionText;

    public ExplosionLayout(Context context) {
        this(context, null);
    }

    public ExplosionLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExplosionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        initData();
        initParams();
    }

    private void initData() {
        // 图片资源
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

        mNumResIds = new int[]{
                R.mipmap.ic_anim_number_p0,
                R.mipmap.ic_anim_number_p1,
                R.mipmap.ic_anim_number_p2,
                R.mipmap.ic_anim_number_p3,
                R.mipmap.ic_anim_number_p4,
                R.mipmap.ic_anim_number_p5,
                R.mipmap.ic_anim_number_p6,
                R.mipmap.ic_anim_number_p7,
                R.mipmap.ic_anim_number_p8,
                R.mipmap.ic_anim_number_p9
        };

        mTextResIds = new int[]{
                R.mipmap.ic_anim_text_p0,
                R.mipmap.ic_anim_text_p1,
                R.mipmap.ic_anim_text_p2,
                R.mipmap.ic_anim_text_p3
        };

        // 插值器
        interpolators[0] = new AccelerateDecelerateInterpolator(); // 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
        interpolators[1] = new AccelerateInterpolator();  // 在动画开始的地方速率改变比较慢，然后开始加速
        interpolators[2] = new DecelerateInterpolator(); // 在动画开始的地方快然后慢
        interpolators[3] = new LinearInterpolator();  // 以常量速率改变
    }

    private void initParams() {
        Drawable drawable = getContext().getResources().getDrawable(mImgResIds[0]);
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicWidth();
        params = new LayoutParams(width, height);
        params.addRule(ALIGN_PARENT_RIGHT, TRUE);
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        params.rightMargin = getContext().getResources().getDimensionPixelSize(R.dimen.fab_margin);
        params.bottomMargin = getContext().getResources().getDimensionPixelSize(R.dimen.bottom_margin);
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = height;
        setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    /**
     * 开始爆炸
     */
    public void explode() {
        explode(DEFAULT_EXPLOSION_COUNT);
    }

    /**
     * 开始爆炸
     *
     * @param childCount 子view个数
     */
    public void explode(int childCount) {
        addExplosionView(childCount);
    }

    /**
     * 显示爆炸性描述
     *
     * @param count 连击数
     */
    public void showDescription(int count) {
        addDescription(count);
    }

    /**
     * 添加爆炸子View
     */
    private void addExplosionView(int count) {
        for (int i = 0; i < count; i++) {
            final ImageView iv = new ImageView(context);
            iv.setLayoutParams(params);
            iv.setImageResource(mImgResIds[new Random().nextInt(mImgResIds.length)]);

            addView(iv);

            // 开启动画，并且在动画结束后销毁
            Animator set = getAnimatorSet(iv);
            set.start();
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    removeView(iv);
                }
            });
        }
    }

    /**
     * 添加描述性文字
     *
     * @param count
     */
    private void addDescription(int count) {
        if (mDescriptionLayout == null) {
            // 这里涉及到了布局文件，以后可以用纯代码实现
            initDescriptionArea();
        } else {
            mDescriptionLayout.setVisibility(VISIBLE);
        }
        setExplosionNumber(count);
        setExplosionText(count);
    }

    /**
     * 初始化描述区域相关view
     */
    private void initDescriptionArea() {
        mDescriptionLayout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_explosion_text, this, false);
        mDescriptionNumber = (LinearLayout) mDescriptionLayout.getChildAt(0);
        mDescriptionText = (ImageView) mDescriptionLayout.getChildAt(1);
        LayoutParams params = (LayoutParams) mDescriptionLayout.getLayoutParams();
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        params.addRule(ALIGN_PARENT_RIGHT, TRUE);
        params.rightMargin = getContext().getResources().getDimensionPixelSize(R.dimen.right_margin);

        addView(mDescriptionLayout, params);

        initNumberViews();
    }

    /**
     * 初始化数字相关view
     */
    private void initNumberViews() {
        if (mDescriptionNumber != null) {
            for (int i = 0; i < 4; i++) {
                ImageView numberView = new ImageView(getContext());
                mDescriptionNumber.addView(numberView, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    /**
     * 设置数字
     *
     * @param count
     */
    private void setExplosionNumber(int count) {
        int a = -1;
        int b = -1;
        int c = -1;
        int d = -1;

        if (count < 10) {
            a = count;
        } else if (count < 100) {
            a = count % 10;
            b = count / 10;
        } else if (count < 1000) {
            a = count % 10;
            b = count / 10 % 10;
            c = count / 100;
        } else if (count < 10000) {
            a = count % 10;
            b = count / 10 % 10;
            c = count / 100 % 10;
            d = count / 1000;
        } else {
            a = b = c = d = 9;
        }

        showNumber(a, b, c, d);
    }

    /**
     * @param g 个位数
     * @param s 十位数
     * @param b 百位数
     * @param q 千位数
     */
    private void showNumber(int g, int s, int b, int q) {
        setNumber(3, g);
        setNumber(2, s);
        setNumber(1, b);
        setNumber(0, q);
    }

    /**
     * 设置各位数字
     *
     * @param index 第几个数字
     * @param count 数值
     */
    private void setNumber(int index, int count) {
        ImageView imageView = (ImageView) mDescriptionNumber.getChildAt(index);
        if (count >= 0) {
            imageView.setVisibility(VISIBLE);
            imageView.setImageResource(mNumResIds[count]);
        } else {
            imageView.setVisibility(GONE);
        }
    }

    /**
     * 设置文字
     *
     * @param count
     */
    private void setExplosionText(int count) {
        if (count < 30) {
            mDescriptionText.setImageResource(mTextResIds[0]);
        } else if (count < 67) {
            mDescriptionText.setImageResource(mTextResIds[1]);
        } else if (count < 160) {
            mDescriptionText.setImageResource(mTextResIds[2]);
        } else {
            mDescriptionText.setImageResource(mTextResIds[3]);
        }
    }

    /**
     * 状态重置
     */
    public void reset() {
        int childCount = mDescriptionNumber.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView view = (ImageView) mDescriptionNumber.getChildAt(i);
            view.setImageDrawable(null);
        }
        mDescriptionText.setImageResource(mTextResIds[0]);
        mDescriptionLayout.setVisibility(GONE);
    }

    /**
     * 获取动画集合
     *
     * @param view
     */
    private Animator getAnimatorSet(ImageView view) {

        // 1.alpha动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1f);

        // 2.缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.2f, 1f);

        // 动画集合
        AnimatorSet showAnimSet = new AnimatorSet();
        showAnimSet.playTogether(alpha, scaleX, scaleY);
        showAnimSet.setDuration(DURATION_SHOW);

        // 贝塞尔曲线动画
        ValueAnimator bzier = getBzierAnimator(view);

        AnimatorSet set = new AnimatorSet();
        // 先缩放动画，再曲线动画
        set.playSequentially(showAnimSet, bzier);
        set.setTarget(view);
        return set;
    }

    /**
     * 贝塞尔动画
     */
    private ValueAnimator getBzierAnimator(final ImageView iv) {
        PointF[] PointFs = getPointFs(iv); // 4个点的坐标
        ArcEvaluator evaluator = new ArcEvaluator(PointFs[1]);
        ValueAnimator valueAnim = ValueAnimator.ofObject(evaluator, PointFs[0], PointFs[2]);
        valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF p = (PointF) animation.getAnimatedValue();
                iv.setX(p.x);
                iv.setY(p.y);
                // 保证动画前段时间透明度不变
                if (animation.getAnimatedFraction() >= 0.6) {
                    iv.setAlpha(1 - animation.getAnimatedFraction()); // 透明度
                }
            }
        });
        valueAnim.setTarget(iv);
        valueAnim.setDuration(DURATION_MOVE);
        valueAnim.setInterpolator(interpolators[new Random().nextInt(4)]);
        return valueAnim;
    }

    /**
     * 生成3个点：起点，控制点，终点
     *
     * @param iv
     * @return
     */
    private PointF[] getPointFs(ImageView iv) {
        PointF[] PointFs = new PointF[3];

        // 起始位置左上角坐标
        int left = mWidth - params.rightMargin - params.width;
        int top = mHeight - params.bottomMargin - params.height;

        PointFs[0] = new PointF(); // 起点 p0
        PointFs[0].x = left;
        PointFs[0].y = top;

        PointFs[1] = new PointF(); // 控制点 p1
        PointFs[1].x = new Random().nextInt(left);
        PointFs[1].y = new Random().nextInt(top);

        PointFs[2] = new PointF(); // 终点 p2
        PointFs[2].x = new Random().nextInt(left);
        PointFs[2].y = new Random().nextInt(top);
        return PointFs;
    }
}
