package carbon.widget;

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
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

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
import carbon.internal.MatrixHelper;
import carbon.internal.PercentLayoutHelper;
import carbon.internal.Reveal;
import carbon.recycler.Component;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowShape;
import carbon.shadow.ShadowView;

import static com.nineoldandroids.view.animation.AnimatorProxy.NEEDS_PROXY;
import static com.nineoldandroids.view.animation.AnimatorProxy.wrap;

/**
 * Created by Marcin on 2014-11-20.
 * <p/>
 * FlowLayout layouts its children from left to right, top to bottom.
 * Has support for material features including shadows, ripples, rounded
 * corners, insets, custom drawing order, touch margins, state animators and others.
 */
public class FlowLayout extends android.widget.FrameLayout implements ShadowView, RippleView, TouchMarginView, StateAnimatorView, AnimatedView, InsetView, CornerView, MaxSizeView, RevealView {
    private final PercentLayoutHelper percentLayoutHelper = new PercentLayoutHelper(this);
    private OnTouchListener onDispatchTouchListener;

    public FlowLayout(Context context) {
        super(context, null, R.attr.carbon_flowLayoutStyle);
        initFlowLayout(null, R.attr.carbon_flowLayoutStyle);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.FlowLayout, R.attr.carbon_flowLayoutStyle, R.styleable.FlowLayout_carbon_theme), attrs, R.attr.carbon_flowLayoutStyle);
        initFlowLayout(attrs, R.attr.carbon_flowLayoutStyle);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.FlowLayout, defStyleAttr, R.styleable.FlowLayout_carbon_theme), attrs, defStyleAttr);
        initFlowLayout(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.FlowLayout, defStyleAttr, R.styleable.FlowLayout_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initFlowLayout(attrs, defStyleAttr);
    }

    private static int[] rippleIds = new int[]{
            R.styleable.FrameLayout_carbon_rippleColor,
            R.styleable.FrameLayout_carbon_rippleStyle,
            R.styleable.FrameLayout_carbon_rippleHotspot,
            R.styleable.FrameLayout_carbon_rippleRadius
    };
    private static int[] animationIds = new int[]{
            R.styleable.FrameLayout_carbon_inAnimation,
            R.styleable.FrameLayout_carbon_outAnimation
    };
    private static int[] touchMarginIds = new int[]{
            R.styleable.FlowLayout_carbon_touchMargin,
            R.styleable.FlowLayout_carbon_touchMarginLeft,
            R.styleable.FlowLayout_carbon_touchMarginTop,
            R.styleable.FlowLayout_carbon_touchMarginRight,
            R.styleable.FlowLayout_carbon_touchMarginBottom
    };
    private static int[] insetIds = new int[]{
            R.styleable.FlowLayout_carbon_inset,
            R.styleable.FlowLayout_carbon_insetLeft,
            R.styleable.FlowLayout_carbon_insetTop,
            R.styleable.FlowLayout_carbon_insetRight,
            R.styleable.FlowLayout_carbon_insetBottom,
            R.styleable.FlowLayout_carbon_insetColor
    };
    private static int[] maxSizeIds = new int[]{
            R.styleable.FlowLayout_carbon_maxWidth,
            R.styleable.FlowLayout_carbon_maxHeight,
    };
    private static int[] elevationIds = new int[]{
            R.styleable.FlowLayout_carbon_elevation,
            R.styleable.FlowLayout_carbon_elevationShadowColor
    };

    private void initFlowLayout(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FlowLayout, defStyleAttr, R.style.carbon_FlowLayout);

        Carbon.initRippleDrawable(this, a, rippleIds);
        Carbon.initElevation(this, a, elevationIds);
        Carbon.initAnimations(this, a, animationIds);
        Carbon.initTouchMargin(this, a, touchMarginIds);
        Carbon.initInset(this, a, insetIds);
        Carbon.initMaxSize(this, a, maxSizeIds);
        setCornerRadius(a.getDimension(R.styleable.FlowLayout_carbon_cornerRadius, 0));

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
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                public long getStartDelay() {
                    return circularReveal.getStartDelay();
                }

                @Override
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                public void setStartDelay(long startDelay) {
                    circularReveal.setStartDelay(startDelay);
                }

                @Override
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                public Animator setDuration(long duration) {
                    circularReveal.setDuration(duration);
                    return this;
                }

                @Override
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                public long getDuration() {
                    return circularReveal.getDuration();
                }

                @Override
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                public void setInterpolator(Interpolator value) {
                    circularReveal.setInterpolator(value);
                }

                @Override
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
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
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.FULL_COLOR_LAYER_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.CLIP_SAVE_FLAG);
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

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
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

    RectF childRect = new RectF();

    @Override
    protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
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
                canvas.concat(MatrixHelper.getMatrix(child));
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

    private void layoutFlowingViews() {
        int width = getWidth();
        int currentX = getPaddingLeft(), currentY = getPaddingTop();
        int nextY = getPaddingTop();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (child.getVisibility() != GONE) {
                if (currentX != 0 && currentX + params.leftMargin + child.getMeasuredWidth() + params.rightMargin > width - getPaddingLeft() - getPaddingRight()) {
                    currentX = getPaddingLeft();
                    currentY = nextY;
                }
                child.layout(currentX + params.leftMargin, currentY + params.topMargin, currentX + params.leftMargin + child.getMeasuredWidth(), currentY + params.topMargin + child.getMeasuredHeight());
                currentX += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
                nextY = Math.max(nextY, currentY + params.topMargin + child.getMeasuredHeight() + params.bottomMargin);
            }
        }
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
        layoutFlowingViews();

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
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.FULL_COLOR_LAYER_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.CLIP_SAVE_FLAG);
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

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            super.draw(canvas);
        }
    }


    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
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

        Matrix matrix = MatrixHelper.getMatrix(this);

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
                        FlowLayout.super.setVisibility(visibility);
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

    public static class LayoutParams extends android.widget.FrameLayout.LayoutParams implements PercentLayoutHelper.PercentLayoutParams {
        private PercentLayoutHelper.PercentLayoutInfo percentLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            percentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
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

        public LayoutParams(android.widget.FrameLayout.LayoutParams source) {
            super((MarginLayoutParams) source);
            gravity = source.gravity;
        }

        public LayoutParams(LayoutParams source) {
            super((MarginLayoutParams) source);

            percentLayoutInfo = source.percentLayoutInfo;
        }

        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            try {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } catch (RuntimeException e) {
                if (!a.hasValue(R.styleable.Carbon_carbon_widthPercent))
                    throw e;
            }
            try {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } catch (RuntimeException e) {
                if (!a.hasValue(R.styleable.Carbon_carbon_widthPercent))
                    throw e;
            }
        }

        @Override
        public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo() {
            if (percentLayoutInfo == null) {
                percentLayoutInfo = new PercentLayoutHelper.PercentLayoutInfo();
            }

            return percentLayoutInfo;
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


    // -------------------------------
    // transformations
    // -------------------------------

    public float getAlpha() {
        return NEEDS_PROXY ? wrap(this).getAlpha() : super.getAlpha();
    }

    public void setAlpha(float alpha) {
        if (NEEDS_PROXY) {
            wrap(this).setAlpha(alpha);
        } else {
            super.setAlpha(alpha);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getPivotX() {
        return NEEDS_PROXY ? wrap(this).getPivotX() : super.getPivotX();
    }

    public void setPivotX(float pivotX) {
        if (NEEDS_PROXY) {
            wrap(this).setPivotX(pivotX);
        } else {
            super.setPivotX(pivotX);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getPivotY() {
        return NEEDS_PROXY ? wrap(this).getPivotY() : super.getPivotY();
    }

    public void setPivotY(float pivotY) {
        if (NEEDS_PROXY) {
            wrap(this).setPivotY(pivotY);
        } else {
            super.setPivotY(pivotY);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getRotation() {
        return NEEDS_PROXY ? wrap(this).getRotation() : super.getRotation();
    }

    public void setRotation(float rotation) {
        if (NEEDS_PROXY) {
            wrap(this).setRotation(rotation);
        } else {
            super.setRotation(rotation);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getRotationX() {
        return NEEDS_PROXY ? wrap(this).getRotationX() : super.getRotationX();
    }

    public void setRotationX(float rotationX) {
        if (NEEDS_PROXY) {
            wrap(this).setRotationX(rotationX);
        } else {
            super.setRotationX(rotationX);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getRotationY() {
        return NEEDS_PROXY ? wrap(this).getRotationY() : super.getRotationY();
    }

    public void setRotationY(float rotationY) {
        if (NEEDS_PROXY) {
            wrap(this).setRotationY(rotationY);
        } else {
            super.setRotationY(rotationY);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getScaleX() {
        return NEEDS_PROXY ? wrap(this).getScaleX() : super.getScaleX();
    }

    public void setScaleX(float scaleX) {
        if (NEEDS_PROXY) {
            wrap(this).setScaleX(scaleX);
        } else {
            super.setScaleX(scaleX);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getScaleY() {
        return NEEDS_PROXY ? wrap(this).getScaleY() : super.getScaleY();
    }

    public void setScaleY(float scaleY) {
        if (NEEDS_PROXY) {
            wrap(this).setScaleY(scaleY);
        } else {
            super.setScaleY(scaleY);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getTranslationX() {
        return NEEDS_PROXY ? wrap(this).getTranslationX() : super.getTranslationX();
    }

    public void setTranslationX(float translationX) {
        if (NEEDS_PROXY) {
            wrap(this).setTranslationX(translationX);
        } else {
            super.setTranslationX(translationX);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getTranslationY() {
        return NEEDS_PROXY ? wrap(this).getTranslationY() : super.getTranslationY();
    }

    public void setTranslationY(float translationY) {
        if (NEEDS_PROXY) {
            wrap(this).setTranslationY(translationY);
        } else {
            super.setTranslationY(translationY);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getX() {
        return NEEDS_PROXY ? wrap(this).getX() : super.getX();
    }

    public void setX(float x) {
        if (NEEDS_PROXY) {
            wrap(this).setX(x);
        } else {
            super.setX(x);
        }
    }

    public float getY() {
        return NEEDS_PROXY ? wrap(this).getY() : super.getY();
    }

    public void setY(float y) {
        if (NEEDS_PROXY) {
            wrap(this).setY(y);
        } else {
            super.setY(y);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }
}
