package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.drawable.DefaultColorStateList;
import carbon.drawable.EmptyDrawable;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;

/**
 * Created by Marcin on 2015-06-25.
 */
public class SeekBar extends View implements RippleView, StateAnimatorView, AnimatedView, TintedView {
    private static float THUMB_RADIUS, THUMB_RADIUS_DRAGGED, STROKE_WIDTH;
    float value = 0.5f;
    float min = 0, max = 1, step = 1;
    float thumbRadius;

    OnValueChangedListener onValueChangedListener;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private int colorControl;

    private Style style;

    DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private ValueAnimator radiusAnimator, valueAnimator;

    public enum Style {
        Continuous, Discrete
    }

    public interface OnValueChangedListener {
        void onValueChanged(SeekBar seekBar, float value);
    }

    public SeekBar(Context context) {
        super(context);
        initSeekBar(null, android.R.attr.seekBarStyle);
    }

    public SeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSeekBar(attrs, android.R.attr.seekBarStyle);
    }

    public SeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSeekBar(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSeekBar(attrs, defStyleAttr);
    }

    private void initSeekBar(AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode())
            return;

        colorControl = Carbon.getThemeColor(getContext(), R.attr.colorControlNormal);

        thumbRadius = THUMB_RADIUS = Carbon.getDip(getContext()) * 8;
        THUMB_RADIUS_DRAGGED = Carbon.getDip(getContext()) * 10;
        STROKE_WIDTH = Carbon.getDip(getContext()) * 2;

        if (attrs != null) {
            Carbon.initAnimations(this, attrs, defStyleAttr);
            Carbon.initTint(this, attrs, defStyleAttr);
            Carbon.initRippleDrawable(this, attrs, defStyleAttr);

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SeekBar, defStyleAttr, 0);

            setStyle(Style.values()[a.getInt(R.styleable.SeekBar_carbon_barStyle, 0)]);
            setMin(a.getFloat(R.styleable.SeekBar_carbon_min, 0));
            setMax(a.getFloat(R.styleable.SeekBar_carbon_max, 0));
            setStepSize(a.getFloat(R.styleable.SeekBar_carbon_stepSize, 0));
            setValue(a.getFloat(R.styleable.SeekBar_carbon_value, 0));

            a.recycle();
        }

        setFocusableInTouchMode(false); // TODO: from theme
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        float v = (value - min) / (max - min);
        int thumbX = (int) (v * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
        int thumbY = getHeight() / 2;

        paint.setStrokeWidth(STROKE_WIDTH);
        if (!isInEditMode())
            paint.setColor(tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
        if (getPaddingLeft() < thumbX - thumbRadius)
            canvas.drawLine(getPaddingLeft(), thumbY, thumbX - thumbRadius, thumbY, paint);

        paint.setColor(colorControl);
        if (thumbX + thumbRadius < getWidth() - getPaddingLeft())
            canvas.drawLine(thumbX + thumbRadius, thumbY, getWidth() - getPaddingLeft(), thumbY, paint);

        if (style == Style.Discrete) {
            paint.setColor(Color.BLACK);
            float range = (max - min) / step;
            for (int i = 0; i < range; i++)
                canvas.drawCircle(i / range * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft(), getHeight() / 2, STROKE_WIDTH, paint);
            canvas.drawCircle(getWidth() - getPaddingRight(), getHeight() / 2, STROKE_WIDTH, paint);
        }

        if (!isInEditMode())
            paint.setColor(tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
        canvas.drawCircle(thumbX, thumbY, thumbRadius, paint);

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        if (max > min) {
            this.max = max;
        } else {
            this.max = min + step;
        }
        this.value = Math.max(min, Math.min(value, max));
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        if (min < max) {
            this.min = min;
        } else if (this.max > step) {
            this.min = max - step;
        } else {
            this.min = 0;
        }
        this.value = Math.max(min, Math.min(value, max));
    }

    public float getValue() {
        if (style == Style.Discrete)
            return (float) Math.floor((value - min + step / 2) / step) * step + min;
        return value;
    }

    public void setValue(float value) {
        this.value = Math.max(min, Math.min(value, max));
    }

    public float getStepSize() {
        return step;
    }

    public void setStepSize(float step) {
        if (step > 0) {
            this.step = step;
        } else {
            this.step = 1;
        }
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }


    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;
    private EmptyDrawable emptyBackground = new EmptyDrawable();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (radiusAnimator != null)
                radiusAnimator.end();
            radiusAnimator = ValueAnimator.ofFloat(thumbRadius, THUMB_RADIUS_DRAGGED);
            radiusAnimator.setDuration(200);
            radiusAnimator.setInterpolator(interpolator);
            radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    thumbRadius = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            radiusAnimator.start();
            ViewParent parent = getParent();
            if (parent != null)
                parent.requestDisallowInterceptTouchEvent(true);
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            if (style == Style.Discrete) {
                float val = (float) Math.floor((value - min + step / 2) / step) * step + min;
                if (valueAnimator != null)
                    valueAnimator.cancel();
                valueAnimator = ValueAnimator.ofFloat(value, val);
                valueAnimator.setDuration(200);
                valueAnimator.setInterpolator(interpolator);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        value = (float) animation.getAnimatedValue();
                        int thumbX = (int) ((value - min) / (max - min) * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
                        int thumbY = getHeight() / 2;
                        int radius = rippleDrawable.getRadius();
                        rippleDrawable.setBounds(thumbX - radius, thumbY - radius, thumbX + radius, thumbY + radius);
                        postInvalidate();
                    }
                });
                valueAnimator.start();
            }
            if (radiusAnimator != null)
                radiusAnimator.end();
            radiusAnimator = ValueAnimator.ofFloat(thumbRadius, THUMB_RADIUS);
            radiusAnimator.setDuration(200);
            radiusAnimator.setInterpolator(interpolator);
            radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    thumbRadius = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            radiusAnimator.start();
            ViewParent parent = getParent();
            if (parent != null)
                parent.requestDisallowInterceptTouchEvent(false);
        }

        float v = (event.getX() - getPaddingLeft()) / (getWidth() - getPaddingLeft() - getPaddingRight());
        v = Math.max(0, Math.min(v, 1));
        value = v * (max - min) + min;

        if (rippleDrawable != null) {
            rippleDrawable.setHotspot(event.getX(), event.getY());
            int thumbX = (int) (v * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
            int thumbY = getHeight() / 2;
            int radius = rippleDrawable.getRadius();
            rippleDrawable.setBounds(thumbX - radius, thumbY - radius, thumbX + radius, thumbY + radius);
        }

        postInvalidate();
        if (onValueChangedListener != null) {
            if (style == Style.Discrete) {
                onValueChangedListener.onValueChanged(this, (float) Math.floor((value - min + step / 2) / step) * step + min);
            } else {
                onValueChangedListener.onValueChanged(this, value);
            }
        }
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }

    public void setRippleDrawable(RippleDrawable newRipple) {
        if (rippleDrawable != null) {
            rippleDrawable.setCallback(null);
            if (rippleDrawable.getStyle() == RippleDrawable.Style.Background)
                super.setBackgroundDrawable(rippleDrawable.getBackground() == null ? emptyBackground : rippleDrawable.getBackground());
        }

        if (newRipple != null) {
            newRipple.setCallback(this);
            if (newRipple.getStyle() == RippleDrawable.Style.Background) {
                super.setBackgroundDrawable((Drawable) newRipple);
            }
        }

        rippleDrawable = newRipple;
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || rippleDrawable == who;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        super.invalidateDrawable(drawable);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate();
    }

    @Override
    public void invalidate(@NonNull Rect dirty) {
        super.invalidate(dirty);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate(dirty);
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate(l, t, r, b);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate();
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds) {
        super.postInvalidateDelayed(delayMilliseconds);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
    }

    @Override
    public void postInvalidate() {
        super.postInvalidate();
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public void postInvalidate(int left, int top, int right, int bottom) {
        super.postInvalidate(left, top, right, bottom);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidate(left, top, right, bottom);
    }

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (background instanceof RippleDrawable) {
            setRippleDrawable((RippleDrawable) background);
            return;
        }

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Background) {
            rippleDrawable.setCallback(null);
            rippleDrawable = null;
        }
        super.setBackgroundDrawable(background == null ? emptyBackground : background);
    }


    // -------------------------------
    // state animators
    // -------------------------------

    private StateAnimator stateAnimator = new StateAnimator(this);

    @Override
    public StateAnimator getStateAnimator() {
        return stateAnimator;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (rippleDrawable != null && rippleDrawable.getStyle() != RippleDrawable.Style.Background)
            rippleDrawable.setState(getDrawableState());
        if (stateAnimator != null)
            stateAnimator.setState(getDrawableState());
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim, outAnim;
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
                        SeekBar.super.setVisibility(visibility);
                    animator = null;
                    clearAnimation();
                }
            });
        }
    }

    public void setVisibilityImmediate(final int visibility) {
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
        this.tint = list != null ? list : new DefaultColorStateList(getContext());
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
