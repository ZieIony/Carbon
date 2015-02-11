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
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.GestureDetector;
import carbon.OnGestureListener;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.DefaultAnimatorListener;
import carbon.internal.ElevationComparator;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2014-11-20.
 */
public class LinearLayout extends android.widget.LinearLayout implements ShadowView, OnGestureListener {
    private boolean isRect = true;
    private float elevation = 0;
    private float translationZ = 0;
    private AnimUtils.Style inAnim, outAnim;

    private int cornerRadius;
    private Bitmap texture;
    private Canvas textureCanvas;
    private Paint paint = new Paint();
    List<View> views;
    Map<View, Shadow> shadows = new HashMap<>();
    GestureDetector gestureDetector = new GestureDetector(this);

    public LinearLayout(Context context) {
        super(context);
        init(null, R.attr.carbon_linearLayoutStyle);
    }

    public LinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_linearLayoutStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LinearLayout, defStyleAttr, 0);
        int color = a.getColor(R.styleable.LinearLayout_carbon_rippleColor, 0);
        if (color != 0)
            setBackgroundDrawable(new RippleDrawable(color, getBackground()));
        setElevation(a.getDimension(R.styleable.LinearLayout_carbon_elevation, 0));
        inAnim = AnimUtils.Style.values()[a.getInt(R.styleable.LinearLayout_carbon_inAnimation, 0)];
        outAnim = AnimUtils.Style.values()[a.getInt(R.styleable.LinearLayout_carbon_outAnimation, 0)];

        cornerRadius = (int) a.getDimension(R.styleable.LinearLayout_carbon_cornerRadius, 0);

        a.recycle();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        setChildrenDrawingOrderEnabled(true);
        setClipToPadding(false);
    }

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            super.setVisibility(visibility);
            AnimUtils.animateIn(this, inAnim, null);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new DefaultAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    LinearLayout.super.setVisibility(visibility);
                }
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isEnabled())
            return false;

        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

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
            ((View) getParent()).invalidate();
    }


    @Override
    public float getTranslationZ() {
        return translationZ;
    }

    private synchronized void setTranslationZInternal(float translationZ) {
        if (translationZ == this.translationZ)
            return;
        this.translationZ = translationZ;
        if (getParent() != null)
            ((View) getParent()).invalidate();
    }

    @Override
    public void setTranslationZ(float translationZ) {
        ValueAnimator animator = ValueAnimator.ofFloat(this.translationZ, translationZ);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationZInternal((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed)
            initDrawing();
        views = new ArrayList<View>();
        for (int i = 0; i < getChildCount(); i++) {
            views.add(getChildAt(i));
        }
    }

    private void initDrawing() {
        if (cornerRadius == 0 || getWidth() == 0 || getHeight() == 0)
            return;
        texture = null;
        texture = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        textureCanvas = new Canvas(texture);
        paint.setShader(new BitmapShader(texture, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Collections.sort(views, new ElevationComparator());

        if (cornerRadius > 0 && textureCanvas != null) {
            textureCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            super.dispatchDraw(textureCanvas);

            RectF rect = new RectF();
            rect.bottom = getHeight();
            rect.right = getWidth();
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
        } else {
            super.dispatchDraw(canvas);
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (!isInEditMode()) {
            if (!child.isShown() || !(child instanceof ShadowView))
                return super.drawChild(canvas, child, drawingTime);

            ShadowView shadowView = (ShadowView) child;
            float elevation = shadowView.getElevation() + shadowView.getTranslationZ();
            if (elevation < 0.01f)
                return super.drawChild(canvas, child, drawingTime);

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

            canvas.save(Canvas.MATRIX_SAVE_FLAG);
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
            canvas.restore();
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

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        initDrawing();
    }

    public AnimUtils.Style getOutAnim() {
        return outAnim;
    }

    public void setOutAnim(AnimUtils.Style outAnim) {
        this.outAnim = outAnim;
    }

    public AnimUtils.Style getInAnim() {
        return inAnim;
    }

    public void setInAnim(AnimUtils.Style inAnim) {
        this.inAnim = inAnim;
    }

    @Override
    public void onPress(MotionEvent motionEvent) {
        if (getBackground() instanceof RippleDrawable)
            ((RippleDrawable) getBackground()).onPress(motionEvent);
    }

    @Override
    public void onTap(MotionEvent motionEvent) {

    }

    @Override
    public void onDrag(MotionEvent motionEvent) {

    }

    @Override
    public void onMove(MotionEvent motionEvent) {

    }

    @Override
    public void onRelease(MotionEvent motionEvent) {
        if (getBackground() instanceof RippleDrawable)
            ((RippleDrawable) getBackground()).onRelease(motionEvent);
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public void onMultiTap(MotionEvent motionEvent, int clicks) {

    }

    @Override
    public void onCancel(MotionEvent motionEvent) {
        if (getBackground() instanceof RippleDrawable)
            ((RippleDrawable) getBackground()).onCancel(motionEvent);
    }
}
