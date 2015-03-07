package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import java.lang.reflect.Method;

import carbon.R;
import carbon.drawable.EdgeEffect;

/**
 * Created by Marcin on 2015-02-28.
 */
public class ListView extends android.widget.ListView {
    private final int mTouchSlop;
    int edgeEffectColor;
    EdgeEffect edgeEffectTop;
    EdgeEffect edgeEffectBottom;
    private boolean drag = true;
    private float prevY;
    private int overscrollMode;

    public static final int OVER_SCROLL_ALWAYS = 0;
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    public static final int OVER_SCROLL_NEVER = 2;

    public ListView(Context context) {
        this(context, null);
    }

    public ListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public ListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ListView, defStyleAttr, 0);
        edgeEffectColor = a.getColor(R.styleable.ListView_carbon_edgeEffectColor, 0);
        a.recycle();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB && "samsung".equalsIgnoreCase(Build.MANUFACTURER)) {
            try {
                Method setEnableExcessScroll = android.widget.ListView.class.getMethod("setEnableExcessScroll", Boolean.TYPE);
                if (setEnableExcessScroll != null) {
                    setEnableExcessScroll.invoke(this, Boolean.FALSE);
                }
            } catch (Exception ignore) {
                // Silently ignore
            }
        }
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (edgeEffectTop != null) {
            final int scrollY = getScrollY();
            if (!edgeEffectTop.isFinished()) {
                final int restoreCount = canvas.save();
                final int width = getWidth() - getPaddingLeft() - getPaddingRight();

                canvas.translate(getPaddingLeft(), Math.min(0, scrollY));
                edgeEffectTop.setSize(width, getHeight());
                if (edgeEffectTop.draw(canvas)) {
                    postInvalidate();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!edgeEffectBottom.isFinished()) {
                final int restoreCount = canvas.save();
                final int width = getWidth() - getPaddingLeft() - getPaddingRight();
                final int height = getHeight();

                canvas.translate(-width + getPaddingLeft(),
                        Math.max(getScrollRange(), scrollY) + height);
                canvas.rotate(180, width, 0);
                edgeEffectBottom.setSize(width, height);
                if (edgeEffectBottom.draw(canvas)) {
                    postInvalidate();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float deltaY = prevY - ev.getY();

                if (!drag && Math.abs(deltaY) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    drag = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                if (drag) {
                    final int oldY = getScrollY();
                    final int range = getScrollRange();
                    boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS ||
                            (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

                    if (canOverscroll) {
                        float pulledToY = oldY + deltaY;
                        if (pulledToY < 0) {
                            edgeEffectTop.onPull(deltaY / getHeight(), ev.getX() / getWidth());
                            if (!edgeEffectBottom.isFinished())
                                edgeEffectBottom.onRelease();
                        } else if (pulledToY > range) {
                            edgeEffectBottom.onPull(deltaY / getHeight(), 1.f - ev.getX() / getWidth());
                            if (!edgeEffectTop.isFinished())
                                edgeEffectTop.onRelease();
                        }
                        if (edgeEffectTop != null && (!edgeEffectTop.isFinished() || !edgeEffectBottom.isFinished()))
                            postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (drag)
                    endDrag();
                break;
        }
        prevY = ev.getY();

        return result;
    }

   /* @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (oldX != x || oldY != y) {
                final int range = getScrollRange();
                final boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS ||
                        (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

                if (canOverscroll) {
                    if (y < 0 && oldY >= 0) {
                        edgeEffectTop.onAbsorb((int) mScroller.getCurrVelocity());
                    } else if (y > range && oldY <= range) {
                        edgeEffectBottom.onAbsorb((int) mScroller.getCurrVelocity());
                    }
                }
            }

            if (!awakenScrollBars()) {
                // Keep on drawing until the animation has finished.
                postInvalidate();
            }
        }
    }*/

    private void endDrag() {
        drag = false;

        if (edgeEffectTop != null) {
            edgeEffectTop.onRelease();
            edgeEffectBottom.onRelease();
        }
    }


    @Override
    public void setOverScrollMode(int mode) {
        if (mode != OVER_SCROLL_NEVER) {
            if (edgeEffectTop == null) {
                Context context = getContext();
                edgeEffectTop = new EdgeEffect(context);
                edgeEffectTop.setColor(edgeEffectColor);
                edgeEffectBottom = new EdgeEffect(context);
                edgeEffectBottom.setColor(edgeEffectColor);
            }
        } else {
            edgeEffectTop = null;
            edgeEffectBottom = null;
        }
        super.setOverScrollMode(OVER_SCROLL_NEVER);
        this.overscrollMode = mode;
    }

    public int getEdgeEffectColor() {
        return edgeEffectColor;
    }

    public void setEdgeEffectColor(int edgeEffectColor) {
        this.edgeEffectColor = edgeEffectColor;
        if(edgeEffectTop !=null)
            edgeEffectTop.setColor(edgeEffectColor);
        if(edgeEffectBottom !=null)
            edgeEffectBottom.setColor(edgeEffectColor);
    }
}
