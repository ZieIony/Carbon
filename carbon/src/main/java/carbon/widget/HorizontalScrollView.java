package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import carbon.R;
import carbon.drawable.EdgeEffect;

/**
 * Created by Marcin on 2015-02-28.
 */
public class HorizontalScrollView extends android.widget.HorizontalScrollView {
    private final int mTouchSlop;
    int edgeEffectColor;
    EdgeEffect edgeEffectLeft;
    EdgeEffect edgeEffectRight;
    private boolean drag = true;
    private float prevX;
    private int overscrollMode;

    public static final int OVER_SCROLL_ALWAYS = 0;
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    public static final int OVER_SCROLL_NEVER = 2;

    public HorizontalScrollView(Context context) {
        this(context, null);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.horizontalScrollViewStyle);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalScrollView, defStyleAttr, 0);
        edgeEffectColor = a.getColor(R.styleable.HorizontalScrollView_carbon_edgeEffectColor, 0);
        a.recycle();
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getWidth() - (getWidth() - getPaddingRight() - getPaddingLeft()));
        }
        return scrollRange;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (edgeEffectLeft != null) {
            final int scrollX = getScrollX();
            if (!edgeEffectLeft.isFinished()) {
                final int restoreCount = canvas.save();
                final int height = getHeight() - getPaddingTop() - getPaddingBottom();

                canvas.rotate(270);
                canvas.translate(-height + getPaddingTop(), Math.min(0, scrollX));
                edgeEffectLeft.setSize(height, getWidth());
                if (edgeEffectLeft.draw(canvas)) {
                    postInvalidate();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!edgeEffectRight.isFinished()) {
                final int restoreCount = canvas.save();
                final int width = getWidth();
                final int height = getHeight() - getPaddingTop() - getPaddingBottom();

                canvas.rotate(90);
                canvas.translate(-getPaddingTop(),
                        -(Math.max(getScrollRange(), scrollX) + width));
                edgeEffectRight.setSize(height, width);
                if (edgeEffectRight.draw(canvas)) {
                    postInvalidate();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float deltaX = prevX - ev.getX();

                if (!drag && Math.abs(deltaX) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    drag = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (drag) {
                    final int oldX = getScrollX();
                    final int range = getScrollRange();
                    boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS ||
                            (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

                    if (canOverscroll) {
                        float pulledToY = oldX + deltaX;
                        if (pulledToY < 0) {
                            edgeEffectLeft.onPull(deltaX / getWidth(), ev.getY() / getHeight());
                            if (!edgeEffectRight.isFinished())
                                edgeEffectRight.onRelease();
                        } else if (pulledToY > range) {
                            edgeEffectRight.onPull(deltaX / getWidth(), 1.f - ev.getY() / getHeight());
                            if (!edgeEffectLeft.isFinished())
                                edgeEffectLeft.onRelease();
                        }
                        if (edgeEffectLeft != null && (!edgeEffectLeft.isFinished() || !edgeEffectRight.isFinished()))
                            postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (drag){
                    drag = false;

                    if (edgeEffectLeft != null) {
                        edgeEffectLeft.onRelease();
                        edgeEffectRight.onRelease();
                    }
                }
                break;
        }
        prevX = ev.getX();

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setOverScrollMode(int mode) {
        if (mode != OVER_SCROLL_NEVER) {
            if (edgeEffectLeft == null) {
                Context context = getContext();
                edgeEffectLeft = new EdgeEffect(context);
                edgeEffectLeft.setColor(edgeEffectColor);
                edgeEffectRight = new EdgeEffect(context);
                edgeEffectRight.setColor(edgeEffectColor);
            }
        } else {
            edgeEffectLeft = null;
            edgeEffectRight = null;
        }
        super.setOverScrollMode(OVER_SCROLL_NEVER);
        this.overscrollMode = mode;
    }

    public int getEdgeEffectColor() {
        return edgeEffectColor;
    }

    public void setEdgeEffectColor(int edgeEffectColor) {
        this.edgeEffectColor = edgeEffectColor;
        if (edgeEffectLeft != null)
            edgeEffectLeft.setColor(edgeEffectColor);
        if (edgeEffectRight != null)
            edgeEffectRight.setColor(edgeEffectColor);
    }
}
