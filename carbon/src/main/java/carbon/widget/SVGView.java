package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.nineoldandroids.animation.Animator;

import carbon.Carbon;
import carbon.GestureDetector;
import carbon.OnGestureListener;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.DefaultAnimatorListener;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;

/**
 * Created by Marcin on 2014-12-02.
 */
public class SVGView extends View implements OnGestureListener, RippleView {
    private static final String TAG = SVGView.class.getSimpleName();
    private int svgId;
    private int filterColor;
    private Rect touchMargin;
    private Bitmap bitmap;
    private Paint paint = new Paint();
    private AnimUtils.Style inAnim, outAnim;
    private GestureDetector gestureDetector = new GestureDetector(this);
    private RippleDrawable rippleDrawable;

    public SVGView(Context context) {
        super(context);
        init(null, R.attr.carbon_svgViewStyle);
    }

    public SVGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_svgViewStyle);
    }

    public SVGView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SVGView, defStyleAttr, 0);
        svgId = a.getResourceId(R.styleable.SVGView_carbon_src, 0);
        Carbon.initRippleDrawable(this, attrs, defStyleAttr);
        filterColor = a.getColor(R.styleable.SVGView_carbon_filterColor, 0);
        paint.setColorFilter(new LightingColorFilter(0, filterColor));
        inAnim = AnimUtils.Style.values()[a.getInt(R.styleable.SVGView_carbon_inAnimation, 0)];
        outAnim = AnimUtils.Style.values()[a.getInt(R.styleable.SVGView_carbon_outAnimation, 0)];

        int touchMarginAll = (int) a.getDimension(R.styleable.SVGView_carbon_touchMargin, 0);
        if (touchMarginAll > 0) {
            touchMargin = new Rect(touchMarginAll, touchMarginAll, touchMarginAll, touchMarginAll);
        } else {
            touchMargin = new Rect();
            int top = (int) a.getDimension(R.styleable.SVGView_carbon_touchMarginTop, 0);
            int left = (int) a.getDimension(R.styleable.SVGView_carbon_touchMarginLeft, 0);
            int right = (int) a.getDimension(R.styleable.SVGView_carbon_touchMarginRight, 0);
            int bottom = (int) a.getDimension(R.styleable.SVGView_carbon_touchMarginBottom, 0);
            if (top > 0 || left > 0 || right > 0 || bottom > 0)
                touchMargin = new Rect(left, top, right, bottom);
        }

        a.recycle();
    }

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            super.setVisibility(visibility);
            AnimUtils.animateIn(this, inAnim, null);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new DefaultAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    SVGView.super.setVisibility(visibility);
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled())
            return false;

        gestureDetector.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }

    public void setSVGResource(int svgId) {
        if (this.svgId == svgId)
            return;
        this.svgId = svgId;
        render();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (isInEditMode() || svgId == 0)
            return;

        if (changed || bitmap == null)
            render();
    }

    private synchronized void render() {
        if (svgId == 0 || getWidth() == 0)
            return;
        try {
            SVG svg = SVG.getFromResource(getContext(), svgId);
            bitmap = Bitmap.createBitmap(getWidth() - getPaddingLeft() - getPaddingRight(),
                    getHeight() - getPaddingTop() - getPaddingBottom(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            svg.setDocumentWidth(bitmap.getWidth());
            svg.setDocumentHeight(bitmap.getHeight());
            svg.renderToCanvas(canvas);
        } catch (SVGParseException e) {
            Log.e(TAG, "problem with the svg", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "problem with the resource", e);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        paint.setColorFilter(new LightingColorFilter(0, filterColor));
    }

    @Override
    public synchronized void draw(Canvas canvas) {
        super.draw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, getPaddingLeft(), getPaddingTop(), paint);
        }
    }

    public void setColorFilter(LightingColorFilter filterColor) {
        paint.setColorFilter(filterColor);
        invalidate();
    }

    public void setFilterColor(int filterColor) {
        this.filterColor = filterColor;
        paint.setColorFilter(new LightingColorFilter(0, filterColor));
        invalidate();
    }

    public void getHitRect(Rect outRect) {
        if (touchMargin == null) {
            super.getHitRect(outRect);
            return;
        }
        outRect.set(getLeft() - touchMargin.left, getTop() - touchMargin.top, getRight() + touchMargin.right, getBottom() + touchMargin.bottom);
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
            ((RippleDrawable) getBackground()).onRelease();
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
            ((RippleDrawable) getBackground()).onCancel();
    }

    @Override
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }

    @Override
    public void setRippleDrawable(RippleDrawable rippleDrawable) {
        this.rippleDrawable = rippleDrawable;
    }
}
