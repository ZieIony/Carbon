package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.DefaultAnimatorListener;
import carbon.animation.StateAnimator;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;
import carbon.internal.ElevationComparator;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2014-11-20.
 */
public class FrameLayout extends android.widget.FrameLayout implements ShadowView, RippleView, TouchMarginView, StateAnimatorView {

    public FrameLayout(Context context) {
        super(context);
        init(null, R.attr.carbon_frameLayoutStyle);
    }

    public FrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_frameLayoutStyle);
    }

    public FrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FrameLayout, defStyleAttr, 0);
        Carbon.initRippleDrawable(this, attrs, defStyleAttr);

        setElevation(a.getDimension(R.styleable.FrameLayout_carbon_elevation, 0));

        setInAnimation(AnimUtils.Style.values()[a.getInt(R.styleable.FrameLayout_carbon_inAnimation, 0)]);
        setOutAnimation(AnimUtils.Style.values()[a.getInt(R.styleable.FrameLayout_carbon_outAnimation, 0)]);
        Carbon.initTouchMargin(this, attrs, defStyleAttr);
        setCornerRadius(a.getDimension(R.styleable.FrameLayout_carbon_cornerRadius, 0));

        a.recycle();

        setChildrenDrawingOrderEnabled(true);
        setClipToPadding(false);
    }


    List<View> views;
    Map<View, Shadow> shadows = new HashMap<>();

    @Override
    protected void dispatchDraw(Canvas canvas) {
        views = new ArrayList<View>();
        for (int i = 0; i < getChildCount(); i++)
            views.add(getChildAt(i));
        Collections.sort(views, new ElevationComparator());
        super.dispatchDraw(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (!child.isShown())
            return super.drawChild(canvas, child, drawingTime);

        if (!isInEditMode() && child instanceof ShadowView) {
            ShadowView shadowView = (ShadowView) child;
            float elevation = shadowView.getElevation() + shadowView.getTranslationZ();
            if (elevation >= 0.01f) {
                Shadow shadow = shadows.get(child);
                if (shadow == null || shadow.elevation != elevation) {
                    shadow = ShadowGenerator.generateShadow(child, elevation);
                    shadows.put(child, shadow);
                }

                paint.setAlpha((int) (127 * ViewHelper.getAlpha(child)));

                int[] location = new int[2];
                child.getLocationOnScreen(location);
                float x = location[0] + child.getWidth() / 2.0f;
                float y = location[1] + child.getHeight() / 2.0f;
                x -= getRootView().getWidth() / 2;
                y += getRootView().getHeight() / 2;   // looks nice
                float length = (float) Math.sqrt(x * x + y * y);

                int saveCount = canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.translate(
                        x / length * elevation / 2,
                        y / length * elevation / 2);
                canvas.translate(
                        child.getLeft(),
                        child.getTop());
                if (Build.VERSION.SDK_INT >= 11) {
                    canvas.concat(child.getMatrix());
                } else {
                    canvas.concat(carbon.internal.ViewHelper.getMatrix(child));
                }
                canvas.scale(ShadowGenerator.SHADOW_SCALE, ShadowGenerator.SHADOW_SCALE);
                shadow.draw(canvas, child, paint);
                canvas.restoreToCount(saveCount);
            }
        }

        if (child instanceof RippleView) {
            RippleView rippleView = (RippleView) child;
            RippleDrawable rippleDrawable = rippleView.getRippleDrawable();
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless) {
                int saveCount = canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.translate(
                        child.getLeft(),
                        child.getTop());
                rippleDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            }
        }

        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return views.indexOf(getChildAt(i));
    }

    protected boolean isTransformedTouchPointInView(float x, float y, View child, PointF outLocalPoint) {
        final Rect frame = new Rect();
        child.getHitRect(frame);
        if (frame.contains((int) x, (int) y)) {
            return true;
        }
        return false;
    }


    // -------------------------------
    // corners
    // -------------------------------

    private float cornerRadius;
    private Canvas textureCanvas;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        initDrawing();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed || getWidth() == 0 || getHeight() == 0)
            return;

        initDrawing();

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    private void initDrawing() {
        if (cornerRadius == 0 || getWidth() == 0 || getHeight() == 0)
            return;
        Bitmap texture = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        textureCanvas = new Canvas(texture);
        paint.setShader(new BitmapShader(texture, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    public void draw(Canvas canvas) {
        if (cornerRadius > 0) {
            textureCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            super.draw(textureCanvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(textureCanvas);

            RectF rect = new RectF();
            rect.bottom = getHeight();
            rect.right = getWidth();
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (rippleDrawable != null && event.getAction() == MotionEvent.ACTION_DOWN)
            ((RippleDrawable) rippleDrawable).setHotspot(event.getX(), event.getY());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setTranslationZ(enabled ? 0 : -elevation);
    }

    @Override
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }

    public void setRippleDrawable(RippleDrawable rippleDrawable) {
        this.rippleDrawable = rippleDrawable;
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || rippleDrawable == who;
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        if (rippleDrawable != null && getParent() != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (rippleDrawable == null || rippleDrawable.getBackground() == null) {
            super.setBackgroundDrawable(background);
            return;
        }
        rippleDrawable.setBackground(background);
        super.setBackgroundDrawable(rippleDrawable);
    }


    // -------------------------------
    // elevation
    // -------------------------------

    private float elevation = 0;
    private float translationZ = 0;
    private boolean isRect = true;

    @Override
    public float getElevation() {
        return elevation;
    }

    public synchronized void setElevation(float elevation) {
        elevation = Math.max(0, Math.min(elevation, 25));
        if (elevation == this.elevation)
            return;
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
        this.translationZ = translationZ;
        if (getParent() != null)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public boolean isRect() {
        return isRect;
    }

    @Override
    public void setRect(boolean rect) {
        this.isRect = rect;
    }


    // -------------------------------
    // touch margin
    // -------------------------------

    private Rect touchMargin;

    @Override
    public void setTouchMargin(Rect rect) {
        touchMargin = rect;
    }

    @Override
    public void setTouchMargin(int left, int top, int right, int bottom) {
        touchMargin = new Rect(left, top, right, bottom);
    }

    @Override
    public Rect getTouchMargin() {
        return touchMargin;
    }

    public void getHitRect(Rect outRect) {
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
        for (StateAnimator animator : stateAnimators)
            animator.stateChanged(getDrawableState());
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim, outAnim;

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            super.setVisibility(visibility);
            AnimUtils.animateIn(this, inAnim, null);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new DefaultAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    FrameLayout.super.setVisibility(visibility);
                }
            });
        }
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
}
