package com.example.administrator.explosionlayout.widget;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by c on 2018/1/3.
 */

public class ArcEvaluator implements TypeEvaluator<PointF> {

    private PointF p1;

    public ArcEvaluator(PointF p1) {
        super();
        this.p1 = p1;
    }

    @Override
    public PointF evaluate(float fraction, PointF p0, PointF p2) {
        PointF pointf = new PointF();

        // 贝塞尔曲线公式  p0*(1-t)^2 + 2p1*t*(1-t) + p2*t^2
        pointf.x = p0.x * (1 - fraction) * (1 - fraction) +
                2 * p1.x * fraction * (1 - fraction) + fraction * fraction * p2.x;
        pointf.y = p0.y * (1 - fraction) * (1 - fraction) +
                2 * p1.y * fraction * (1 - fraction) + fraction * fraction * p2.y;
        return pointf;
    }
}