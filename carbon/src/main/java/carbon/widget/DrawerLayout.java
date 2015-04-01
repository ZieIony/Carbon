package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.RippleStateAnimator;
import carbon.animation.StateAnimator;
import carbon.drawable.EmptyDrawable;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;
import carbon.internal.ElevationComparator;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2015-04-01.
 */
public class DrawerLayout extends android.support.v4.widget.DrawerLayout implements ShadowView, RippleView, TouchMarginView, StateAnimatorView, AnimatedView {
    private boolean debugMode;

    public DrawerLayout(Context context) {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_drawerLayoutStyle);
    }

    public DrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DrawerLayout, defStyleAttr, 0);
        Carbon.initRippleDrawable(this, attrs, defStyleAttr);

        setElevation(a.getDimension(R.styleable.DrawerLayout_carbon_elevation, 0));

        Carbon.initAnimations(this, attrs, defStyleAttr);
        Carbon.initTouchMargin(this, attrs, defStyleAttr);
        setCornerRadius(a.getDimension(R.styleable.DrawerLayout_carbon_cornerRadius, 0));

        a.recycle();

        if (isInEditMode()) {
            a = getContext().obtainStyledAttributes(attrs, R.styleable.Carbon, defStyleAttr, 0);
            debugMode = a.getBoolean(R.styleable.Carbon_carbon_debugMode, false);
            a.recycle();
        }

        setChildrenDrawingOrderEnabled(true);
        setClipToPadding(false);

        if (getBackground() == null)
            super.setBackgroundDrawable(emptyBackground);
    }


    List<View> views;
    Map<View, Shadow> shadows = new HashMap<>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (views == null || views.size() != getChildCount()) {
            views = new ArrayList<View>();
            for (int i = 0; i < getChildCount(); i++)
                views.add(getChildAt(i));
        }
        Collections.sort(views, new ElevationComparator());

        super.dispatchDraw(canvas);

        if (debugMode)
            Carbon.drawDebugInfo(this, canvas);
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

                float[] childLocation = new float[]{(child.getLeft() + child.getRight()) / 2, (child.getTop() + child.getBottom()) / 2};
                if (child.getAnimation() != null) {
                    Transformation t = new Transformation();
                    child.getAnimation().getTransformation(drawingTime, t);
                    t.getMatrix().mapPoints(childLocation);
                }

                int[] location = new int[2];
                getLocationOnScreen(location);
                float x = childLocation[0] + location[0];
                float y = childLocation[1] + location[1];
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
    protected int getChildDrawingOrder(int childCount, int child) {
        return views != null ? indexOfChild(views.get(child)) : child;
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
    private Path cornersClipPath;

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
        cornersClipPath = new Path();
        RectF rect = new RectF();
        rect.bottom = getHeight();
        rect.right = getWidth();
        cornersClipPath.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW);
    }

    @Override
    public void draw(Canvas canvas) {
        if (cornerRadius > 0 && getWidth() > 0 && getHeight() > 0) {
            canvas.save(Canvas.CLIP_SAVE_FLAG);
            canvas.clipPath(cornersClipPath);
            super.draw(canvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(canvas);
            canvas.restore();
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (rippleDrawable != null && event.getAction() == MotionEvent.ACTION_DOWN)
            rippleDrawable.setHotspot(event.getX(), event.getY());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }

    public void setRippleDrawable(RippleDrawable newRipple) {
        Drawable background = getBackground();
        if (rippleDrawable != null) {
            rippleDrawable.setCallback(null);
            if (rippleDrawable.getStyle() == RippleDrawable.Style.Background) {
                background = rippleDrawable.getBackground();
            }
        }

        if (newRipple != null) {
            newRipple.setCallback(this);
            if (newRipple.getStyle() == RippleDrawable.Style.Background) {
                newRipple.setBackground(background);
                background = newRipple;
            }
        }

        StateAnimator animator = null;
        for (StateAnimator a : stateAnimators) {
            if (a instanceof RippleStateAnimator) {
                animator = a;
                break;
            }
        }
        if (animator != null && newRipple == null) {
            stateAnimators.remove(animator);
        } else if (animator == null && newRipple != null) {
            addStateAnimator(new RippleStateAnimator(this));
        }

        super.setBackgroundDrawable(background == null ? emptyBackground : background);
        rippleDrawable = newRipple;
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
        if (background instanceof RippleDrawable) {
            setRippleDrawable((RippleDrawable) background);
            return;
        }

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Background) {
            rippleDrawable.setBackground(background);
        } else {
            super.setBackgroundDrawable(background == null ? emptyBackground : background);
        }
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

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setTranslationZ(enabled ? 0 : -elevation);
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
        if (stateAnimators != null)
            for (StateAnimator animator : stateAnimators)
                animator.stateChanged(getDrawableState());
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim, outAnim;

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            AnimUtils.animateIn(this, inAnim, null);
            super.setVisibility(visibility);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    DrawerLayout.super.setVisibility(visibility);
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
