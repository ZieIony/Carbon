package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.drawable.ControlFocusedColorStateList;
import carbon.drawable.EmptyDrawable;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;

/**
 * Created by Marcin on 2015-06-25.
 */
public class RangeSeekBar extends View implements RippleView, carbon.animation.StateAnimatorView, AnimatedView, TintedView {
    private static float RIPPLE_RADIUS;
    private static float THUMB_RADIUS;
    float value = 0.3f, value2 = 0.7f;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private int colorControl;

    public RangeSeekBar(Context context) {
        this(context, null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode())
            return;

        Resources.Theme theme = getContext().getTheme();
        TypedValue typedvalueattr = new TypedValue();
        theme.resolveAttribute(R.attr.colorControlNormal, typedvalueattr, true);
        colorControl = typedvalueattr.resourceId != 0 ? getContext().getResources().getColor(typedvalueattr.resourceId) : typedvalueattr.data;

        THUMB_RADIUS = Carbon.getDip(getContext()) * 12;
        RIPPLE_RADIUS = Carbon.getDip(getContext()) * 15;

        Carbon.initAnimations(this, attrs, defStyleAttr);
        Carbon.initTint(this, attrs, defStyleAttr);
        Carbon.initRippleDrawable(this, attrs, defStyleAttr);

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

        int thumbX = (int) (value * (getWidth() - getPaddingLeft() - getPaddingRight() - THUMB_RADIUS * 2) + getPaddingLeft() + THUMB_RADIUS);
        int thumbY = getHeight() / 2;
        int thumbX2 = (int) (value2 * (getWidth() - getPaddingLeft() - getPaddingRight() - THUMB_RADIUS * 2) + getPaddingLeft() + THUMB_RADIUS);

        paint.setStrokeWidth(Carbon.getDip(getContext()) * 2);
        paint.setColor(tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
        canvas.drawCircle(thumbX, thumbY, THUMB_RADIUS, paint);
        canvas.drawCircle(thumbX2, thumbY, THUMB_RADIUS, paint);
        if (thumbX + THUMB_RADIUS < thumbX2 - THUMB_RADIUS)
            canvas.drawLine(thumbX + THUMB_RADIUS, thumbY, thumbX2 - THUMB_RADIUS, thumbY, paint);
        paint.setColor(colorControl);
        if (getPaddingLeft() + THUMB_RADIUS < thumbX - THUMB_RADIUS)
            canvas.drawLine(getPaddingLeft() + THUMB_RADIUS, thumbY, thumbX - THUMB_RADIUS, thumbY, paint);
        if (thumbX2 + THUMB_RADIUS < getWidth() - getPaddingLeft() - THUMB_RADIUS)
            canvas.drawLine(thumbX2 + THUMB_RADIUS, thumbY, getWidth() - getPaddingLeft() - THUMB_RADIUS, thumbY, paint);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(thumbX - THUMB_RADIUS * 1.5f, thumbY - THUMB_RADIUS * 1.5f);
        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
        canvas.restore();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(thumbX - THUMB_RADIUS * 1.5f, thumbY - THUMB_RADIUS * 1.5f);
        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
        canvas.restore();
    }


    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;
    private EmptyDrawable emptyBackground = new EmptyDrawable();

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        THUMB_RADIUS = Carbon.getDip(getContext()) * 12;

        int thumbX = (int) (value * (getWidth() - getPaddingLeft() - getPaddingRight() - THUMB_RADIUS * 2) + getPaddingLeft() + THUMB_RADIUS);
        int thumbY = getHeight() / 2;
        int thumbX2 = (int) (value2 * (getWidth() - getPaddingLeft() - getPaddingRight() - THUMB_RADIUS * 2) + getPaddingLeft() + THUMB_RADIUS);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
        }

        if (Math.abs(event.getX() - thumbX) < Math.abs(event.getX() - thumbX2)) {
            value = (event.getX() - getPaddingLeft() - THUMB_RADIUS) / (getWidth() - getPaddingLeft() - getPaddingRight() - THUMB_RADIUS * 2);
            value = Math.max(0, Math.min(value, 1));
            rippleDrawable.setBounds((int) (thumbX - RIPPLE_RADIUS), (int) (thumbY - RIPPLE_RADIUS), (int) (thumbX + RIPPLE_RADIUS), (int) (thumbY + RIPPLE_RADIUS));
        } else {
            value2 = (event.getX() - getPaddingLeft() - THUMB_RADIUS) / (getWidth() - getPaddingLeft() - getPaddingRight() - THUMB_RADIUS * 2);
            value2 = Math.max(0, Math.min(value2, 1));
            rippleDrawable.setBounds((int) (thumbX2 - RIPPLE_RADIUS), (int) (thumbY - RIPPLE_RADIUS), (int) (thumbX2 + RIPPLE_RADIUS), (int) (thumbY + RIPPLE_RADIUS));
        }

        if (rippleDrawable != null)
            rippleDrawable.setHotspot(event.getX(), event.getY());

        postInvalidate();
        return super.dispatchTouchEvent(event);
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

    private List<StateAnimator> stateAnimators = new ArrayList<>();

    public void removeStateAnimator(StateAnimator animator) {
        stateAnimators.remove(animator);
    }

    public void addStateAnimator(StateAnimator animator) {
        this.stateAnimators.add(animator);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (rippleDrawable != null && rippleDrawable.getStyle() != RippleDrawable.Style.Background)
            rippleDrawable.setState(getDrawableState());
        if (stateAnimators != null)
            for (StateAnimator animator : stateAnimators)
                animator.stateChanged(getDrawableState());
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
                        RangeSeekBar.super.setVisibility(visibility);
                    animator = null;
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
        this.tint = list != null ? list : new ControlFocusedColorStateList(getContext());
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
