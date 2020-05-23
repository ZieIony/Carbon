package carbon.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.core.view.ViewCompat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.drawable.EdgeEffect;
import carbon.view.MarginView;
import carbon.view.TintedView;
import carbon.view.VisibleView;

public class ViewPager extends androidx.viewpager.widget.ViewPager implements TintedView, VisibleView, MarginView {
    private int mTouchSlop;
    EdgeEffect leftGlow;
    EdgeEffect rightGlow;
    private boolean drag = true;
    private float prevX;
    private int overscrollMode;
    private boolean swipeEnabled;

    public static final int OVER_SCROLL_ALWAYS = 0;
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    public static final int OVER_SCROLL_NEVER = 2;

    private final OnPageChangeListener internalOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            for (OnPageChangeListener listener : pageChangeListenerList)
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            for (OnPageChangeListener listener : pageChangeListenerList)
                listener.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            for (OnPageChangeListener listener : pageChangeListenerList)
                listener.onPageScrollStateChanged(state);
        }
    };
    List<OnPageChangeListener> pageChangeListenerList = new ArrayList<>();

    /**
     * @param listener page changed listener
     */
    public void addOnPageChangeListener(@NotNull OnPageChangeListener listener) {
        pageChangeListenerList.add(listener);
    }

    public void removeOnPageChangeListener(@NotNull OnPageChangeListener listener) {
        pageChangeListenerList.remove(listener);
    }

    public ViewPager(Context context) {
        super(context, null);
        initViewPager(null, R.attr.carbon_viewPagerStyle, R.style.carbon_ViewPager);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager(attrs, R.attr.carbon_viewPagerStyle, R.style.carbon_ViewPager);
    }

    public ViewPager(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs);
        initViewPager(attrs, defStyleAttr, R.style.carbon_ViewPager);
    }

    public ViewPager(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs);
        initViewPager(attrs, defStyleAttr, defStyleRes);
    }

    private static int[] tintIds = new int[]{
            R.styleable.ViewPager_carbon_tint,
            R.styleable.ViewPager_carbon_tintMode,
            R.styleable.ViewPager_carbon_backgroundTint,
            R.styleable.ViewPager_carbon_backgroundTintMode,
            R.styleable.ViewPager_carbon_animateColorChanges
    };

    private void initViewPager(AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super.setOnPageChangeListener(internalOnPageChangeListener);

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ViewPager, defStyleAttr, defStyleRes);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ViewPager_carbon_overScroll) {
                setOverScrollMode(a.getInt(attr, OVER_SCROLL_ALWAYS));
            } else if (attr == R.styleable.ViewPager_carbon_swipeEnabled) {
                setSwipeEnabled(a.getBoolean(attr, true));
            }
        }

        Carbon.initTint(this, a, tintIds);

        a.recycle();

        setWillNotDraw(false);
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() == 0) {
            return getWidth();
        }
        if (getChildCount() > 0 && getAdapter() != null) {
            if (getCurrentItem() == getAdapter().getCount() - 1) {
                View child = getChildAt(getChildCount() - 1);
                scrollRange = Math.max(0,
                        child.getRight() - (getWidth() - getPaddingRight() - getPaddingLeft()));
            } else {
                scrollRange = Integer.MAX_VALUE;
            }
        }
        return scrollRange;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        this.swipeEnabled = swipeEnabled;
    }

    public boolean isSwipeEnabled() {
        return swipeEnabled;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return swipeEnabled && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return swipeEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (leftGlow != null) {
            final int scrollX = getScrollX();
            if (!leftGlow.isFinished()) {
                final int restoreCount = canvas.save();
                final int height = getHeight() - getPaddingTop() - getPaddingBottom();

                canvas.rotate(270);
                canvas.translate(-height + getPaddingTop(), Math.min(0, scrollX));
                leftGlow.setSize(height, getWidth());
                if (leftGlow.draw(canvas)) {
                    postInvalidate();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!rightGlow.isFinished()) {
                final int restoreCount = canvas.save();
                final int width = getWidth();
                final int height = getHeight() - getPaddingTop() - getPaddingBottom();

                canvas.rotate(90);
                canvas.translate(-getPaddingTop(),
                        -(Math.max(getScrollRange(), scrollX) + width));
                rightGlow.setSize(height, width);
                if (rightGlow.draw(canvas)) {
                    postInvalidate();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
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
                        float pulledToX = oldX + deltaX;
                        if (pulledToX < 0) {
                            leftGlow.onPull(deltaX / getWidth(), 1.f - ev.getY() / getHeight());
                            if (!rightGlow.isFinished())
                                rightGlow.onRelease();
                        } else if (pulledToX > range) {
                            rightGlow.onPull(deltaX / getWidth(), ev.getY() / getHeight());
                            if (!leftGlow.isFinished())
                                leftGlow.onRelease();
                        }
                        if (leftGlow != null && (!leftGlow.isFinished() || !rightGlow.isFinished()))
                            postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (drag) {
                    drag = false;

                    if (leftGlow != null) {
                        leftGlow.onRelease();
                        rightGlow.onRelease();
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
            if (leftGlow == null) {
                Context context = getContext();
                leftGlow = new EdgeEffect(context);
                rightGlow = new EdgeEffect(context);
                updateTint();
            }
        } else {
            leftGlow = null;
            rightGlow = null;
        }
        super.setOverScrollMode(OVER_SCROLL_NEVER);
        this.overscrollMode = mode;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateTint();
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

    private void updateTint() {
        if (tint == null)
            return;
        int color = tint.getColorForState(getDrawableState(), tint.getDefaultColor());
        if (leftGlow != null)
            leftGlow.setColor(color);
        if (rightGlow != null)
            rightGlow.setColor(color);
        /*if (topGlow != null)
            topGlow.setColor(color);
        if (bottomGlow != null)
            bottomGlow.setColor(color);*/
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
        if (getBackground() == null)
            return;
        if (backgroundTint != null && backgroundTintMode != null) {
            int color = backgroundTint.getColorForState(getDrawableState(), backgroundTint.getDefaultColor());
            getBackground().setColorFilter(new PorterDuffColorFilter(color, tintMode));
        } else {
            getBackground().setColorFilter(null);
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
        if (this.animateColorChanges == animateColorChanges)
            return;
        this.animateColorChanges = animateColorChanges;
        setTintList(tint);
        setBackgroundTintList(backgroundTint);
    }


    // -------------------------------
    // scroll bars
    // -------------------------------

    protected void onDrawHorizontalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b) {
        scrollBar.setColorFilter(tint != null ? tint.getColorForState(getDrawableState(), tint.getDefaultColor()) : Color.WHITE, tintMode);
        scrollBar.setBounds(l, t, r, b);
        scrollBar.draw(canvas);
    }

    protected void onDrawVerticalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b) {
        scrollBar.setColorFilter(tint != null ? tint.getColorForState(getDrawableState(), tint.getDefaultColor()) : Color.WHITE, tintMode);
        scrollBar.setBounds(l, t, r, b);
        scrollBar.draw(canvas);
    }
}
