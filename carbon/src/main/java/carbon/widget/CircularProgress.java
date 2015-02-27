package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import carbon.R;
import carbon.animation.AnimUtils;
import carbon.drawable.CircularProgressDrawable;

/**
 * Created by Marcin on 2015-02-08.
 */
public class CircularProgress extends View {
    private CircularProgressDrawable drawable;

    public CircularProgress(Context context) {
        super(context);
        init(null, R.attr.carbon_circularProgressStyle);
    }

    public CircularProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_circularProgressStyle);
    }

    public CircularProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        drawable = new CircularProgressDrawable();
        setBackgroundDrawable(drawable);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircularProgress, defStyle, 0);
        int color = a.getColor(R.styleable.CircularProgress_carbon_arcColor, 0);
        drawable.setArcColor(color);
        int arcBacground = a.getColor(R.styleable.CircularProgress_carbon_arcBackground, 0);
        drawable.setArcBackground(arcBacground);
        drawable.setArcWidth(a.getDimension(R.styleable.CircularProgress_carbon_arcWidth, 5));
        drawable.setArcPadding(a.getDimension(R.styleable.CircularProgress_carbon_arcPadding, 0));
        drawable.setIndeterminate(a.getBoolean(R.styleable.CircularProgress_carbon_indeterminate, false));

        inAnim = AnimUtils.Style.values()[a.getInt(R.styleable.CircularProgress_carbon_inAnimation, 0)];
        outAnim = AnimUtils.Style.values()[a.getInt(R.styleable.CircularProgress_carbon_outAnimation, 0)];

        a.recycle();
    }

    public void setProgress(float progress) {
        drawable.setProgress(progress);
    }

    public float getProgress() {
        return drawable.getProgress();
    }

    public float getArcWidth() {
        return drawable.getArcWidth();
    }

    public void setArcWidth(float arcWidth) {
        drawable.setArcWidth(arcWidth);
    }

    public void setArcPadding(float arcPadding) {
        drawable.setArcPadding(arcPadding);
    }

    public float getArcPadding() {
        return drawable.getArcPadding();
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim, outAnim;

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            super.setVisibility(visibility);
            AnimUtils.animateIn(this, inAnim, null);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    CircularProgress.super.setVisibility(visibility);
                }
            });
        }
    }

    public AnimUtils.Style getOutAnimation() {
        return outAnim;
    }

    public void setOutAnimation(AnimUtils.Style outAnim) {
        this.outAnim = outAnim;
    }

    public AnimUtils.Style getInAnimation() {
        return inAnim;
    }

    public void setInAnimation(AnimUtils.Style inAnim) {
        this.inAnim = inAnim;
    }
}
