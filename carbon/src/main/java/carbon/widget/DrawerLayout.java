package carbon.widget;

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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import carbon.Carbon;
import carbon.CarbonContextWrapper;
import carbon.R;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.behavior.Behavior;
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
import carbon.view.BehaviorView;
import carbon.view.Corners;
import carbon.view.CornersView;
import carbon.view.InsetView;
import carbon.view.MaxSizeView;
import carbon.view.RenderingModeView;
import carbon.view.RevealView;
import carbon.view.StateAnimatorView;
import carbon.view.StrokeView;
import carbon.view.TouchMarginView;
import carbon.view.TransformationView;
import carbon.view.VisibleView;

/**
 * Carbon version of a drawer layout with support for shadows, ripples and other material features.
 * Not really useful, but added for sake of completeness.
 */
public class DrawerLayout extends androidx.drawerlayout.widget.DrawerLayout
        implements
        ShadowView,
        RippleView,
        TouchMarginView,
        StateAnimatorView,
        AnimatedView,
        InsetView,
        CornersView,
        StrokeView,
        MaxSizeView,
        RevealView,
        VisibleView,
        TransformationView,
        BehaviorView {

    private OnTouchListener onDispatchTouchListener;

    public DrawerLayout(Context context) {
        super(CarbonContextWrapper.wrap(context), null, R.attr.carbon_drawerLayoutStyle);
        initDrawerLayout(null, R.attr.carbon_drawerLayoutStyle);
    }

    public DrawerLayout(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.DrawerLayout, R.attr.carbon_drawerLayoutStyle, R.styleable.DrawerLayout_carbon_theme), attrs, R.attr.carbon_drawerLayoutStyle);
        initDrawerLayout(attrs, R.attr.carbon_drawerLayoutStyle);
    }

    public DrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.DrawerLayout, defStyleAttr, R.styleable.DrawerLayout_carbon_theme), attrs, defStyleAttr);
        initDrawerLayout(attrs, defStyleAttr);
    }

    private static int[] rippleIds = new int[]{
            R.styleable.DrawerLayout_carbon_rippleColor,
            R.styleable.DrawerLayout_carbon_rippleStyle,
            R.styleable.DrawerLayout_carbon_rippleHotspot,
            R.styleable.DrawerLayout_carbon_rippleRadius
    };
    private static int[] animationIds = new int[]{
            R.styleable.DrawerLayout_carbon_inAnimation,
            R.styleable.DrawerLayout_carbon_outAnimation
    };
    private static int[] touchMarginIds = new int[]{
            R.styleable.DrawerLayout_carbon_touchMargin,
            R.styleable.DrawerLayout_carbon_touchMarginLeft,
            R.styleable.DrawerLayout_carbon_touchMarginTop,
            R.styleable.DrawerLayout_carbon_touchMarginRight,
            R.styleable.DrawerLayout_carbon_touchMarginBottom
    };
    private static int[] insetIds = new int[]{
            R.styleable.DrawerLayout_carbon_inset,
            R.styleable.DrawerLayout_carbon_insetLeft,
            R.styleable.DrawerLayout_carbon_insetTop,
            R.styleable.DrawerLayout_carbon_insetRight,
            R.styleable.DrawerLayout_carbon_insetBottom,
            R.styleable.DrawerLayout_carbon_insetColor
    };
    private static int[] strokeIds = new int[]{
            R.styleable.DrawerLayout_carbon_stroke,
            R.styleable.DrawerLayout_carbon_strokeWidth
    };
    private static int[] cornerCutRadiusIds = new int[]{
            R.styleable.DrawerLayout_carbon_cornerRadiusTopStart,
            R.styleable.DrawerLayout_carbon_cornerRadiusTopEnd,
            R.styleable.DrawerLayout_carbon_cornerRadiusBottomStart,
            R.styleable.DrawerLayout_carbon_cornerRadiusBottomEnd,
            R.styleable.DrawerLayout_carbon_cornerRadius,
            R.styleable.DrawerLayout_carbon_cornerCutTopStart,
            R.styleable.DrawerLayout_carbon_cornerCutTopEnd,
            R.styleable.DrawerLayout_carbon_cornerCutBottomStart,
            R.styleable.DrawerLayout_carbon_cornerCutBottomEnd,
            R.styleable.DrawerLayout_carbon_cornerCut
    };
    private static int[] maxSizeIds = new int[]{
            R.styleable.DrawerLayout_carbon_maxWidth,
            R.styleable.DrawerLayout_carbon_maxHeight,
    };
    private static int[] elevationIds = new int[]{
            R.styleable.DrawerLayout_carbon_elevation,
            R.styleable.DrawerLayout_carbon_elevationShadowColor,
            R.styleable.DrawerLayout_carbon_elevationAmbientShadowColor,
            R.styleable.DrawerLayout_carbon_elevationSpotShadowColor
    };

    private void initDrawerLayout(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DrawerLayout, defStyleAttr, R.style.carbon_DrawerLayout);

        Carbon.initRippleDrawable(this, a, rippleIds);
        Carbon.initElevation(this, a, elevationIds);
        Carbon.initAnimations(this, a, animationIds);
        Carbon.initTouchMargin(this, a, touchMarginIds);
        Carbon.initInset(this, a, insetIds);
        Carbon.initMaxSize(this, a, maxSizeIds);
        Carbon.initStroke(this, a, strokeIds);
        Carbon.initCornerCutRadius(this, a, cornerCutRadiusIds);

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
        if (Carbon.IS_LOLLIPOP_OR_HIGHER && renderingMode == RenderingMode.Auto) {
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
        boolean c = !corners.isZero();
        // draw not called, we have to handle corners here
        if (isInEditMode() && !drawCalled && (r || c) && getWidth() > 0 && getHeight() > 0) {
            Bitmap layer = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas layerCanvas = new Canvas(layer);
            dispatchDrawInternal(layerCanvas);

            Bitmap mask = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas maskCanvas = new Canvas(mask);
            Paint maskPaint = new Paint(0xffffffff);
            maskCanvas.drawPath(cornersMask, maskPaint);

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    int maskPixel = mask.getPixel(x, y);
                    layer.setPixel(x, y, Color.alpha(maskPixel) > 0 ? layer.getPixel(x, y) : 0);
                }
            }
            canvas.drawBitmap(layer, 0, 0, paint);
        } else if (!drawCalled && (r || c) && getWidth() > 0 && getHeight() > 0 && (!Carbon.IS_LOLLIPOP_OR_HIGHER || renderingMode == RenderingMode.Software)) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.save();
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
        // TODO: why isShown() returns false after being reattached?
        if (child instanceof ShadowView && (!Carbon.IS_LOLLIPOP_OR_HIGHER || ((RenderingModeView) child).getRenderingMode() == RenderingMode.Software || ((ShadowView) child).getElevationShadowColor() != null)) {
            ShadowView shadowView = (ShadowView) child;
            shadowView.drawShadow(canvas);
        }

        if (child instanceof RippleView) {
            RippleView rippleView = (RippleView) child;
            RippleDrawable rippleDrawable = rippleView.getRippleDrawable();
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless) {
                int saveCount = canvas.save();
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
        if (views.size() != childCount)
            getViews();
        return indexOfChild(views.get(child));
    }

    protected boolean isTransformedTouchPointInView(float x, float y, View child, PointF outLocalPoint) {
        final Rect frame = new Rect();
        child.getHitRect(frame);
        return frame.contains((int) x, (int) y);
    }


    // -------------------------------
    // corners
    // -------------------------------

    private Corners corners;
    private Path cornersMask;

    /**
     * Gets the corner radius. If corner radius is equal to 0, rounded corners are turned off.
     *
     * @return corner radius, equal to or greater than 0.
     */
    @Deprecated
    @Override
    public float getCornerRadius() {
        return corners.getTopStart();
    }

    public Corners getCorners() {
        return corners;
    }


    /**
     * Sets the corner radius. If corner radius is equal to 0, rounded corners are turned off.
     *
     * @param cornerRadius
     */
    @Override
    public void setCornerRadius(float cornerRadius) {
        setCorners(new Corners(cornerRadius, false));
    }

    @Override
    public void setCornerCut(float cornerCut) {
        setCorners(new Corners(cornerCut, true));
    }

    @Override
    public void setCorners(Corners corners) {
        if (this.corners != null && this.corners.equals(corners))
            return;
        if (!Carbon.IS_LOLLIPOP_OR_HIGHER)
            postInvalidate();
        this.corners = corners;
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
        if (!corners.isZero() && Carbon.IS_LOLLIPOP_OR_HIGHER && renderingMode == RenderingMode.Auto) {
            setClipToOutline(true);
            setOutlineProvider(ShadowShape.viewOutlineProvider);
        }

        cornersMask = corners.getPath(getWidth(), getHeight());
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
        boolean c = !corners.isZero();
        if (isInEditMode() && (r || c) && getWidth() > 0 && getHeight() > 0) {
            Bitmap layer = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas layerCanvas = new Canvas(layer);
            drawInternal(layerCanvas);

            Bitmap mask = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas maskCanvas = new Canvas(mask);
            Paint maskPaint = new Paint(0xffffffff);
            maskCanvas.drawPath(cornersMask, maskPaint);

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    int maskPixel = mask.getPixel(x, y);
                    layer.setPixel(x, y, Color.alpha(maskPixel) > 0 ? layer.getPixel(x, y) : 0);
                }
            }
            canvas.drawBitmap(layer, 0, 0, paint);
        } else if ((r || c) && getWidth() > 0 && getHeight() > 0 && (!Carbon.IS_LOLLIPOP_OR_HIGHER || renderingMode == RenderingMode.Software || corners.getShape() == ShadowShape.CONVEX_PATH)) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.save();
                canvas.clipRect(revealAnimator.x - revealAnimator.radius, revealAnimator.y - revealAnimator.radius, revealAnimator.x + revealAnimator.radius, revealAnimator.y + revealAnimator.radius);
                drawInternal(canvas);
                canvas.restoreToCount(saveCount2);
            } else {
                drawInternal(canvas);
            }

            paint.setXfermode(Carbon.CLEAR_MODE);
            if (c) {
                cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
                canvas.drawPath(cornersMask, paint);
            }
            if (r)
                canvas.drawPath(revealAnimator.mask, paint);
            paint.setXfermode(null);

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            drawInternal(canvas);
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
            newRipple.setState(getDrawableState());
            ((Drawable) newRipple).setVisible(getVisibility() == VISIBLE, false);
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

        if (elevation > 0 || corners != null)
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

        if (elevation > 0 || corners != null)
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
    private ColorStateList ambientShadowColor, spotShadowColor;
    private PorterDuffColorFilter ambientShadowColorFilter, spotShadowColorFilter;

    @Override
    public float getElevation() {
        return elevation;
    }

    @Override
    public void setElevation(float elevation) {
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setElevation(elevation);
            super.setTranslationZ(translationZ);
        } else if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            if ((ambientShadowColor == null || spotShadowColor == null) && renderingMode == RenderingMode.Auto) {
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
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setTranslationZ(translationZ);
        } else if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            if ((ambientShadowColor == null || spotShadowColor == null) && renderingMode == RenderingMode.Auto) {
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
        return corners.getShape();
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
        float e = z / getResources().getDisplayMetrics().density;
        if (spotShadow == null || spotShadow.elevation != e || !spotShadow.corners.equals(corners)) {
            ambientShadow = ShadowGenerator.generateShadow(this, e / 4);
            spotShadow = ShadowGenerator.generateShadow(this, e);
        }

        int saveCount = 0;
        boolean maskShadow = getBackground() != null && alpha != 1;
        boolean r = revealAnimator != null && revealAnimator.isRunning();
        if (maskShadow) {
            saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        } else if (r) {
            saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            canvas.clipRect(
                    getLeft() + revealAnimator.x - revealAnimator.radius, getTop() + revealAnimator.y - revealAnimator.radius,
                    getLeft() + revealAnimator.x + revealAnimator.radius, getTop() + revealAnimator.y + revealAnimator.radius);
        }

        paint.setAlpha((int) (Shadow.ALPHA * alpha));

        Matrix matrix = getMatrix();

        canvas.save();
        canvas.translate(this.getLeft(), this.getTop());
        canvas.concat(matrix);
        ambientShadow.draw(canvas, this, paint, ambientShadowColorFilter);
        canvas.restore();

        canvas.save();
        canvas.translate(this.getLeft(), this.getTop() + z / 2);
        canvas.concat(matrix);
        spotShadow.draw(canvas, this, paint, spotShadowColorFilter);
        canvas.restore();

        if (saveCount != 0) {
            canvas.translate(this.getLeft(), this.getTop());
            canvas.concat(matrix);
            paint.setXfermode(Carbon.CLEAR_MODE);
        }
        if (maskShadow) {
            cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
            canvas.drawPath(cornersMask, paint);
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
        ambientShadowColor = spotShadowColor = shadowColor;
        ambientShadowColorFilter = spotShadowColorFilter = shadowColor != null ? new PorterDuffColorFilter(shadowColor.getColorForState(getDrawableState(), shadowColor.getDefaultColor()), PorterDuff.Mode.MULTIPLY) : Shadow.DEFAULT_FILTER;
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public void setElevationShadowColor(int color) {
        ambientShadowColor = spotShadowColor = ColorStateList.valueOf(color);
        ambientShadowColorFilter = spotShadowColorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public ColorStateList getElevationShadowColor() {
        return ambientShadowColor;
    }

    @Override
    public void setOutlineAmbientShadowColor(int color) {
        setOutlineAmbientShadowColor(ColorStateList.valueOf(color));
    }

    @Override
    public void setOutlineAmbientShadowColor(ColorStateList color) {
        ambientShadowColor = color;
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setOutlineAmbientShadowColor(color.getColorForState(getDrawableState(), color.getDefaultColor()));
        } else {
            ambientShadowColorFilter = new PorterDuffColorFilter(color.getColorForState(getDrawableState(), color.getDefaultColor()), PorterDuff.Mode.MULTIPLY);
            setElevation(elevation);
            setTranslationZ(translationZ);
        }
    }

    @Override
    public int getOutlineAmbientShadowColor() {
        return ambientShadowColor.getDefaultColor();
    }

    @Override
    public void setOutlineSpotShadowColor(int color) {
        setOutlineSpotShadowColor(ColorStateList.valueOf(color));
    }

    @Override
    public void setOutlineSpotShadowColor(ColorStateList color) {
        spotShadowColor = color;
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setOutlineSpotShadowColor(color.getColorForState(getDrawableState(), color.getDefaultColor()));
        } else {
            spotShadowColorFilter = new PorterDuffColorFilter(color.getColorForState(getDrawableState(), color.getDefaultColor()), PorterDuff.Mode.MULTIPLY);
            setElevation(elevation);
            setTranslationZ(translationZ);
        }
    }

    @Override
    public int getOutlineSpotShadowColor() {
        return spotShadowColor.getDefaultColor();
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

    final RectF tmpHitRect = new RectF();

    public void getHitRect(@NonNull Rect outRect) {
        Matrix matrix = getMatrix();
        if (matrix.isIdentity()) {
            outRect.set(getLeft(), getTop(), getRight(), getBottom());
        } else {
            tmpHitRect.set(0, 0, getWidth(), getHeight());
            matrix.mapRect(tmpHitRect);
            outRect.set((int) tmpHitRect.left + getLeft(), (int) tmpHitRect.top + getTop(),
                    (int) tmpHitRect.right + getLeft(), (int) tmpHitRect.bottom + getTop());
        }
        outRect.left -= touchMargin.left;
        outRect.top -= touchMargin.top;
        outRect.right += touchMargin.right;
        outRect.bottom += touchMargin.bottom;
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
        if (ambientShadow != null && ambientShadowColor != null)
            ambientShadowColorFilter = new PorterDuffColorFilter(ambientShadowColor.getColorForState(getDrawableState(), ambientShadowColor.getDefaultColor()), PorterDuff.Mode.MULTIPLY);
        if (spotShadow != null && spotShadowColor != null)
            spotShadowColorFilter = new PorterDuffColorFilter(spotShadowColor.getColorForState(getDrawableState(), spotShadowColor.getDefaultColor()), PorterDuff.Mode.MULTIPLY);
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
        } else {
            setVisibility(visibility);
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

    private void drawStroke(Canvas canvas) {
        strokePaint.setStrokeWidth(strokeWidth * 2);
        strokePaint.setColor(stroke.getColorForState(getDrawableState(), stroke.getDefaultColor()));
        cornersMask.setFillType(Path.FillType.WINDING);
        canvas.drawPath(cornersMask, strokePaint);
    }

    @Override
    public void setStroke(ColorStateList colorStateList) {
        stroke = colorStateList;

        if (stroke == null)
            return;

        if (strokePaint == null) {
            strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            strokePaint.setStyle(Paint.Style.STROKE);
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
        if (getWidth() > 0 && getHeight() > 0)
            updateCorners();
    }

    @Override
    public RenderingMode getRenderingMode() {
        return renderingMode;
    }


    // -------------------------------
    // transformations
    // -------------------------------

    List<OnTransformationChangedListener> transformationChangedListeners = new ArrayList<>();

    public void addOnTransformationChangedListener(OnTransformationChangedListener listener) {
        transformationChangedListeners.add(listener);
    }

    public void removeOnTransformationChangedListener(OnTransformationChangedListener listener) {
        transformationChangedListeners.remove(listener);
    }

    public void clearOnTransformationChangedListeners() {
        transformationChangedListeners.clear();
    }

    private void fireOnTransformationChangedListener() {
        if (transformationChangedListeners == null)
            return;
        for (OnTransformationChangedListener listener : transformationChangedListeners)
            listener.onTransformationChanged();
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setRotationY(float rotationY) {
        super.setRotationY(rotationY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setRotationX(float rotationX) {
        super.setRotationX(rotationX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setPivotX(float pivotX) {
        super.setPivotX(pivotX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setPivotY(float pivotY) {
        super.setPivotY(pivotY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        super.setAlpha(alpha);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setTranslationX(float translationX) {
        super.setTranslationX(translationX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
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


    // -------------------------------
    // dependency
    // -------------------------------

    private List<Behavior> behaviors = new ArrayList<>();

    @Override
    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
    }

    @Override
    public void removeBehavior(Behavior behavior) {
        behaviors.remove(behavior);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Stream.of(behaviors).forEach(Behavior::onDetachedFromWindow);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Stream.of(behaviors).forEach(Behavior::onAttachedToWindow);
    }
}
