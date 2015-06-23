package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedView;
import carbon.drawable.CircularProgressDrawable;
import carbon.drawable.ProgressBarDrawable;
import carbon.drawable.ProgressDrawable;

/**
 * Created by Marcin on 2015-02-08.
 */
public class ProgressBar extends View implements AnimatedView, TintedView {
    private ProgressDrawable drawable;

    public enum Style {
        BarDeterminate, BarIndeterminate, BarQuery, CircularDeterminate, CircularIndeterminate
    }

    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_progressBarStyle);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBar, defStyleAttr, 0);
        Style style = Style.values()[a.getInt(R.styleable.ProgressBar_carbon_progressStyle, 0)];
        if (style == Style.BarDeterminate || style == Style.BarIndeterminate || style == Style.BarQuery) {
            drawable = new ProgressBarDrawable();
        } else {
            drawable = new CircularProgressDrawable();
        }
        drawable.setStyle(style);
        setBackgroundDrawable(drawable);

        drawable.setBarWidth(a.getDimension(R.styleable.ProgressBar_carbon_barWidth, 5));

        Carbon.initTint(this,attrs,defStyleAttr);
        Carbon.initAnimations(this, attrs, defStyleAttr);

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

    public void setBarPadding(float padding) {
        drawable.setBarPadding(padding);
    }

    public float getBarPadding() {
        return drawable.getBarPadding();
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim, outAnim;
    private Animator animator;

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            animator = AnimUtils.animateIn(this, inAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator a) {
                    animator = null;
                }
            });
            super.setVisibility(visibility);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            animator = AnimUtils.animateOut(this, outAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator a) {
                    ProgressBar.super.setVisibility(visibility);
                    animator = null;
                }
            });
        }
    }

    public void setVisibilityImmediate(final int visibility){
        super.setVisibility(visibility);
    }

    public Animator getAnimator() {
        return animator;
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


    // -------------------------------
    // tint
    // -------------------------------

    ColorStateList tint;

    @Override
    public void setTint(ColorStateList list) {
        this.tint = list;
        drawable.setBarColor(list);
    }

    @Override
    public void setTint(int color) {
        setTint(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }

}
