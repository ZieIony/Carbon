package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.animation.StateAnimatorView;
import carbon.drawable.EmptyDrawable;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;
import carbon.drawable.VectorDrawable;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowShape;
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2015-01-22.
 */
public class ImageView extends android.widget.ImageView implements ShadowView, RippleView, TouchMarginView, StateAnimatorView, AnimatedView, CornerView, TintedView {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public ImageView(Context context) {
        this(context, null);
    }

    public ImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_imageViewStyle);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImageView, defStyleAttr, 0);

        setElevation(a.getDimension(R.styleable.ImageView_carbon_elevation, 0));
        int resId = a.getResourceId(R.styleable.ImageView_android_src, 0);
        if (getDrawable() == null && ((resId & 0xffff0000) == 0x7f050000))
            setImageDrawable(new VectorDrawable(getResources(), resId));

        setCornerRadius((int) a.getDimension(R.styleable.ImageView_carbon_cornerRadius, 0));

        Carbon.initRippleDrawable(this, attrs, defStyleAttr);
        Carbon.initAnimations(this, attrs, defStyleAttr);
        Carbon.initTouchMargin(this, attrs, defStyleAttr);
        Carbon.initTint(this, attrs, defStyleAttr);

        a.recycle();
    }


    // -------------------------------
    // corners
    // -------------------------------

    private int cornerRadius;
    private Path cornersMask;
    private static PorterDuffXfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidateShadow();
        initCorners();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed)
            return;

        invalidateShadow();

        if (getWidth() == 0 || getHeight() == 0)
            return;

        initCorners();

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    private void initCorners() {
        if (cornerRadius > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setClipToOutline(true);
                setOutlineProvider(ShadowShape.viewOutlineProvider);
            } else {
                cornersMask = new Path();
                cornersMask.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), cornerRadius, cornerRadius, Path.Direction.CW);
                cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (cornerRadius > 0 && getWidth() > 0 && getHeight() > 0 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            super.draw(canvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(canvas);

            paint.setXfermode(pdMode);
            canvas.drawPath(cornersMask, paint);

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            super.draw(canvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(canvas);
        }
    }


    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;
    private EmptyDrawable emptyBackground = new EmptyDrawable();

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (rippleDrawable != null && event.getAction() == MotionEvent.ACTION_DOWN)
            rippleDrawable.setHotspot(event.getX(), event.getY());
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

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).invalidate();
    }

    @Override
    public void invalidate(@NonNull Rect dirty) {
        super.invalidate(dirty);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate(dirty);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).invalidate(dirty);
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate(l, t, r, b);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).invalidate(l, t, r, b);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate();

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).invalidate();
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds) {
        super.postInvalidateDelayed(delayMilliseconds);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
    }

    @Override
    public void postInvalidate() {
        super.postInvalidate();
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidate();

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public void postInvalidate(int left, int top, int right, int bottom) {
        super.postInvalidate(left, top, right, bottom);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidate(left, top, right, bottom);

        if (getElevation() > 0 || getCornerRadius() > 0)
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
    // elevation
    // -------------------------------

    private float elevation = 0;
    private float translationZ = 0;
    private Shadow shadow;

    @Override
    public float getElevation() {
        return elevation;
    }

    public synchronized void setElevation(float elevation) {
        if (elevation == this.elevation)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            super.setElevation(elevation);
        this.elevation = elevation;
        if (getParent() != null)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public float getTranslationZ() {
        return translationZ;
    }

    public synchronized void setTranslationZ(float translationZ) {
        if (translationZ == this.translationZ)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            super.setTranslationZ(translationZ);
        this.translationZ = translationZ;
        if (getParent() != null)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public ShadowShape getShadowShape() {
        if (cornerRadius == getWidth() / 2 && getWidth() == getHeight())
            return ShadowShape.CIRCLE;
        if (cornerRadius > 0)
            return ShadowShape.ROUND_RECT;
        return ShadowShape.RECT;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setTranslationZ(enabled ? 0 : -elevation);
    }

    @Override
    public Shadow getShadow() {
        float elevation = getElevation() + getTranslationZ();
        if (elevation >= 0.01f && getWidth() > 0 && getHeight() > 0) {
            if (shadow == null || shadow.elevation != elevation)
                shadow = ShadowGenerator.generateShadow(this, elevation);
            return shadow;
        }
        return null;
    }

    @Override
    public void invalidateShadow() {
        shadow = null;
        if (getParent() != null && getParent() instanceof View)
            ((View) getParent()).postInvalidate();
    }


    // -------------------------------
    // touch margin
    // -------------------------------

    private Rect touchMargin;

    @Override
    public void setTouchMargin(int left, int top, int right, int bottom) {
        touchMargin = new Rect(left, top, right, bottom);
    }

    @Override
    public void setTouchMarginLeft(int margin) {
        touchMargin.left = margin;
    }

    @Override
    public void setTouchMarginTop(int margin) {
        touchMargin.top = margin;
    }

    @Override
    public void setTouchMarginRight(int margin) {
        touchMargin.right = margin;
    }

    @Override
    public void setTouchMarginBottom(int margin) {
        touchMargin.bottom = margin;
    }

    @Override
    public Rect getTouchMargin() {
        return touchMargin;
    }

    public void getHitRect(@NonNull Rect outRect) {
        if (touchMargin == null) {
            super.getHitRect(outRect);
            return;
        }
        outRect.set(getLeft() - touchMargin.left, getTop() - touchMargin.top, getRight() + touchMargin.right, getBottom() + touchMargin.bottom);
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
        updateTint();
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
                    ImageView.super.setVisibility(visibility);
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
        this.tint = list;
        updateTint();
        postInvalidate();
    }

    @Override
    public void setTint(int color) {
        setTint(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }

    private void updateTint() {
        if (tint != null) {
            int color = tint.getColorForState(getDrawableState(), tint.getDefaultColor());
            setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            setAlpha(Color.alpha(color));
        }
    }
}
