package carbon.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.drawable.DefaultPrimaryColorStateList;
import carbon.drawable.EdgeEffect;

public class ViewPager extends android.support.v4.view.ViewPager implements TintedView, VisibleView {
    private int mTouchSlop;
    EdgeEffect leftGlow;
    EdgeEffect rightGlow;
    private boolean drag = true;
    private float prevX;
    private int overscrollMode;

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
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        pageChangeListenerList.add(listener);
    }

    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        pageChangeListenerList.remove(listener);
    }

    public ViewPager(Context context) {
        super(context, null);
        initViewPager(null, R.attr.carbon_viewPagerStyle);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ViewPager, R.attr.carbon_viewPagerStyle, R.styleable.ViewPager_carbon_theme), attrs);
        initViewPager(attrs, R.attr.carbon_viewPagerStyle);
    }

    public ViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ViewPager, defStyleAttr, R.styleable.ViewPager_carbon_theme), attrs);
        initViewPager(attrs, defStyleAttr);
    }

    private static int[] tintIds = new int[]{
            R.styleable.ViewPager_carbon_tint,
            R.styleable.ViewPager_carbon_tintMode,
            R.styleable.ViewPager_carbon_backgroundTint,
            R.styleable.ViewPager_carbon_backgroundTintMode,
            R.styleable.ViewPager_carbon_animateColorChanges
    };

    private void initViewPager(AttributeSet attrs, int defStyleAttr) {
        super.setOnPageChangeListener(internalOnPageChangeListener);

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ViewPager, defStyleAttr, R.style.carbon_ViewPager);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ViewPager_carbon_overScroll) {
                setOverScrollMode(a.getInt(attr, OVER_SCROLL_ALWAYS));
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
    public void setTint(ColorStateList list) {
        this.tint = list == null ? null : animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, tintAnimatorListener) : list;
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
    public void setBackgroundTintMode(@NonNull PorterDuff.Mode mode) {
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
