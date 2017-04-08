package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.component.ComponentView;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.ElevationComparator;
import carbon.internal.PercentLayoutHelper;
import carbon.internal.Reveal;
import carbon.component.Component;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowShape;
import carbon.shadow.ShadowView;

/**
 * A LinearLayout implementation with support for material features including shadows, ripples, rounded
 * corners, insets, custom drawing order, touch margins, state animators and others.
 */
public class LinearLayout extends android.widget.LinearLayout
        implements ShadowView, RippleView, TouchMarginView, StateAnimatorView, AnimatedView, InsetView, CornerView, MaxSizeView, RevealView, VisibleView {

    private final PercentLayoutHelper percentLayoutHelper = new PercentLayoutHelper(this);
    private OnTouchListener onDispatchTouchListener;

    public LinearLayout(Context context) {
        super(context, null);
        initLinearLayout(null, R.attr.carbon_linearLayoutStyle);
    }

    public LinearLayout(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.LinearLayout, R.attr.carbon_linearLayoutStyle, R.styleable.LinearLayout_carbon_theme), attrs);
        initLinearLayout(attrs, R.attr.carbon_linearLayoutStyle);
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.LinearLayout, defStyleAttr, R.styleable.LinearLayout_carbon_theme), attrs);
        initLinearLayout(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.LinearLayout, defStyleAttr, R.styleable.LinearLayout_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initLinearLayout(attrs, defStyleAttr);
    }

    private static int[] rippleIds = new int[]{
            R.styleable.LinearLayout_carbon_rippleColor,
            R.styleable.LinearLayout_carbon_rippleStyle,
            R.styleable.LinearLayout_carbon_rippleHotspot,
            R.styleable.LinearLayout_carbon_rippleRadius
    };
    private static int[] animationIds = new int[]{
            R.styleable.LinearLayout_carbon_inAnimation,
            R.styleable.LinearLayout_carbon_outAnimation
    };
    private static int[] touchMarginIds = new int[]{
            R.styleable.LinearLayout_carbon_touchMargin,
            R.styleable.LinearLayout_carbon_touchMarginLeft,
            R.styleable.LinearLayout_carbon_touchMarginTop,
            R.styleable.LinearLayout_carbon_touchMarginRight,
            R.styleable.LinearLayout_carbon_touchMarginBottom
    };
    private static int[] insetIds = new int[]{
            R.styleable.LinearLayout_carbon_inset,
            R.styleable.LinearLayout_carbon_insetLeft,
            R.styleable.LinearLayout_carbon_insetTop,
            R.styleable.LinearLayout_carbon_insetRight,
            R.styleable.LinearLayout_carbon_insetBottom,
            R.styleable.LinearLayout_carbon_insetColor
    };
    private static int[] maxSizeIds = new int[]{
            R.styleable.LinearLayout_carbon_maxWidth,
            R.styleable.LinearLayout_carbon_maxHeight,
    };
    private static int[] elevationIds = new int[]{
            R.styleable.LinearLayout_carbon_elevation,
            R.styleable.LinearLayout_carbon_elevationShadowColor
    };

    private void initLinearLayout(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LinearLayout, defStyleAttr, R.style.carbon_LinearLayout);

        Carbon.initRippleDrawable(this, a, rippleIds);
        Carbon.initElevation(this, a, elevationIds);
        Carbon.initAnimations(this, a, animationIds);
        Carbon.initTouchMargin(this, a, touchMarginIds);
        Carbon.initInset(this, a, insetIds);
        Carbon.initMaxSize(this, a, maxSizeIds);
        setCornerRadius(a.getDimension(R.styleable.LinearLayout_carbon_cornerRadius, 0));

        a.recycle();

        setChildrenDrawingOrderEnabled(true);
        setClipToPadding(false);
    }

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private boolean drawCalled = false;
    Reveal reveal;

    @Override
    public Animator startReveal(int x, int y, float startRadius, float finishRadius) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            android.animation.Animator circularReveal = ViewAnimationUtils.createCircularReveal(this, x, y, startRadius, finishRadius);
            circularReveal.start();
            return new Animator() {
                @Override
                public long getStartDelay() {
                    return circularReveal.getStartDelay();
                }

                @Override
                public void setStartDelay(long startDelay) {
                    circularReveal.setStartDelay(startDelay);
                }

                @Override
                public Animator setDuration(long duration) {
                    circularReveal.setDuration(duration);
                    return this;
                }

                @Override
                public long getDuration() {
                    return circularReveal.getDuration();
                }

                @Override
                public void setInterpolator(TimeInterpolator value) {
                    circularReveal.setInterpolator(value);
                }

                @Override
                public boolean isRunning() {
                    return circularReveal.isRunning();
                }
            };
        } else {
            reveal = new Reveal(x, y, startRadius);
            ValueAnimator animator = ValueAnimator.ofFloat(startRadius, finishRadius);
            animator.setDuration(Carbon.getDefaultRevealDuration());
            animator.addUpdateListener(animation -> {
                reveal.radius = (float) animation.getAnimatedValue();
                reveal.mask.reset();
                reveal.mask.addCircle(reveal.x, reveal.y, Math.max(reveal.radius, 1), Path.Direction.CW);
                postInvalidate();
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    reveal = null;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    reveal = null;
                }
            });
            animator.start();
            return animator;
        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        boolean r = reveal != null;
        boolean c = cornerRadius > 0;
        // draw not called, we have to handle corners here
        if (!drawCalled && (r || c) && getWidth() > 0 && getHeight() > 0 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.save(Canvas.CLIP_SAVE_FLAG);
                canvas.clipRect(reveal.x - reveal.radius, reveal.y - reveal.radius, reveal.x + reveal.radius, reveal.y + reveal.radius);
                internalDispatchDraw(canvas);
                canvas.restoreToCount(saveCount2);
            } else {
                internalDispatchDraw(canvas);
            }

            paint.setXfermode(Carbon.CLEAR_MODE);
            if (c)
                canvas.drawPath(cornersMask, paint);
            if (r)
                canvas.drawPath(reveal.mask, paint);
            paint.setXfermode(null);

            canvas.restoreToCount(saveCount);
        } else {
            internalDispatchDraw(canvas);
        }
        drawCalled = false;
    }

    private void internalDispatchDraw(@NonNull Canvas canvas) {
        Collections.sort(getViews(), new ElevationComparator());

        super.dispatchDraw(canvas);
        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
        if (insetColor != 0) {
            paint.setColor(insetColor);
            paint.setAlpha(255);
            if (insetLeft != 0)
                canvas.drawRect(0, 0, insetLeft, getHeight(), paint);
            if (insetTop != 0)
                canvas.drawRect(0, 0, getWidth(), insetTop, paint);
            if (insetRight != 0)
                canvas.drawRect(getWidth() - insetRight, 0, getWidth(), getHeight(), paint);
            if (insetBottom != 0)
                canvas.drawRect(0, getHeight() - insetBottom, getWidth(), getHeight(), paint);
        }
    }

    @Override
    protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        // TODO: why isShown() returns false after being reattached?
        if (child instanceof ShadowView && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH || ((ShadowView) child).getElevationShadowColor() != null)) {
            ShadowView shadowView = (ShadowView) child;
            shadowView.drawShadow(canvas);
        }

        if (child instanceof RippleView) {
            RippleView rippleView = (RippleView) child;
            RippleDrawable rippleDrawable = rippleView.getRippleDrawable();
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless) {
                int saveCount = canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.translate(child.getLeft(), child.getTop());
                canvas.concat(child.getMatrix());
                rippleDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            }
        }

        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int child) {
        return views != null ? indexOfChild(views.get(child)) : child;
    }

    protected boolean isTransformedTouchPointInView(float x, float y, View child, PointF outLocalPoint) {
        final Rect frame = new Rect();
        child.getHitRect(frame);
        return frame.contains((int) x, (int) y);
    }


    // -------------------------------
    // corners
    // -------------------------------

    private float cornerRadius;
    private Path cornersMask;

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidateShadow();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutAnchoredViews();

        if (!changed)
            return;

        invalidateShadow();

        if (getWidth() == 0 || getHeight() == 0)
            return;

        initCorners();

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());

        percentLayoutHelper.restoreOriginalParams();
    }

    private void initCorners() {
        if (cornerRadius > 0) {
            cornerRadius = Math.min(cornerRadius, Math.min(getWidth(), getHeight()) / 2.0f);
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
        drawCalled = true;
        boolean r = reveal != null;
        boolean c = cornerRadius > 0;
        if ((r || c) && getWidth() > 0 && getHeight() > 0 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.save(Canvas.CLIP_SAVE_FLAG);
                canvas.clipRect(reveal.x - reveal.radius, reveal.y - reveal.radius, reveal.x + reveal.radius, reveal.y + reveal.radius);
                super.draw(canvas);
                canvas.restoreToCount(saveCount2);
            } else {
                super.draw(canvas);
            }

            paint.setXfermode(Carbon.CLEAR_MODE);
            if (c)
                canvas.drawPath(cornersMask, paint);
            if (r)
                canvas.drawPath(reveal.mask, paint);
            paint.setXfermode(null);

            canvas.restoreToCount(saveCount);
        } else {
            super.draw(canvas);
        }
    }


    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;
    private Transformation t = new Transformation();

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        Animation a = getAnimation();
        if (a != null) {
            a.getTransformation(event.getEventTime(), t);
            float[] loc = new float[]{event.getX(), event.getY()};
            t.getMatrix().mapPoints(loc);
            event.setLocation(loc[0], loc[1]);
        }
        if (onDispatchTouchListener != null && onDispatchTouchListener.onTouch(this, event))
            return true;

        if (rippleDrawable != null && event.getAction() == MotionEvent.ACTION_DOWN)
            rippleDrawable.setHotspot(event.getX(), event.getY());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }

    @Override
    public void setRippleDrawable(RippleDrawable newRipple) {
        if (rippleDrawable != null) {
            rippleDrawable.setCallback(null);
            if (rippleDrawable.getStyle() == RippleDrawable.Style.Background)
                super.setBackgroundDrawable(rippleDrawable.getBackground());
        }

        if (newRipple != null) {
            newRipple.setCallback(this);
            newRipple.setBounds(0, 0, getWidth(), getHeight());
            if (newRipple.getStyle() == RippleDrawable.Style.Background)
                super.setBackgroundDrawable((Drawable) newRipple);
        }

        rippleDrawable = newRipple;
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
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
        super.setBackgroundDrawable(background);
    }


    // -------------------------------
    // elevation
    // -------------------------------

    private float elevation = 0;
    private float translationZ = 0;
    private Shadow shadow;
    private ColorStateList shadowColor;
    private PorterDuffColorFilter shadowColorFilter;
    private RectF shadowMaskRect = new RectF();

    @Override
    public float getElevation() {
        return elevation;
    }

    @Override
    public synchronized void setElevation(float elevation) {
        if (elevation == this.elevation)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            super.setElevation(shadowColor == null ? elevation : 0);
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
            super.setTranslationZ(shadowColor == null ? translationZ : 0);
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
    }

    @Override
    public boolean hasShadow() {
        return getElevation() + getTranslationZ() >= 0.01f && getWidth() > 0 && getHeight() > 0;
    }

    @Override
    public void drawShadow(Canvas canvas) {
        float alpha = getAlpha() * Carbon.getDrawableAlpha(getBackground()) / 255.0f * Carbon.getBackgroundTintAlpha(this) / 255.0f;
        if (alpha == 0)
            return;

        if (!hasShadow())
            return;

        float z = getElevation() + getTranslationZ();
        if (shadow == null || shadow.elevation != z)
            shadow = ShadowGenerator.generateShadow(this, z);

        int saveCount = 0;
        boolean maskShadow = getBackground() != null && alpha != 1;
        if (maskShadow)
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        paint.setAlpha((int) (Shadow.ALPHA * alpha));

        Matrix matrix = getMatrix();

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(this.getLeft(), this.getTop() + z / 2);
        canvas.concat(matrix);
        shadow.draw(canvas, this, paint, shadowColorFilter);
        canvas.restore();

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(this.getLeft(), this.getTop());
        canvas.concat(matrix);
        shadow.draw(canvas, this, paint, shadowColorFilter);
        canvas.restore();

        if (maskShadow) {
            canvas.translate(this.getLeft(), this.getTop());
            canvas.concat(matrix);
            paint.setXfermode(Carbon.CLEAR_MODE);
            shadowMaskRect.set(0, 0, getWidth(), getHeight());
            canvas.drawRoundRect(shadowMaskRect, cornerRadius, cornerRadius, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    public void invalidateShadow() {
        shadow = null;
        if (getParent() != null && getParent() instanceof View)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public void setElevationShadowColor(ColorStateList shadowColor) {
        this.shadowColor = shadowColor;
        shadowColorFilter = shadowColor != null ? new PorterDuffColorFilter(shadowColor.getColorForState(getDrawableState(), shadowColor.getDefaultColor()), PorterDuff.Mode.MULTIPLY) : Shadow.DEFAULT_FILTER;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            super.setElevation(shadowColor == null ? elevation : 0);
    }

    @Override
    public void setElevationShadowColor(int color) {
        shadowColor = ColorStateList.valueOf(color);
        shadowColorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            super.setElevation(0);
    }

    @Override
    public ColorStateList getElevationShadowColor() {
        return shadowColor;
    }


    // -------------------------------
    // touch margin
    // -------------------------------

    private Rect touchMargin = new Rect();

    @Override
    public void setTouchMargin(int left, int top, int right, int bottom) {
        touchMargin.set(left, top, right, bottom);
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
                        LinearLayout.super.setVisibility(visibility);
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
    // insets
    // -------------------------------

    int insetLeft = INSET_NULL, insetTop = INSET_NULL, insetRight = INSET_NULL, insetBottom = INSET_NULL;
    int insetColor;
    private OnInsetsChangedListener onInsetsChangedListener;

    public int getInsetColor() {
        return insetColor;
    }

    public void setInsetColor(int insetsColor) {
        this.insetColor = insetsColor;
    }

    public void setInset(int left, int top, int right, int bottom) {
        insetLeft = left;
        insetTop = top;
        insetRight = right;
        insetBottom = bottom;
    }

    public int getInsetLeft() {
        return insetLeft;
    }

    public void setInsetLeft(int insetLeft) {
        this.insetLeft = insetLeft;
    }

    public int getInsetTop() {
        return insetTop;
    }

    public void setInsetTop(int insetTop) {
        this.insetTop = insetTop;
    }

    public int getInsetRight() {
        return insetRight;
    }

    public void setInsetRight(int insetRight) {
        this.insetRight = insetRight;
    }

    public int getInsetBottom() {
        return insetBottom;
    }

    public void setInsetBottom(int insetBottom) {
        this.insetBottom = insetBottom;
    }

    @Override
    protected boolean fitSystemWindows(@NonNull Rect insets) {
        if (insetLeft == INSET_NULL)
            insetLeft = insets.left;
        if (insetTop == INSET_NULL)
            insetTop = insets.top;
        if (insetRight == INSET_NULL)
            insetRight = insets.right;
        if (insetBottom == INSET_NULL)
            insetBottom = insets.bottom;
        insets.set(insetLeft, insetTop, insetRight, insetBottom);
        if (onInsetsChangedListener != null)
            onInsetsChangedListener.onInsetsChanged();
        postInvalidate();
        return super.fitSystemWindows(insets);
    }

    public void setOnInsetsChangedListener(OnInsetsChangedListener onInsetsChangedListener) {
        this.onInsetsChangedListener = onInsetsChangedListener;
    }


    // -------------------------------
    // ViewGroup utils
    // -------------------------------

    List<View> views = new ArrayList<>();

    public List<View> getViews() {
        views.clear();
        for (int i = 0; i < getChildCount(); i++)
            views.add(getChildAt(i));
        return views;
    }

    public void setOnDispatchTouchListener(OnTouchListener onDispatchTouchListener) {
        this.onDispatchTouchListener = onDispatchTouchListener;
    }

    public Component findComponentById(int id) {
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(this);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof ComponentView && ((ComponentView) child).getComponent().getView().getId() == id)
                    return ((ComponentView) child).getComponent();
                if (child instanceof ViewGroup)
                    groups.add((ViewGroup) child);
            }
        }
        return null;
    }

    public List<Component> findComponentsById(int id) {
        List<Component> result = new ArrayList<>();
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(this);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof ComponentView && ((ComponentView) child).getComponent().getView().getId() == id)
                    result.add(((ComponentView) child).getComponent());
                if (child instanceof ViewGroup)
                    groups.add((ViewGroup) child);
            }
        }
        return result;
    }

    public Component findComponentOfType(Class type) {
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(this);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof ComponentView && ((ComponentView) child).getComponent().getClass().equals(type))
                    return ((ComponentView) child).getComponent();
                if (child instanceof ViewGroup)
                    groups.add((ViewGroup) child);
            }
        }
        return null;
    }

    public List<Component> findComponentsOfType(Class type) {
        List<Component> result = new ArrayList<>();
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(this);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof ComponentView && ((ComponentView) child).getComponent().getClass().equals(type))
                    result.add(((ComponentView) child).getComponent());
                if (child instanceof ViewGroup)
                    groups.add((ViewGroup) child);
            }
        }
        return result;
    }

    public View findViewOfType(Class type) {
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(this);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child.getClass().equals(type))
                    return child;
                if (child instanceof ViewGroup)
                    groups.add((ViewGroup) child);
            }
        }
        return null;
    }

    public List<View> findViewsOfType(Class type) {
        List<View> result = new ArrayList<>();
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(this);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child.getClass().equals(type))
                    result.add(child);
                if (child instanceof ViewGroup)
                    groups.add((ViewGroup) child);
            }
        }
        return result;
    }

    public List<View> findViewsById(int id) {
        List<View> result = new ArrayList<>();
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(this);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child.getId() == id)
                    result.add(child);
                if (child instanceof ViewGroup)
                    groups.add((ViewGroup) child);
            }
        }
        return result;
    }

    public List<View> findViewsWithTag(Object tag) {
        List<View> result = new ArrayList<>();
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(this);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (tag.equals(child.getTag()))
                    result.add(child);
                if (child instanceof ViewGroup)
                    groups.add((ViewGroup) child);
            }
        }
        return result;
    }


    // -------------------------------
    // layout params
    // -------------------------------

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    private void layoutAnchoredViews() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.anchorView != 0) {
                    View anchorView = findViewById(lp.anchorView);
                    if (anchorView != null && anchorView != child) {
                        int left = child.getLeft();
                        int right = child.getRight();
                        int top = child.getTop();
                        int bottom = child.getBottom();
                        if ((lp.anchorGravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
                            top = anchorView.getBottom() - lp.height / 2;
                            bottom = top + lp.height;
                        }
                        if ((lp.anchorGravity & Gravity.TOP) == Gravity.TOP) {
                            top = anchorView.getTop() - lp.height / 2;
                            bottom = top + lp.height;
                        }
                        if ((GravityCompat.getAbsoluteGravity(lp.anchorGravity, ViewCompat.getLayoutDirection(child)) & Gravity.LEFT) == Gravity.LEFT) {
                            left = anchorView.getLeft() - lp.width / 2;
                            right = left + lp.width;
                        }
                        if ((GravityCompat.getAbsoluteGravity(lp.anchorGravity, ViewCompat.getLayoutDirection(child)) & Gravity.RIGHT) == Gravity.RIGHT) {
                            left = anchorView.getRight() - lp.width / 2;
                            right = left + lp.width;
                        }
                        child.layout(left, top, right, bottom);
                    }
                }
            }
        }
    }

    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams implements PercentLayoutHelper.PercentLayoutParams {
        private PercentLayoutHelper.PercentLayoutInfo percentLayoutInfo;
        private int anchorView;
        private int anchorGravity;
        private RuntimeException delayedException;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.LinearLayout_Layout);
            anchorView = a.getResourceId(R.styleable.LinearLayout_Layout_carbon_anchor, -1);
            anchorGravity = a.getInt(R.styleable.LinearLayout_Layout_carbon_anchorGravity, -1);
            a.recycle();

            if (delayedException != null) {
                percentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);

                if ((percentLayoutInfo.widthPercent == -1.0f || percentLayoutInfo.heightPercent == -1.0f) && percentLayoutInfo.aspectRatio == -1 ||
                        (percentLayoutInfo.widthPercent == -1.0f && percentLayoutInfo.heightPercent == -1.0f))
                    throw delayedException;
            }
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.widget.LinearLayout.LayoutParams source) {
            super((MarginLayoutParams) source);
            gravity = source.gravity;
        }

        public LayoutParams(LayoutParams source) {
            super((MarginLayoutParams) source);

            this.anchorView = source.anchorView;
            this.anchorGravity = source.anchorGravity;
            percentLayoutInfo = source.percentLayoutInfo;
        }

        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            try {
                super.setBaseAttributes(a, widthAttr, heightAttr);
            } catch (RuntimeException e) {
                delayedException = e;
            }
        }

        @Override
        public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo() {
            if (percentLayoutInfo == null) {
                percentLayoutInfo = new PercentLayoutHelper.PercentLayoutInfo();
            }

            return percentLayoutInfo;
        }

        public int getAnchorGravity() {
            return anchorGravity;
        }

        public void setAnchorGravity(int anchorGravity) {
            this.anchorGravity = anchorGravity;
        }

        public int getAnchorView() {
            return anchorView;
        }

        public void setAnchorView(int anchorView) {
            this.anchorView = anchorView;
        }
    }


    // -------------------------------
    // maximum width & height
    // -------------------------------

    int maxWidth = Integer.MAX_VALUE, maxHeight = Integer.MAX_VALUE;

    @Override
    public int getMaximumWidth() {
        return maxWidth;
    }

    @Override
    public void setMaximumWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        requestLayout();
    }

    @Override
    public int getMaximumHeight() {
        return maxHeight;
    }

    @Override
    public void setMaximumHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        percentLayoutHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (percentLayoutHelper.handleMeasuredStateTooSmall())
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > maxWidth || getMeasuredHeight() > maxHeight) {
            if (getMeasuredWidth() > maxWidth)
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
            if (getMeasuredHeight() > maxHeight)
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
