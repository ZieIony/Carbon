package carbon.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Field;

import carbon.drawable.EdgeEffect;

/**
 * Created by Marcin on 2015-02-28.
 */
public class ScrollView extends android.widget.ScrollView {
    EdgeEffect edgeEffectTop;
    EdgeEffect edgeEffectBottom;
    private boolean mIsBeingDragged = true;
    private float prevY;
    private int overscrollMode;

    public ScrollView(Context context) {
        this(context,null);
    }

    public ScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        edgeEffectTop = new EdgeEffect(getContext());
        edgeEffectBottom = new EdgeEffect(getContext());
        setWillNotDraw(false);
        setOverScrollMode(OVER_SCROLL_ALWAYS);
    }

    @Override
    public void setOverScrollMode(int mode) {
        super.setOverScrollMode(mode);
        try {
            Field glowTopField = ScrollView.class.getDeclaredField("mEdgeGlowTop");
            glowTopField.setAccessible(true);
            glowTopField.set(this, null);
            Field glowBottomField = ScrollView.class.getDeclaredField("mEdgeGlowBottom");
            glowBottomField.setAccessible(true);
            glowBottomField.set(this, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.overscrollMode = mode;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mIsBeingDragged) {
                    final int oldY = getScrollY();
                    boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS ||
                            (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS);

                    float deltaY = prevY - ev.getY();
                    if (canOverscroll) {
                        final int pulledToY = (int) (oldY + deltaY);
                        if (pulledToY < 0) {
                            edgeEffectTop.onPull((float) deltaY / getHeight(),
                                    ev.getX() / getWidth());
                            if (!edgeEffectBottom.isFinished()) {
                                edgeEffectBottom.onRelease();
                            }
                        } else if (pulledToY > getScrollRange()) {
                            edgeEffectBottom.onPull((float) deltaY / getHeight(),
                                    1.f - ev.getX() / getWidth());
                            if (!edgeEffectTop.isFinished()) {
                                edgeEffectTop.onRelease();
                            }
                        }
                        if (edgeEffectTop != null
                                && (!edgeEffectTop.isFinished() || !edgeEffectBottom.isFinished())) {
                            postInvalidate();
                        }
                    }
                }
                break;
        }
        prevY = ev.getY();
        return true;
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

}
