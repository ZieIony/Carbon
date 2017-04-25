package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
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
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
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
import carbon.drawable.ColorStateListDrawable;
import carbon.drawable.DefaultPrimaryColorStateList;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.AllCapsTransformationMethod;
import carbon.internal.RevealAnimator;
import carbon.internal.TypefaceUtils;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowShape;
import carbon.shadow.ShadowView;

/**
 * Carbon version of android.widget.Button. Supports shadows, ripples, animations and all other
 * material features.
 */
@SuppressLint("AppCompatCustomView")
public class Button extends android.widget.Button
        implements ShadowView, RippleView, TouchMarginView, StateAnimatorView, AnimatedView, RoundedCornersView, TintedView, StrokeView, AutoSizeTextView, RevealView, VisibleView {

    protected TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public Button(Context context) {
        super(context);
        initButton(null, android.R.attr.buttonStyle);
    }

    public Button(Context context, String text, OnClickListener listener) {
        super(context);
        initButton(null, android.R.attr.buttonStyle);
        setText(text);
        setOnClickListener(listener);
    }

    /**
     * XML constructor. Gets default parameters from android.R.attr.buttonStyle.
     *
     * @param context
     * @param attrs
     */
    public Button(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.Button, android.R.attr.buttonStyle, R.styleable.Button_carbon_theme), attrs, android.R.attr.buttonStyle);
        initButton(attrs, android.R.attr.buttonStyle);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.Button, defStyleAttr, R.styleable.Button_carbon_theme), attrs, defStyleAttr);
        initButton(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Button(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.Button, defStyleAttr, R.styleable.Button_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initButton(attrs, defStyleAttr);
    }

    private static int[] rippleIds = new int[]{
            R.styleable.Button_carbon_rippleColor,
            R.styleable.Button_carbon_rippleStyle,
            R.styleable.Button_carbon_rippleHotspot,
            R.styleable.Button_carbon_rippleRadius
    };
    private static int[] animationIds = new int[]{
            R.styleable.Button_carbon_inAnimation,
            R.styleable.Button_carbon_outAnimation
    };
    private static int[] touchMarginIds = new int[]{
            R.styleable.Button_carbon_touchMargin,
            R.styleable.Button_carbon_touchMarginLeft,
            R.styleable.Button_carbon_touchMarginTop,
            R.styleable.Button_carbon_touchMarginRight,
            R.styleable.Button_carbon_touchMarginBottom
    };
    private static int[] tintIds = new int[]{
            R.styleable.Button_carbon_tint,
            R.styleable.Button_carbon_tintMode,
            R.styleable.Button_carbon_backgroundTint,
            R.styleable.Button_carbon_backgroundTintMode,
            R.styleable.Button_carbon_animateColorChanges
    };
    private static int[] strokeIds = new int[]{
            R.styleable.Button_carbon_stroke,
            R.styleable.Button_carbon_strokeWidth
    };
    private static int[] elevationIds = new int[]{
            R.styleable.Button_carbon_elevation,
            R.styleable.Button_carbon_elevationShadowColor
    };
    private static int[] autoSizeTextIds = new int[]{
            R.styleable.Button_carbon_autoSizeText,
            R.styleable.Button_carbon_autoSizeMinTextSize,
            R.styleable.Button_carbon_autoSizeMaxTextSize,
            R.styleable.Button_carbon_autoSizeStepGranularity
    };

    private void initButton(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Button, defStyleAttr, R.style.carbon_Button);

        int ap = a.getResourceId(R.styleable.Button_android_textAppearance, -1);
        if (ap != -1)
            setTextAppearanceInternal(ap);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (!isInEditMode() && attr == R.styleable.Button_carbon_fontPath) {
                String path = a.getString(attr);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                setTypeface(typeface);
            } else if (attr == R.styleable.Button_carbon_fontFamily) {
                int textStyle = a.getInt(R.styleable.Button_android_textStyle, 0);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), a.getString(attr), textStyle);
                setTypeface(typeface);
            } else if (attr == R.styleable.Button_android_textAllCaps) {
                setAllCaps(a.getBoolean(attr, true));
            }
        }

        TypedValue bg = new TypedValue();
        a.getValue(R.styleable.Button_android_background, bg);
        if (bg.resourceId == R.drawable.carbon_defaultprimarybackground)
            setBackgroundDrawable(new ColorStateListDrawable(AnimatedColorStateList.fromList(new DefaultPrimaryColorStateList(getContext()), animation -> postInvalidate())));

        Carbon.initRippleDrawable(this, a, rippleIds);
        Carbon.initElevation(this, a, elevationIds);
        Carbon.initTint(this, a, tintIds);
        Carbon.initAnimations(this, a, animationIds);
        Carbon.initTouchMargin(this, a, touchMarginIds);
        Carbon.initHtmlText(this, a, R.styleable.Button_carbon_htmlText);
        Carbon.initStroke(this, a, strokeIds);
        setCornerRadius(a.getDimension(R.styleable.Button_carbon_cornerRadius, 0));
        Carbon.initAutoSizeText(this, a, autoSizeTextIds);

        a.recycle();
    }

    /**
     * Changes text transformation method to caps.
     *
     * @param allCaps if true, Button will automatically capitalize all characters
     */
    public void setAllCaps(boolean allCaps) {
        if (allCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        } else {
            setTransformationMethod(null);
        }
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(animateColorChanges && !(colors instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(colors, textColorAnimatorListener) : colors);
    }

    @Override
    public void setTextAppearance(@NonNull Context context, int resid) {
        super.setTextAppearance(context, resid);
        setTextAppearanceInternal(resid);
    }

    public void setTextAppearance(int resid) {
        super.setTextAppearance(getContext(), resid);
        setTextAppearanceInternal(resid);
    }

    private void setTextAppearanceInternal(int resid) {
        TypedArray appearance = getContext().obtainStyledAttributes(resid, R.styleable.TextAppearance);
        if (appearance != null) {
            for (int i = 0; i < appearance.getIndexCount(); i++) {
                int attr = appearance.getIndex(i);
                if (!isInEditMode() && attr == R.styleable.TextAppearance_carbon_fontPath) {
                    String path = appearance.getString(attr);
                    Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                    setTypeface(typeface);
                } else if (attr == R.styleable.TextAppearance_carbon_fontFamily) {
                    int textStyle = appearance.getInt(R.styleable.TextAppearance_android_textStyle, 0);
                    Typeface typeface = TypefaceUtils.getTypeface(getContext(), appearance.getString(attr), textStyle);
                    setTypeface(typeface);
                } else if (attr == R.styleable.Button_android_textAllCaps) {
                    setAllCaps(appearance.getBoolean(attr, true));
                }
            }
            appearance.recycle();
        }
    }

    RevealAnimator revealAnimator;

    @Override
    public Animator startReveal(int x, int y, float startRadius, float finishRadius) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH && renderingMode == RenderingMode.Auto) {
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
     * Shadows work faster when corner radius is less than 2.5dp.
     *
     * @return corner radius, equal to or greater than 0.
     */
    public float getCornerRadius() {
        return cornerRadius;
    }

    /**
     * Sets the corner radius. If corner radius is equal to 0, rounded corners are turned off.
     * Shadows work faster when corner radius is less than 2.5dp.
     *
     * @param cornerRadius
     */
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && renderingMode == RenderingMode.Auto) {
                setClipToOutline(true);
                setOutlineProvider(ShadowShape.viewOutlineProvider);
            } else {
                cornersMask = new Path();
                cornersMask.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), cornerRadius, cornerRadius, Path.Direction.CW);
                cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (isInEditMode() && cornerRadius > 0 && getWidth() > 0 && getHeight() > 0) {
            Bitmap layer = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas layerCanvas = new Canvas(layer);
            super.draw(layerCanvas);
            if (stroke != null)
                drawStroke(layerCanvas);

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
        } else if (cornerRadius > 0 && getWidth() > 0 && getHeight() > 0 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH || renderingMode == RenderingMode.Software) {
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
        if (a != null) {
            a.getTransformation(event.getEventTime(), t);
            float[] loc = new float[]{event.getX(), event.getY()};
            t.getMatrix().mapPoints(loc);
            event.setLocation(loc[0], loc[1]);
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
    public void setElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        if (shadow == null || shadow.elevation != z)
            shadow = ShadowGenerator.generateShadow(this, z / getResources().getDisplayMetrics().density);

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
        ColorStateList textColors = getTextColors();
        if (textColors instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) textColors).setState(getDrawableState());
        if (tint != null && tint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) tint).setState(getDrawableState());
        if (backgroundTint != null && backgroundTint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) backgroundTint).setState(getDrawableState());
        if (shadow != null && shadowColor != null)
            shadowColorFilter = new PorterDuffColorFilter(shadowColor.getColorForState(getDrawableState(), shadowColor.getDefaultColor()), PorterDuff.Mode.MULTIPLY);
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim = AnimUtils.Style.None, outAnim = AnimUtils.Style.None;
    private Animator animator;

    public Animator animateVisibility(final int visibility) {
        float alpha = getAlpha();
        if (visibility == View.VISIBLE && (getVisibility() != View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (inAnim != AnimUtils.Style.None) {
                animator = AnimUtils.animateIn(this, inAnim, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator a) {
                        animator = null;
                        setAlpha(alpha);
                    }
                });
            }
            setVisibility(visibility);
        } else if (visibility != View.VISIBLE && (getVisibility() == View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (outAnim == AnimUtils.Style.None) {
                setVisibility(visibility);
                return null;
            }
            animator = AnimUtils.animateOut(this, outAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator a) {
                    if (((ValueAnimator) a).getAnimatedFraction() == 1)
                        setVisibility(visibility);
                    animator = null;
                    setAlpha(alpha);
                }
            });
        }
        return animator;
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
        ViewCompat.postInvalidateOnAnimation(Button.this);
    };
    ValueAnimator.AnimatorUpdateListener backgroundTintAnimatorListener = animation -> {
        updateBackgroundTint();
        ViewCompat.postInvalidateOnAnimation(Button.this);
    };
    ValueAnimator.AnimatorUpdateListener textColorAnimatorListener = animation -> setHintTextColor(getHintTextColors());

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
        Drawable[] drawables = getCompoundDrawables();
        if (tint != null && tintMode != null) {
            int color = tint.getColorForState(getDrawableState(), tint.getDefaultColor());
            for (Drawable d : drawables)
                if (d != null)
                    d.setColorFilter(new PorterDuffColorFilter(color, tintMode));
        } else {
            for (Drawable d : drawables)
                if (d != null)
                    d.setColorFilter(null);
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
        Drawable background = getBackground();
        if (background instanceof RippleDrawable)
            background = ((RippleDrawable) background).getBackground();
        if (background == null)
            return;
        if (backgroundTint != null && backgroundTintMode != null) {
            int color = backgroundTint.getColorForState(getDrawableState(), backgroundTint.getDefaultColor());
            background.setColorFilter(new PorterDuffColorFilter(color, backgroundTintMode));
        } else {
            background.setColorFilter(null);
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
        if (!(getTextColors() instanceof AnimatedColorStateList))
            setTextColor(AnimatedColorStateList.fromList(getTextColors(), textColorAnimatorListener));
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
    // auto size
    // -------------------------------

    private AutoSizeTextMode autoSizeText = AutoSizeTextMode.None;
    private float minTextSize, maxTextSize, autoSizeStepGranularity;
    private float[] autoSizeStepPresets;

    private RectF textRect = new RectF();
    private RectF availableSpaceRect = new RectF();
    private float spacingMult = 1.0f;
    private float spacingAdd = 0.0f;
    private int maxLines = -1;

    @NonNull
    public AutoSizeTextMode getAutoSizeText() {
        return autoSizeText;
    }

    public void setAutoSizeText(@NonNull AutoSizeTextMode autoSizeText) {
        this.autoSizeText = autoSizeText;
        adjustTextSize();
    }

    @Override
    public void setText(final CharSequence text, BufferType type) {
        super.setText(text, type);
        adjustTextSize();
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        adjustTextSize();
    }

    @Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        this.maxLines = maxLines;
        adjustTextSize();
    }

    @Override
    public void setSingleLine() {
        super.setSingleLine();
        adjustTextSize();
    }

    @Override
    public void setSingleLine(boolean singleLine) {
        super.setSingleLine(singleLine);
        if (!singleLine)
            super.setMaxLines(-1);
        adjustTextSize();
    }

    @Override
    public void setLines(int lines) {
        super.setLines(lines);
        adjustTextSize();
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        adjustTextSize();
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        spacingMult = mult;
        spacingAdd = add;
    }

    private void initAutoSize() {
        if (autoSizeText == AutoSizeTextMode.Uniform && minTextSize > 0 && maxTextSize > 0) {
            autoSizeStepPresets = new float[(int) Math.ceil((maxTextSize - minTextSize) / autoSizeStepGranularity) + 1];
            for (int i = 0; i < autoSizeStepPresets.length - 1; i++)
                autoSizeStepPresets[i] = minTextSize + autoSizeStepGranularity * i;
            autoSizeStepPresets[autoSizeStepPresets.length - 1] = maxTextSize;
        }
    }

    public float getMinTextSize() {
        return minTextSize;
    }

    public void setMinTextSize(float minTextSize) {
        this.minTextSize = minTextSize;
        autoSizeStepPresets = null;
        adjustTextSize();
    }

    public float getMaxTextSize() {
        return maxTextSize;
    }

    public float getAutoSizeStepGranularity() {
        return autoSizeStepGranularity;
    }

    public void setAutoSizeStepGranularity(float autoSizeStepGranularity) {
        this.autoSizeStepGranularity = autoSizeStepGranularity;
        autoSizeStepPresets = null;
        adjustTextSize();
    }

    public void setMaxTextSize(float maxTextSize) {
        this.maxTextSize = maxTextSize;
        autoSizeStepPresets = null;
        adjustTextSize();
    }

    private void adjustTextSize() {
        if (autoSizeText == AutoSizeTextMode.None || minTextSize <= 0 || maxTextSize <= 0 || getMeasuredWidth() == 0 || getMeasuredHeight() == 0)
            return;
        if (autoSizeStepPresets == null)
            initAutoSize();
        availableSpaceRect.right = getMeasuredWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
        availableSpaceRect.bottom = getMeasuredHeight() - getCompoundPaddingBottom() - getCompoundPaddingTop();
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, binarySearch(availableSpaceRect));
    }

    private float binarySearch(RectF availableSpace) {
        int lastBest = 0;
        int lo = 0;
        int hi = autoSizeStepPresets.length - 1;
        int mid;
//        for (int i = 0; i < autoSizeStepPresets.length; i++) {
//            if (testSize(autoSizeStepPresets[i], availableSpace)) {
//                lastBest = i;
//            } else {
//                break;
//            }
//        }
        while (lo <= hi) {
            mid = (lo + hi) / 2;
            boolean fits = testSize(autoSizeStepPresets[mid], availableSpace);
            if (fits) {
                lastBest = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return autoSizeStepPresets[lastBest];

    }

    public boolean testSize(float suggestedSize, RectF availableSpace) {
        paint.setTextSize(suggestedSize);
        paint.setTypeface(getTypeface());
        String text = getText().toString();
        if (maxLines == 1) {
            textRect.bottom = paint.getFontSpacing();
            textRect.right = paint.measureText(text);
            return availableSpace.width() >= textRect.right && availableSpace.height() >= textRect.bottom;
        } else {
            StaticLayout layout = new StaticLayout(text, paint, (int) availableSpace.right, Layout.Alignment.ALIGN_NORMAL, spacingMult, spacingAdd, true);
            if (maxLines != -1 && layout.getLineCount() > maxLines)
                return false;
            return availableSpace.width() >= layout.getWidth() && availableSpace.height() >= layout.getHeight();
        }
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        super.onTextChanged(text, start, before, after);
        adjustTextSize();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
        super.onSizeChanged(width, height, oldwidth, oldheight);
        if (width != oldwidth || height != oldheight)
            adjustTextSize();
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
}
