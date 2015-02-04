package pl.zielony.carbon.beta;

import android.graphics.Rect;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Marcin on 2014-11-14.
 */
public class LayoutAnimation extends Animation {
    private final Rect from;
    private final Rect to;
    private final AnimatedLayout view;
    private boolean reversed;

    public LayoutAnimation(Rect from, Rect to, AnimatedLayout view) {
        this.from = from;
        this.to = to;
        this.view = view;
    }

    @Override
    protected void applyTransformation(float v, Transformation t) {
        if(reversed)
            v = 1-v;

        view.setClipRect(lerp(from.left, to.left, v),
                lerp(from.top, to.top, v),
                lerp(from.right, to.right, v),
                lerp(from.bottom, to.bottom, v));
        super.applyTransformation(v, t);
    }

    private int lerp(float f, float t, float v) {
        return (int) (f * (1 - v) + t * v);
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
}
