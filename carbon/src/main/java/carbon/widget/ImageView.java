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
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedColorStateList;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.drawable.DefaultPrimaryColorStateList;
import carbon.drawable.VectorDrawable;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.RevealAnimator;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowShape;
import carbon.shadow.ShadowView;

public class ImageView extends android.widget.ImageView
        implements ShadowView, RippleView, TouchMarginView, StateAnimatorView, AnimatedView, CornerView, TintedView, StrokeView, RevealView, VisibleView {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public ImageView(Context context) {
        super(context, null, R.attr.carbon_imageViewStyle);
        initImageView(null, R.attr.carbon_imageViewStyle);
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ImageView, R.attr.carbon_imageViewStyle, R.styleable.ImageView_carbon_theme), attrs, R.attr.carbon_imageViewStyle);
        initImageView(attrs, R.attr.carbon_imageViewStyle);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ImageView, defStyleAttr, R.styleable.ImageView_carbon_theme), attrs, defStyleAttr);
        initImageView(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ImageView, defStyleAttr, R.styleable.ImageView_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initImageView(attrs, defStyleAttr);
    }

    private static int[] rippleIds = new int[]{
            R.styleable.ImageView_carbon_rippleColor,
            R.styleable.ImageView_carbon_rippleStyle,
            R.styleable.ImageView_carbon_rippleHotspot,
            R.styleable.ImageView_carbon_rippleRadius
    };
    private static int[] animationIds = new int[]{
            R.styleable.ImageView_carbon_inAnimation,
            R.styleable.ImageView_carbon_outAnimation
    };
    private static int[] touchMarginIds = new int[]{
            R.styleable.ImageView_carbon_touchMargin,
            R.styleable.ImageView_carbon_touchMarginLeft,
            R.styleable.ImageView_carbon_touchMarginTop,
            R.styleable.ImageView_carbon_touchMarginRight,
            R.styleable.ImageView_carbon_touchMarginBottom
    };
    private static int[] tintIds = new int[]{
            R.styleable.ImageView_carbon_tint,
            R.styleable.ImageView_carbon_tintMode,
            R.styleable.ImageView_carbon_backgroundTint,
            R.styleable.ImageView_carbon_backgroundTintMode,
            R.styleable.ImageView_carbon_animateColorChanges
    };
    private static int[] strokeIds = new int[]{
            R.styleable.ImageView_carbon_stroke,
            R.styleable.ImageView_carbon_strokeWidth
    };
    private static int[] elevationIds = new int[]{
            R.styleable.ImageView_carbon_elevation,
            R.styleable.ImageView_carbon_elevationShadowColor
    };

    private void initImageView(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImageView, defStyleAttr, R.style.carbon_ImageView);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ImageView_android_src) {
                int resId = a.getResourceId(attr, 0);
                if (resId != 0 && getContext().getResources().getResourceTypeName(resId).equals("raw")) {
                    if (!isInEditMode()) {
                        setImageDrawable(new VectorDrawable(getResources(), resId));
                    } else {
                        setImageResource(R.drawable.carbon_iconplaceholder);
                    }
                }
            } else if (attr == R.styleable.ImageView_android_enabled) {
                setEnabled(a.getBoolean(attr, true));
            } else if (attr == R.styleable.ImageView_carbon_cornerRadius) {
                setCornerRadius(a.getDimension(attr, 0));
            }
        }

        new AppCompatImageHelper(this).loadFromAttributes(attrs, defStyleAttr);

        Carbon.initElevation(this, a, elevationIds);
        Carbon.initRippleDrawable(this, a, rippleIds);
        Carbon.initAnimations(this, a, animationIds);
        Carbon.initTouchMargin(this, a, touchMarginIds);
        Carbon.initTint(this, a, tintIds);
        Carbon.initStroke(this, a, strokeIds);

        a.recycle();
    }

    @Override
    public void setImageResource(int resId) {
        if (resId != 0 && getContext().getResources().getResourceTypeName(resId).equals("raw")) {
            setImageDrawable(new VectorDrawable(getResources(), resId));
        } else {
            super.setImageResource(resId);
        }
    }

    RevealAnimator revealAnimator;

    @Override
    public Animator startReveal(int x, int y, float startRadius, float finishRadius) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(this, x, y, startRadius, finishRadius);
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
            revealAnimator = new RevealAnimator(x, y, startRadius, finishRadius);
            revealAnimator.setDuration(Carbon.getDefaultRevealDuration());
            revealAnimator.addUpdateListener(animation -> {
                RevealAnimator reveal = ((RevealAnimator) animation);
                reveal.radius = (float) reveal.getAnimatedValue();
                reveal.mask.reset();
                reveal.mask.addCircle(reveal.x, reveal.y, Math.max((Float) reveal.getAnimatedValue(), 1), Path.Direction.CW);
                postInvalidate();
            });
            revealAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    revealAnimator = null;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    revealAnimator = null;
                }
            });
            revealAnimator.start();
            return revealAnimator;
        }
    }


    // -------------------------------
    // corners
    // -------------------------------

    private float cornerRadius;
    private Path cornersMask;

    /**
     * Gets the corner radius. If corner radius is equal to 0, rounded corners are turned off.
     *
     * @return corner radius, equal to or greater than 0.
     */
    public float getCornerRadius() {
        return cornerRadius;
    }

    /**
     * Sets the corner radius. If corner radius is equal to 0, rounded corners are turned off.
     *
     * @param cornerRadius
     */
    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidateShadow();
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
        if (cornerRadius > 0 && getWidth() > 0 && getHeight() > 0 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            super.draw(canvas);
            if (stroke != null)
                drawStroke(canvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(canvas);

            paint.setXfermode(Carbon.CLEAR_MODE);
            canvas.drawPath(cornersMask, paint);

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            super.draw(canvas);
            if (stroke != null)
                drawStroke(canvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(canvas);
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
        if (a != null && a.hasStarted()) {
            a.getTransformation(getDrawingTime(), t);
            float[] loc = new float[]{event.getX(), event.getY()};
            //t.getMatrix().mapPoints(loc);
            loc[0] -= getTranslationX();
            loc[1] -= getTranslationY();
            event.setLocation(loc[0], loc[1]);
            // Log.e("mapped loc", "" + loc[0] + ", " + loc[1]);
        }
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
        updateBackgroundTint();
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
        boolean r = revealAnimator != null && revealAnimator.isRunning();
        if (maskShadow) {
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        } else if (r) {
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            canvas.clipRect(getLeft() + revealAnimator.x - revealAnimator.radius, getTop() + revealAnimator.y - revealAnimator.radius, getLeft() + revealAnimator.x + revealAnimator.radius, getTop() + revealAnimator.y + revealAnimator.radius);
        }

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

        if (saveCount != 0) {
            canvas.translate(this.getLeft(), this.getTop());
            canvas.concat(matrix);
            paint.setXfermode(Carbon.CLEAR_MODE);
        }
        if (maskShadow) {
            shadowMaskRect.set(0, 0, getWidth(), getHeight());
            canvas.drawRoundRect(shadowMaskRect, cornerRadius, cornerRadius, paint);
        }
        if (r) {
            canvas.drawPath(revealAnimator.mask, paint);
        }
        if (saveCount != 0) {
            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
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
            Animation a = getAnimation();
            if (a != null && a.hasStarted()) {
                a.getTransformation(System.currentTimeMillis(), t);
                float[] loc = new float[]{outRect.left, outRect.top, outRect.right, outRect.bottom};
                //t.getMatrix().mapPoints(loc);
                loc[0] += getTranslationX();
                loc[1] += getTranslationY();
                loc[2] += getTranslationX();
                loc[3] += getTranslationY();
                outRect.set((int) loc[0], (int) loc[1], (int) loc[2], (int) loc[3]);
            }
            return;
        }
        outRect.set(getLeft() - touchMargin.left, getTop() - touchMargin.top, getRight() + touchMargin.right, getBottom() + touchMargin.bottom);
        Animation a = getAnimation();
        if (a != null && a.hasStarted()) {
            a.getTransformation(System.currentTimeMillis(), t);
            float[] loc = new float[]{outRect.left, outRect.top, outRect.right, outRect.bottom};
            //t.getMatrix().mapPoints(loc);
            loc[0] += getTranslationX();
            loc[1] += getTranslationY();
            loc[2] += getTranslationX();
            loc[3] += getTranslationY();
            outRect.set((int) loc[0], (int) loc[1], (int) loc[2], (int) loc[3]);
        }
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
        if (tint != null && tint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) tint).setState(getDrawableState());
        if (backgroundTint != null && backgroundTint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) backgroundTint).setState(getDrawableState());
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
                        ImageView.super.setVisibility(visibility);
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
    PorterDuff.Mode tintMode;
    ColorStateList backgroundTint;
    PorterDuff.Mode backgroundTintMode;
    boolean animateColorChanges;
    ValueAnimator.AnimatorUpdateListener tintAnimatorListener = animation -> {
        updateTint();
        ViewCompat.postInvalidateOnAnimation(ImageView.this);
    };
    ValueAnimator.AnimatorUpdateListener backgroundTintAnimatorListener = animation -> {
        updateBackgroundTint();
        ViewCompat.postInvalidateOnAnimation(ImageView.this);
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
            setColorFilter(new PorterDuffColorFilter(color, tintMode));
        } else {
            setColorFilter(null);
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
            getBackground().setColorFilter(new PorterDuffColorFilter(color, backgroundTintMode));
        } else {
            getBackground().setColorFilter(null);
        }
    }

    @Override
    public void setBackgroundTintMode(@NonNull PorterDuff.Mode mode) {
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


    // -------------------------------
    // stroke
    // -------------------------------

    private ColorStateList stroke;
    private float strokeWidth;
    private Paint strokePaint;
    private RectF strokeRect;

    private void drawStroke(Canvas canvas) {
        strokePaint.setStrokeWidth(strokeWidth * 2);
        strokePaint.setColor(stroke.getColorForState(getDrawableState(), stroke.getDefaultColor()));
        strokeRect.set(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(strokeRect, cornerRadius, cornerRadius, strokePaint);
    }

    @Override
    public void setStroke(ColorStateList colorStateList) {
        stroke = colorStateList;

        if (stroke == null)
            return;

        if (strokePaint == null) {
            strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokeRect = new RectF();
        }
    }

    @Override
    public void setStroke(int color) {
        setStroke(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getStroke() {
        return stroke;
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Override
    public float getStrokeWidth() {
        return strokeWidth;
    }
}
