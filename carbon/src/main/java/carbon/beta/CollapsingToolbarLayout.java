package carbon.beta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
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
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.component.Component;
import carbon.component.ComponentView;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.ElevationComparator;
import carbon.internal.RevealAnimator;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowShape;
import carbon.shadow.ShadowView;
import carbon.view.InsetView;
import carbon.view.MaxSizeView;
import carbon.view.RenderingModeView;
import carbon.view.RevealView;
import carbon.view.RoundedCornersView;
import carbon.view.StateAnimatorView;
import carbon.view.StrokeView;
import carbon.view.VisibleView;
import carbon.widget.OnInsetsChangedListener;
import carbon.widget.RenderingMode;
import carbon.widget.TouchMarginView;

public class CollapsingToolbarLayout extends android.support.design.widget.CollapsingToolbarLayout
        implements
        ShadowView,
        RippleView,
        TouchMarginView,
        StateAnimatorView,
        AnimatedView,
        InsetView,
        RoundedCornersView,
        StrokeView,
        MaxSizeView,
        RevealView,
        VisibleView {

    private OnTouchListener onDispatchTouchListener;

    public CollapsingToolbarLayout(Context context) {
        super(context);
        initCollapsingToolbarLayout(null, R.attr.carbon_collapsingToolbarLayoutStyle);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.CollapsingToolbarLayout, R.attr.carbon_collapsingToolbarLayoutStyle, R.styleable.CollapsingToolbarLayout_carbon_theme), attrs);
        initCollapsingToolbarLayout(attrs, R.attr.carbon_collapsingToolbarLayoutStyle);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.CollapsingToolbarLayout, defStyleAttr, R.styleable.CollapsingToolbarLayout_carbon_theme), attrs, defStyleAttr);
        initCollapsingToolbarLayout(attrs, defStyleAttr);
    }

    private static int[] rippleIds = new int[]{
            R.styleable.CollapsingToolbarLayout_carbon_rippleColor,
            R.styleable.CollapsingToolbarLayout_carbon_rippleStyle,
            R.styleable.CollapsingToolbarLayout_carbon_rippleHotspot,
            R.styleable.CollapsingToolbarLayout_carbon_rippleRadius
    };
    private static int[] animationIds = new int[]{
            R.styleable.CollapsingToolbarLayout_carbon_inAnimation,
            R.styleable.CollapsingToolbarLayout_carbon_outAnimation
    };
    private static int[] touchMarginIds = new int[]{
            R.styleable.CollapsingToolbarLayout_carbon_touchMargin,
            R.styleable.CollapsingToolbarLayout_carbon_touchMarginLeft,
            R.styleable.CollapsingToolbarLayout_carbon_touchMarginTop,
            R.styleable.CollapsingToolbarLayout_carbon_touchMarginRight,
            R.styleable.CollapsingToolbarLayout_carbon_touchMarginBottom
    };
    private static int[] insetIds = new int[]{
            R.styleable.CollapsingToolbarLayout_carbon_inset,
            R.styleable.CollapsingToolbarLayout_carbon_insetLeft,
            R.styleable.CollapsingToolbarLayout_carbon_insetTop,
            R.styleable.CollapsingToolbarLayout_carbon_insetRight,
            R.styleable.CollapsingToolbarLayout_carbon_insetBottom,
            R.styleable.CollapsingToolbarLayout_carbon_insetColor
    };
    private static int[] strokeIds = new int[]{
            R.styleable.CollapsingToolbarLayout_carbon_stroke,
            R.styleable.CollapsingToolbarLayout_carbon_strokeWidth
    };
    private static int[] maxSizeIds = new int[]{
            R.styleable.CollapsingToolbarLayout_carbon_maxWidth,
            R.styleable.CollapsingToolbarLayout_carbon_maxHeight,
    };
    private static int[] elevationIds = new int[]{
            R.styleable.CollapsingToolbarLayout_carbon_elevation,
            R.styleable.CollapsingToolbarLayout_carbon_elevationShadowColor
    };

    private void initCollapsingToolbarLayout(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CollapsingToolbarLayout, defStyleAttr, 0);

        Carbon.initRippleDrawable(this, a, rippleIds);
        Carbon.initElevation(this, a, elevationIds);
        Carbon.initAnimations(this, a, animationIds);
        Carbon.initTouchMargin(this, a, touchMarginIds);
        Carbon.initInset(this, a, insetIds);
        Carbon.initMaxSize(this, a, maxSizeIds);
        Carbon.initStroke(this, a, strokeIds);
        setCornerRadius(a.getDimension(R.styleable.CollapsingToolbarLayout_carbon_cornerRadius, 0));

        a.recycle();

        setChildrenDrawingOrderEnabled(true);
        setClipToPadding(false);
    }

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private boolean drawCalled = false;
    RevealAnimator revealAnimator;

    public Point getLocationOnScreen() {
        int[] outLocation = new int[2];
        super.getLocationOnScreen(outLocation);
        return new Point(outLocation[0], outLocation[1]);
    }

    public Point getLocationInWindow() {
        int[] outLocation = new int[2];
        super.getLocationInWindow(outLocation);
        return new Point(outLocation[0], outLocation[1]);
    }

    public Animator createCircularReveal(android.view.View hotspot, float startRadius, float finishRadius) {
        int[] location = new int[2];
        hotspot.getLocationOnScreen(location);
        int[] myLocation = new int[2];
        getLocationOnScreen(myLocation);
        return createCircularReveal(location[0] - myLocation[0] + hotspot.getWidth() / 2, location[1] - myLocation[1] + hotspot.getHeight() / 2, startRadius, finishRadius);
    }

    @Override
    public Animator createCircularReveal(int x, int y, float startRadius, float finishRadius) {
        startRadius = Carbon.getRevealRadius(this, x, y, startRadius);
        finishRadius = Carbon.getRevealRadius(this, x, y, finishRadius);
        if (Carbon.IS_LOLLIPOP && renderingMode == RenderingMode.Auto) {
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(this, x, y, startRadius, finishRadius);
            circularReveal.setDuration(Carbon.getDefaultRevealDuration());
            return circularReveal;
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
            return revealAnimator;
        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        boolean r = revealAnimator != null && revealAnimator.isRunning();
        boolean c = cornerRadius > 0;
        // draw not called, we have to handle corners here
        if (isInEditMode() && !drawCalled && (r || c) && getWidth() > 0 && getHeight() > 0) {
            Bitmap layer = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas layerCanvas = new Canvas(layer);
            dispatchDrawInternal(layerCanvas);

            Bitmap mask = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas maskCanvas = new Canvas(mask);
            Paint maskPaint = new Paint(0xffffffff);
            maskCanvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), cornerRadius, cornerRadius, maskPaint);

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    int maskPixel = mask.getPixel(x, y);
                    layer.setPixel(x, y, Color.alpha(maskPixel) > 0 ? layer.getPixel(x, y) : 0);
                }
            }
            canvas.drawBitmap(layer, 0, 0, paint);
        } else if (!drawCalled && (r || c) && getWidth() > 0 && getHeight() > 0 && !Carbon.IS_LOLLIPOP || renderingMode == RenderingMode.Software) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.save(Canvas.CLIP_SAVE_FLAG);
                canvas.clipRect(revealAnimator.x - revealAnimator.radius, revealAnimator.y - revealAnimator.radius, revealAnimator.x + revealAnimator.radius, revealAnimator.y + revealAnimator.radius);
                dispatchDrawInternal(canvas);
                canvas.restoreToCount(saveCount2);
            } else {
                dispatchDrawInternal(canvas);
            }

            paint.setXfermode(Carbon.CLEAR_MODE);
            if (c)
                canvas.drawPath(cornersMask, paint);
            if (r)
                canvas.drawPath(revealAnimator.mask, paint);
            paint.setXfermode(null);

            canvas.restoreToCount(saveCount);
        } else {
            dispatchDrawInternal(canvas);
        }
        drawCalled = false;
    }

    private void dispatchDrawInternal(@NonNull Canvas canvas) {
        Collections.sort(getViews(), new ElevationComparator());

        super.dispatchDraw(canvas);
        if (stroke != null)
            drawStroke(canvas);
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
        if (child instanceof ShadowView && (!Carbon.IS_LOLLIPOP || ((RenderingModeView) child).getRenderingMode() == RenderingMode.Software || ((ShadowView) child).getElevationShadowColor() != null)) {
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
    public boolean gatherTransparentRegion(Region region) {
        getViews();
        return super.gatherTransparentRegion(region);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int child) {
        return views.size() > child ? indexOfChild(views.get(child)) : child;
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
        if (getWidth() > 0 && getHeight() > 0)
            updateCorners();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        updateCorners();

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    private void updateCorners() {
        if (cornerRadius > 0) {
            cornerRadius = Math.min(cornerRadius, Math.min(getWidth(), getHeight()) / 2.0f);
            if (Carbon.IS_LOLLIPOP && renderingMode == RenderingMode.Auto) {
                setClipToOutline(true);
                setOutlineProvider(ShadowShape.viewOutlineProvider);
            } else {
                cornersMask = new Path();
                cornersMask.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), cornerRadius, cornerRadius, Path.Direction.CW);
                cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
            }
        } else if (Carbon.IS_LOLLIPOP) {
            setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }

    public void drawInternal(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (stroke != null)
            drawStroke(canvas);
        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(@NonNull Canvas canvas) {
        drawCalled = true;
        boolean r = revealAnimator != null;
        boolean c = cornerRadius > 0;
        if (isInEditMode() && (r || c) && getWidth() > 0 && getHeight() > 0) {
            Bitmap layer = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas layerCanvas = new Canvas(layer);
            drawInternal(layerCanvas);

            Bitmap mask = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas maskCanvas = new Canvas(mask);
            Paint maskPaint = new Paint(0xffffffff);
            maskCanvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), cornerRadius, cornerRadius, maskPaint);

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    int maskPixel = mask.getPixel(x, y);
                    layer.setPixel(x, y, Color.alpha(maskPixel) > 0 ? layer.getPixel(x, y) : 0);
                }
            }
            canvas.drawBitmap(layer, 0, 0, paint);
        } else if ((r || c) && getWidth() > 0 && getHeight() > 0 && !Carbon.IS_LOLLIPOP || renderingMode == RenderingMode.Software) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.save(Canvas.CLIP_SAVE_FLAG);
                canvas.clipRect(revealAnimator.x - revealAnimator.radius, revealAnimator.y - revealAnimator.radius, revealAnimator.x + revealAnimator.radius, revealAnimator.y + revealAnimator.radius);
                drawInternal(canvas);
                canvas.restoreToCount(saveCount2);
            } else {
                drawInternal(canvas);
            }

            paint.setXfermode(Carbon.CLEAR_MODE);
            if (c)
                canvas.drawPath(cornersMask, paint);
            if (r)
                canvas.drawPath(revealAnimator.mask, paint);
            paint.setXfermode(null);

            canvas.restoreToCount(saveCount);
        } else {
            drawInternal(canvas);
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
        invalidateParentIfNeeded();
    }

    @Override
    public void invalidate(@NonNull Rect dirty) {
        super.invalidate(dirty);
        invalidateParentIfNeeded();
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        invalidateParentIfNeeded();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateParentIfNeeded();
    }

    private void invalidateParentIfNeeded() {
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
        postInvalidateParentIfNeededDelayed(delayMilliseconds);
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
        postInvalidateParentIfNeededDelayed(delayMilliseconds);
    }

    private void postInvalidateParentIfNeededDelayed(long delayMilliseconds) {
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);
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
    private Shadow ambientShadow, spotShadow;
    private ColorStateList shadowColor;
    private PorterDuffColorFilter shadowColorFilter;
    private RectF shadowMaskRect = new RectF();

    @Override
    public float getElevation() {
        return elevation;
    }

    @Override
    public void setElevation(float elevation) {
        if (Carbon.IS_LOLLIPOP) {
            if (shadowColor == null && renderingMode == RenderingMode.Auto) {
                super.setElevation(elevation);
                super.setTranslationZ(translationZ);
            } else {
                super.setElevation(0);
                super.setTranslationZ(0);
            }
        } else if (elevation != this.elevation && getParent() != null) {
            ((View) getParent()).postInvalidate();
        }
        this.elevation = elevation;
    }

    @Override
    public float getTranslationZ() {
        return translationZ;
    }

    public void setTranslationZ(float translationZ) {
        if (translationZ == this.translationZ)
            return;
        if (Carbon.IS_LOLLIPOP) {
            if (shadowColor == null && renderingMode == RenderingMode.Auto) {
                super.setTranslationZ(translationZ);
            } else {
                super.setTranslationZ(0);
            }
        } else if (translationZ != this.translationZ && getParent() != null) {
            ((View) getParent()).postInvalidate();
        }
        this.translationZ = translationZ;
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
        if (ambientShadow == null || ambientShadow.elevation != z || ambientShadow.cornerRadius != cornerRadius) {
            ambientShadow = ShadowGenerator.generateShadow(this, z / getResources().getDisplayMetrics().density / 4);
            spotShadow = ShadowGenerator.generateShadow(this, z / getResources().getDisplayMetrics().density);
        }

        int saveCount = 0;
        boolean maskShadow = getBackground() != null && alpha != 1;
        boolean r = revealAnimator != null && revealAnimator.isRunning();
        if (maskShadow) {
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        } else if (r) {
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            canvas.clipRect(
                    getLeft() + revealAnimator.x - revealAnimator.radius, getTop() + revealAnimator.y - revealAnimator.radius,
                    getLeft() + revealAnimator.x + revealAnimator.radius, getTop() + revealAnimator.y + revealAnimator.radius);
        }

        paint.setAlpha((int) (Shadow.ALPHA * alpha));

        Matrix matrix = getMatrix();

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(this.getLeft(), this.getTop());
        canvas.concat(matrix);
        ambientShadow.draw(canvas, this, paint, shadowColorFilter);
        canvas.restore();

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(this.getLeft(), this.getTop() + z / 2);
        canvas.concat(matrix);
        spotShadow.draw(canvas, this, paint, shadowColorFilter);
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
    public void setElevationShadowColor(ColorStateList shadowColor) {
        this.shadowColor = shadowColor;
        shadowColorFilter = shadowColor != null ? new PorterDuffColorFilter(shadowColor.getColorForState(getDrawableState(), shadowColor.getDefaultColor()), PorterDuff.Mode.MULTIPLY) : Shadow.DEFAULT_FILTER;
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public void setElevationShadowColor(int color) {
        shadowColor = ColorStateList.valueOf(color);
        shadowColorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
        setElevation(elevation);
        setTranslationZ(translationZ);
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

    private Animator inAnim = null, outAnim = null;
    private Animator animator;

    public Animator animateVisibility(final int visibility) {
        if (visibility == View.VISIBLE && (getVisibility() != View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (inAnim != null) {
                animator = inAnim;
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator a) {
                        a.removeListener(this);
                        animator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator a) {
                        a.removeListener(this);
                        animator = null;
                    }
                });
                animator.start();
            }
            setVisibility(visibility);
        } else if (visibility != View.VISIBLE && (getVisibility() == View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (outAnim == null) {
                setVisibility(visibility);
                return null;
            }
            animator = outAnim;
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator a) {
                    if (((ValueAnimator) a).getAnimatedFraction() == 1)
                        setVisibility(visibility);
                    a.removeListener(this);
                    animator = null;
                }

                @Override
                public void onAnimationCancel(Animator a) {
                    a.removeListener(this);
                    animator = null;
                }
            });
            animator.start();
        }
        return animator;
    }

    public Animator getAnimator() {
        return animator;
    }

    public Animator getOutAnimator() {
        return outAnim;
    }

    public void setOutAnimator(Animator outAnim) {
        if (this.outAnim != null)
            this.outAnim.setTarget(null);
        this.outAnim = outAnim;
        if (outAnim != null)
            outAnim.setTarget(this);
    }

    public Animator getInAnimator() {
        return inAnim;
    }

    public void setInAnimator(Animator inAnim) {
        if (this.inAnim != null)
            this.inAnim.setTarget(null);
        this.inAnim = inAnim;
        if (inAnim != null)
            inAnim.setTarget(this);
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

    public <Type extends View> Type findViewOfType(Class<Type> type) {
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(this);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child.getClass().equals(type))
                    return (Type) child;
                if (child instanceof ViewGroup)
                    groups.add((ViewGroup) child);
            }
        }
        return null;
    }

    public <Type extends View> List<Type> findViewsOfType(Class<Type> type) {
        List<Type> result = new ArrayList<>();
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(this);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child.getClass().equals(type))
                    result.add((Type) child);
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
    // rendering mode
    // -------------------------------

    private RenderingMode renderingMode = RenderingMode.Auto;

    @Override
    public void setRenderingMode(RenderingMode mode) {
        this.renderingMode = mode;
        setElevation(elevation);
        setTranslationZ(translationZ);
        updateCorners();
    }

    @Override
    public RenderingMode getRenderingMode() {
        return renderingMode;
    }


    // -------------------------------
    // transformations
    // -------------------------------

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        invalidateParentIfNeeded();
    }

    @Override
    public void setRotationY(float rotationY) {
        super.setRotationY(rotationY);
        invalidateParentIfNeeded();
    }

    @Override
    public void setRotationX(float rotationX) {
        super.setRotationX(rotationX);
        invalidateParentIfNeeded();
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        invalidateParentIfNeeded();
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
        invalidateParentIfNeeded();
    }

    @Override
    public void setPivotX(float pivotX) {
        super.setPivotX(pivotX);
        invalidateParentIfNeeded();
    }

    @Override
    public void setPivotY(float pivotY) {
        super.setPivotY(pivotY);
        invalidateParentIfNeeded();
    }

    @Override
    public void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        super.setAlpha(alpha);
        invalidateParentIfNeeded();
    }

    @Override
    public void setTranslationX(float translationX) {
        super.setTranslationX(translationX);
        invalidateParentIfNeeded();
    }

    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
        invalidateParentIfNeeded();
    }

    public void setWidth(int width) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            layoutParams.width = width;
            setLayoutParams(layoutParams);
        }
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
        } else {
            layoutParams.height = height;
            setLayoutParams(layoutParams);
        }
    }

    public void setSize(int width, int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            setLayoutParams(new ViewGroup.LayoutParams(width, height));
        } else {
            layoutParams.width = width;
            layoutParams.height = height;
            setLayoutParams(layoutParams);
        }
    }

    public void setBounds(int x, int y, int width, int height) {
        setSize(width, height);
        setTranslationX(x);
        setTranslationY(y);
    }
}
