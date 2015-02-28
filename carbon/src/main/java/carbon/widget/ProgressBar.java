package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;

import carbon.R;
import carbon.animation.AnimUtils;
import carbon.drawable.ProgressBarDrawable;

/**
 * Created by Marcin on 2015-02-08.
 */
public class ProgressBar extends View {
    private ProgressBarDrawable drawable;

    public ProgressBar(Context context) {
        super(context);
        init(null, R.attr.carbon_progressBarStyle);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_progressBarStyle);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        drawable = new ProgressBarDrawable();
        setBackgroundDrawable(drawable);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBar, defStyle, 0);
        int color = a.getColor(R.styleable.ProgressBar_carbon_barColor, 0);
        drawable.setBarColor(color);
        int arcBacground = a.getColor(R.styleable.ProgressBar_carbon_barBackground, 0);
        drawable.setBarBackground(arcBacground);
        drawable.setBarWidth(a.getDimension(R.styleable.ProgressBar_carbon_barWidth, 5));
        drawable.setIndeterminate(a.getBoolean(R.styleable.ProgressBar_carbon_indeterminate, false));

        inAnim = AnimUtils.Style.values()[a.getInt(R.styleable.ProgressBar_carbon_inAnimation, 0)];
        outAnim = AnimUtils.Style.values()[a.getInt(R.styleable.ProgressBar_carbon_outAnimation, 0)];

        a.recycle();
    }

    public void setProgress(float progress) {
        drawable.setProgress(progress);
    }

    public float getProgress() {
        return drawable.getProgress();
    }

    public float getBarWidth() {
        return drawable.getBarWidth();
    }

    public void setBarWidth(float arcWidth) {
        drawable.setBarWidth(arcWidth);
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim, outAnim;

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            ViewHelper.setAlpha(this, 0);
            super.setVisibility(visibility);
            AnimUtils.animateIn(this, inAnim, null);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    ProgressBar.super.setVisibility(visibility);
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
