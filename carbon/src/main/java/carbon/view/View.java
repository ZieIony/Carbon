package carbon.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedColorStateList;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.RevealAnimator;
import carbon.widget.Label;
import carbon.widget.OnTransformationChangedListener;
import carbon.widget.PopupWindow;

/**
 * Basic View class for making views from scratch
 */
public abstract class View extends android.view.View
        implements
        ShadowView,
        RippleView,
        TouchMarginView,
        StateAnimatorView,
        AnimatedView,
        ShapeModelView,
        TintedView,
        StrokeView,
        MaxSizeView,
        RevealView,
        VisibleView,
        TransformationView,
        MarginView {

    protected TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public View(Context context) {
        super(context);
        initView(null, 0);
    }

    public View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs, 0);
    }

    public View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public View(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs, defStyleAttr);
    }

    private static int[] rippleIds = new int[]{
            R.styleable.View_carbon_rippleColor,
            R.styleable.View_carbon_rippleStyle,
            R.styleable.View_carbon_rippleHotspot,
            R.styleable.View_carbon_rippleRadius
    };
    private static int[] animationIds = new int[]{
            R.styleable.View_carbon_inAnimation,
            R.styleable.View_carbon_outAnimation
    };
    private static int[] touchMarginIds = new int[]{
            R.styleable.View_carbon_touchMargin,
            R.styleable.View_carbon_touchMarginLeft,
            R.styleable.View_carbon_touchMarginTop,
            R.styleable.View_carbon_touchMarginRight,
            R.styleable.View_carbon_touchMarginBottom
    };
    private static int[] tintIds = new int[]{
            R.styleable.View_carbon_tint,
            R.styleable.View_carbon_tintMode,
            R.styleable.View_carbon_backgroundTint,
            R.styleable.View_carbon_backgroundTintMode,
            R.styleable.View_carbon_animateColorChanges
    };
    private static int[] strokeIds = new int[]{
            R.styleable.View_carbon_stroke,
            R.styleable.View_carbon_strokeWidth
    };
    private static int[] cornerCutRadiusIds = new int[]{
            R.styleable.View_carbon_cornerRadiusTopStart,
            R.styleable.View_carbon_cornerRadiusTopEnd,
            R.styleable.View_carbon_cornerRadiusBottomStart,
            R.styleable.View_carbon_cornerRadiusBottomEnd,
            R.styleable.View_carbon_cornerRadius,
            R.styleable.View_carbon_cornerCutTopStart,
            R.styleable.View_carbon_cornerCutTopEnd,
            R.styleable.View_carbon_cornerCutBottomStart,
            R.styleable.View_carbon_cornerCutBottomEnd,
            R.styleable.View_carbon_cornerCut
    };
    private static int[] maxSizeIds = new int[]{
            R.styleable.View_carbon_maxWidth,
            R.styleable.View_carbon_maxHeight,
    };
    private static int[] elevationIds = new int[]{
            R.styleable.View_carbon_elevation,
            R.styleable.View_carbon_elevationShadowColor,
            R.styleable.View_carbon_elevationAmbientShadowColor,
            R.styleable.View_carbon_elevationSpotShadowColor
    };

    private void initView(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.View, defStyleAttr, 0);

        Carbon.initDefaultBackground(this, a, R.styleable.View_android_background);
        Carbon.initElevation(this, a, elevationIds);
        Carbon.initRippleDrawable(this, a, rippleIds);
        Carbon.initTint(this, a, tintIds);
        Carbon.initAnimations(this, a, animationIds);
        Carbon.initTouchMargin(this, a, touchMarginIds);
        Carbon.initMaxSize(this, a, maxSizeIds);
        Carbon.initStroke(this, a, strokeIds);
        Carbon.initCornerCutRadius(this, a, cornerCutRadiusIds);
        setTooltipText(a.getText(R.styleable.View_carbon_tooltipText));

        a.recycle();
    }

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
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
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


    // -------------------------------
    // corners
    // -------------------------------

    private RectF boundsRect = new RectF();
    private Path cornersMask = new Path();

    public ShapeAppearanceModel getShapeModel() {
        return shapeModel;
    }


    /**
     * Sets the corner radius. If corner radius is equal to 0, rounded corners are turned off.
     *
     * @param cornerRadius
     */
    @Override
    public void setCornerRadius(float cornerRadius) {
        shapeModel = ShapeAppearanceModel.builder().setAllCorners(new RoundedCornerTreatment(cornerRadius)).build();
        setShapeModel(shapeModel);
    }

    @Override
    public void setCornerCut(float cornerCut) {
        shapeModel = ShapeAppearanceModel.builder().setAllCorners(new CutCornerTreatment(cornerCut)).build();
        setShapeModel(shapeModel);
    }

    @Override
    public void setShapeModel(ShapeAppearanceModel model) {
        this.shapeModel = model;
        shadowDrawable = new MaterialShapeDrawable(shapeModel);
        if (getWidth() > 0 && getHeight() > 0)
            updateCorners();
        if (!Carbon.IS_LOLLIPOP_OR_HIGHER)
            postInvalidate();
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
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            setClipToOutline(true);
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(android.view.View view, Outline outline) {
                    if (Carbon.isShapeRect(shapeModel, boundsRect)) {
                        outline.setRect(0, 0, getWidth(), getHeight());
                    } else {
                        shadowDrawable.setBounds(0, 0, getWidth(), getHeight());
                        shadowDrawable.getOutline(outline);
                    }
                }
            });
        }

        boundsRect.set(shadowDrawable.getBounds());
        shadowDrawable.getPathForSize(getWidth(), getHeight(), cornersMask);
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
        boolean r = revealAnimator != null;
        boolean c = !Carbon.isShapeRect(shapeModel, boundsRect);

        if (Carbon.IS_PIE_OR_HIGHER) {
            if (spotShadowColor != null)
                super.setOutlineSpotShadowColor(spotShadowColor.getColorForState(getDrawableState(), spotShadowColor.getDefaultColor()));
            if (ambientShadowColor != null)
                super.setOutlineAmbientShadowColor(ambientShadowColor.getColorForState(getDrawableState(), ambientShadowColor.getDefaultColor()));
        }

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
        } else if (getWidth() > 0 && getHeight() > 0 && (((r || c) && !Carbon.IS_LOLLIPOP_OR_HIGHER) || !shapeModel.isRoundRect(boundsRect))) {
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
            paint.setXfermode(null);    // TODO check if this is needed

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            drawInternal(canvas);
        }
    }


    // -------------------------------
    // ripple
    // -------------------------------

    protected RippleDrawable rippleDrawable;

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (shadowDrawable.isPointInTransparentRegion((int) event.getX(), (int) event.getY()))
            return false;

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
        if (getParent() == null || !(getParent() instanceof android.view.View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((android.view.View) getParent()).invalidate();

        if (elevation > 0 || !Carbon.isShapeRect(shapeModel, boundsRect))
            ((android.view.View) getParent()).invalidate();
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
        if (getParent() == null || !(getParent() instanceof android.view.View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((android.view.View) getParent()).postInvalidateDelayed(delayMilliseconds);

        if (elevation > 0 || !Carbon.isShapeRect(shapeModel, boundsRect))
            ((android.view.View) getParent()).postInvalidateDelayed(delayMilliseconds);
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
    private ShapeAppearanceModel shapeModel = new ShapeAppearanceModel();
    private MaterialShapeDrawable shadowDrawable = new MaterialShapeDrawable(shapeModel);
    private ColorStateList ambientShadowColor, spotShadowColor;

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
            if (ambientShadowColor == null || spotShadowColor == null) {
                super.setElevation(elevation);
                super.setTranslationZ(translationZ);
            } else {
                super.setElevation(0);
                super.setTranslationZ(0);
            }
        } else if (elevation != this.elevation && getParent() != null) {
            ((android.view.View) getParent()).postInvalidate();
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
            if (ambientShadowColor == null || spotShadowColor == null) {
                super.setTranslationZ(translationZ);
            } else {
                super.setTranslationZ(0);
            }
        } else if (translationZ != this.translationZ && getParent() != null) {
            ((android.view.View) getParent()).postInvalidate();
        }
        this.translationZ = translationZ;
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
        float alpha = getAlpha() * Carbon.getBackgroundTintAlpha(this) / 255.0f;
        if (alpha == 0 || !hasShadow())
            return;

        float z = getElevation() + getTranslationZ();

        int saveCount;
        boolean maskShadow = getBackground() != null && alpha != 1;
        boolean r = revealAnimator != null && revealAnimator.isRunning();

        if (alpha != 255) {
            paint.setAlpha((int) (127 * alpha));
            saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        } else {
            saveCount = canvas.save();
        }
        Matrix matrix = getMatrix();
        canvas.setMatrix(matrix);

        if (r) {
            canvas.clipRect(
                    getLeft() + revealAnimator.x - revealAnimator.radius, getTop() + revealAnimator.y - revealAnimator.radius,
                    getLeft() + revealAnimator.x + revealAnimator.radius, getTop() + revealAnimator.y + revealAnimator.radius);
        }

        shadowDrawable.setFillColor(spotShadowColor);
        shadowDrawable.setShadowColor(spotShadowColor != null ? spotShadowColor.getColorForState(getDrawableState(), spotShadowColor.getDefaultColor()) : 0xff000000);
        shadowDrawable.setAlpha(0x44);
        shadowDrawable.setElevation(z);
        shadowDrawable.setShadowVerticalOffset(0);
        shadowDrawable.setBounds(getLeft(), (int) (getTop() + z / 4), getRight(), (int) (getBottom() + z / 4));
        shadowDrawable.draw(canvas);

        canvas.translate(this.getLeft(), this.getTop());
        canvas.concat(matrix);
        paint.setXfermode(Carbon.CLEAR_MODE);
        if (maskShadow) {
            cornersMask.setFillType(Path.FillType.WINDING);
            canvas.drawPath(cornersMask, paint);
        }
        if (r)
            canvas.drawPath(revealAnimator.mask, paint);

        canvas.restoreToCount(saveCount);
        paint.setXfermode(null);
        paint.setAlpha(255);
    }

    @Override
    public void setElevationShadowColor(ColorStateList shadowColor) {
        ambientShadowColor = spotShadowColor = shadowColor;
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public void setElevationShadowColor(int color) {
        ambientShadowColor = spotShadowColor = ColorStateList.valueOf(color);
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
        if (tint != null && tint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) tint).setState(getDrawableState());
        if (backgroundTint != null && backgroundTint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) backgroundTint).setState(getDrawableState());
    }


    // -------------------------------
    // animations
    // -------------------------------

    private Animator inAnim = null, outAnim = null;
    private Animator animator;

    public Animator animateVisibility(final int visibility) {
        if (visibility == android.view.View.VISIBLE && (getVisibility() != android.view.View.VISIBLE || animator != null)) {
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
        } else if (visibility != android.view.View.VISIBLE && (getVisibility() == android.view.View.VISIBLE || animator != null)) {
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
    // tint
    // -------------------------------

    protected ColorStateList tint;
    protected PorterDuff.Mode tintMode;
    ColorStateList backgroundTint;
    PorterDuff.Mode backgroundTintMode;
    boolean animateColorChanges;
    ValueAnimator.AnimatorUpdateListener tintAnimatorListener = animation -> {
        updateTint();
        ViewCompat.postInvalidateOnAnimation(this);
    };
    ValueAnimator.AnimatorUpdateListener backgroundTintAnimatorListener = animation -> {
        updateBackgroundTint();
        ViewCompat.postInvalidateOnAnimation(this);
    };

    @Override
    public void setTintList(ColorStateList list) {
        this.tint = list == null ? null : animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, tintAnimatorListener) : list;
        updateTint();
    }

    @Override
    public void setTint(int color) {
        setTintList(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }

    protected void updateTint() {
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
    public void setBackgroundTintList(ColorStateList list) {
        this.backgroundTint = list == null ? null : animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, backgroundTintAnimatorListener) : list;
        updateBackgroundTint();
    }

    @Override
    public void setBackgroundTint(int color) {
        setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getBackgroundTint() {
        return backgroundTint;
    }

    private void updateBackgroundTint() {
        Drawable background = getBackground();
        if (background instanceof RippleDrawable)
            background = ((RippleDrawable) background).getBackground();
        if (background == null)
            return;

        if (backgroundTint != null && backgroundTintMode != null) {
            Carbon.setTintListMode(background, backgroundTint, backgroundTintMode);
        } else {
            Carbon.setTintList(background, null);
        }

        if (background.isStateful())
            background.setState(getDrawableState());
    }

    @Override
    public void setBackgroundTintMode(@Nullable PorterDuff.Mode mode) {
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
        if (this.animateColorChanges == animateColorChanges)
            return;
        this.animateColorChanges = animateColorChanges;
        setTintList(tint);
        setBackgroundTintList(backgroundTint);
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
    public int getMaxWidth() {
        return maxWidth;
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        requestLayout();
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        onMeasureInternal(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > maxWidth || getMeasuredHeight() > maxHeight) {
            if (getMeasuredWidth() > maxWidth)
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
            if (getMeasuredHeight() > maxHeight)
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
            onMeasureInternal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    protected void onMeasureInternal(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
    // tooltip
    // -------------------------------

    public void setTooltipText(CharSequence text) {
        if (text != null) {
            setOnLongClickListener(v -> {
                Label tooltip = (Label) LayoutInflater.from(getContext()).inflate(R.layout.carbon_tooltip, null);
                tooltip.setText(text);
                PopupWindow window = new PopupWindow(tooltip);
                window.show(this, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
                new Handler(Looper.getMainLooper()).postDelayed(window::dismiss, AnimUtils.TOOLTIP_DURATION);
                return true;
            });
        } else if (isLongClickable()) {
            setOnLongClickListener(null);
        }
    }
}
