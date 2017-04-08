package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedColorStateList;
import carbon.animation.AnimatedView;
import carbon.drawable.CircularProgressDrawable;
import carbon.drawable.DefaultPrimaryColorStateList;
import carbon.drawable.ProgressBarDrawable;
import carbon.drawable.ProgressDrawable;

public class ProgressBar extends View implements AnimatedView, TintedView, VisibleView {
    private ProgressDrawable drawable;

    public enum Style {
        BarDeterminate, BarIndeterminate, BarQuery, CircularDeterminate, CircularIndeterminate
    }

    public ProgressBar(Context context) {
        super(context);
        initProgressBar(null, android.R.attr.progressBarStyle);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ProgressBar, android.R.attr.progressBarStyle, R.styleable.ProgressBar_carbon_theme), attrs);
        initProgressBar(attrs, android.R.attr.progressBarStyle);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ProgressBar, defStyleAttr, R.styleable.ProgressBar_carbon_theme), attrs, defStyleAttr);
        initProgressBar(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ProgressBar, defStyleAttr, R.styleable.ProgressBar_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initProgressBar(attrs, defStyleAttr);
    }

    private static int[] animationIds = new int[]{
            R.styleable.ProgressBar_carbon_inAnimation,
            R.styleable.ProgressBar_carbon_outAnimation
    };
    private static int[] tintIds = new int[]{
            R.styleable.ProgressBar_carbon_tint,
            R.styleable.ProgressBar_carbon_tintMode,
            R.styleable.ProgressBar_carbon_backgroundTint,
            R.styleable.ProgressBar_carbon_backgroundTintMode,
            R.styleable.ProgressBar_carbon_animateColorChanges
    };

    private void initProgressBar(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBar, defStyleAttr, R.style.carbon_ProgressBar);
        Style style = Style.values()[a.getInt(R.styleable.ProgressBar_carbon_progressStyle, 0)];
        if (style == Style.BarDeterminate || style == Style.BarIndeterminate || style == Style.BarQuery) {
            setDrawable(new ProgressBarDrawable());
        } else {
            setDrawable(new CircularProgressDrawable());
        }
        drawable.setStyle(style);

        drawable.setBarWidth(a.getDimension(R.styleable.ProgressBar_carbon_barWidth, 5));

        Carbon.initTint(this, a, tintIds);
        Carbon.initAnimations(this, a, animationIds);

        a.recycle();

        if (getVisibility() == VISIBLE) {
            setBarWidth(getBarWidth() + getBarPadding());
            setBarPadding(0);
        } else {
            setBarPadding(getBarWidth() + getBarPadding());
            setBarWidth(0);
        }
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

    public void setDrawable(ProgressDrawable newDrawable) {
        this.drawable = newDrawable;

        if (drawable != null)
            drawable.setCallback(null);

        if (newDrawable != null)
            newDrawable.setCallback(this);
    }

    public ProgressDrawable getDrawable() {
        return drawable;
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || who == drawable;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (drawable != null)
            drawable.draw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        if (drawable != null)
            drawable.setBounds(0, 0, getWidth(), getHeight());
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim = AnimUtils.Style.None, outAnim = AnimUtils.Style.None;
    private Animator animator;

    public void setVisibility(final int visibility) {
        if (visibility == View.VISIBLE && (getVisibility() != View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (inAnim != AnimUtils.Style.None) {
                animator = AnimUtils.animateIn(this, inAnim, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator a) {
                        animator = null;
                        clearAnimation();
                    }
                });
            }
            super.setVisibility(visibility);
        } else if (visibility != View.VISIBLE && (getVisibility() == View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (outAnim == AnimUtils.Style.None) {
                super.setVisibility(visibility);
                return;
            }
            animator = AnimUtils.animateOut(this, outAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator a) {
                    if (((ValueAnimator) a).getAnimatedFraction() == 1)
                        ProgressBar.super.setVisibility(visibility);
                    animator = null;
                    clearAnimation();
                }
            });
        }
    }

    public void setVisibilityImmediate(final int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            setBarWidth(getBarWidth() + getBarPadding());
            setBarPadding(0);
        } else {
            setBarPadding(getBarWidth() + getBarPadding());
            setBarWidth(0);
        }
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
    PorterDuff.Mode tintMode;
    ColorStateList backgroundTint;
    PorterDuff.Mode backgroundTintMode;
    boolean animateColorChanges;
    ValueAnimator.AnimatorUpdateListener tintAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            postInvalidate();
            ViewCompat.postInvalidateOnAnimation(ProgressBar.this);
        }
    };
    ValueAnimator.AnimatorUpdateListener backgroundTintAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            postInvalidate();
            ViewCompat.postInvalidateOnAnimation(ProgressBar.this);
        }
    };

    @Override
    public void setTint(ColorStateList list) {
        this.tint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, tintAnimatorListener) : list;
        updateTint();
    }

    @Override
    public void setTint(int color) {
        if (color == 0) {
            setTint(new DefaultPrimaryColorStateList(getContext()));
        } else {
            setTint(ColorStateList.valueOf(color));
        }
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }

    private void updateTint() {
        if (tint != null && tintMode != null) {
            int color = tint.getColorForState(getDrawableState(), tint.getDefaultColor());
            if (drawable != null) {
                drawable.setTint(color);
                drawable.setTintMode(tintMode);
            }
        } else {
            if (drawable != null) {
                drawable.setTint(null);
            }
        }
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode mode) {
        this.tintMode = mode;
        updateTint();
    }

    @Override
    public PorterDuff.Mode getTintMode() {
        return tintMode;
    }

    @Override
    public void setBackgroundTint(ColorStateList list) {
        this.backgroundTint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, backgroundTintAnimatorListener) : list;
        updateBackgroundTint();
    }

    @Override
    public void setBackgroundTint(int color) {
        if (color == 0) {
            setBackgroundTint(new DefaultPrimaryColorStateList(getContext()));
        } else {
            setBackgroundTint(ColorStateList.valueOf(color));
        }
    }

    @Override
    public ColorStateList getBackgroundTint() {
        return backgroundTint;
    }

    private void updateBackgroundTint() {
        if (getBackground() == null)
            return;
        if (backgroundTint != null && backgroundTintMode != null) {
            int color = backgroundTint.getColorForState(getDrawableState(), backgroundTint.getDefaultColor());
            getBackground().setColorFilter(new PorterDuffColorFilter(color, tintMode));
        } else {
            getBackground().setColorFilter(null);
        }
    }

    @Override
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        this.backgroundTintMode = mode;
        updateBackgroundTint();
    }

    @Override
    public PorterDuff.Mode getBackgroundTintMode() {
        return backgroundTintMode;
    }

    public boolean isAnimateColorChangesEnabled() {
        return animateColorChanges;
    }

    public void setAnimateColorChangesEnabled(boolean animateColorChanges) {
        this.animateColorChanges = animateColorChanges;
        if (tint != null && !(tint instanceof AnimatedColorStateList))
            setTint(AnimatedColorStateList.fromList(tint, tintAnimatorListener));
        if (backgroundTint != null && !(backgroundTint instanceof AnimatedColorStateList))
            setBackgroundTint(AnimatedColorStateList.fromList(backgroundTint, backgroundTintAnimatorListener));
    }
}
