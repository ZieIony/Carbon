package carbon.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.view.TintedView;
import carbon.view.VisibleView;

public class PagerTabStrip extends android.widget.HorizontalScrollView implements TintedView, VisibleView {  // TODO: extend carbon.widget.HorizontalScrollView
    ViewPager viewPager;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    LinearLayout content;
    private float indicatorPos = 0;
    private int selectedPage = 0;
    private float indicatorPos2 = 0;
    float indicatorHeight;
    DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    boolean fixed = false;

    private ValueAnimator animator, animator2;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            position = Math.round(position + positionOffset);
            if (position != selectedPage) {
                View view = content.getChildAt(position);
                if (view == null)
                    return; // TODO: what's really going on here? #130

                if (animator != null)
                    animator.cancel();
                if (animator2 != null)
                    animator2.cancel();

                animator = ValueAnimator.ofFloat(indicatorPos, view.getLeft());
                animator.setDuration(200);
                if (position > selectedPage)
                    animator.setStartDelay(100);
                animator.setInterpolator(decelerateInterpolator);
                animator.addUpdateListener(animation -> {
                    indicatorPos = (float) animation.getAnimatedValue();
                    postInvalidate();
                });
                animator.start();

                animator2 = ValueAnimator.ofFloat(indicatorPos2, view.getRight());
                animator2.setDuration(200);
                if (position < selectedPage)
                    animator2.setStartDelay(100);
                animator2.setInterpolator(decelerateInterpolator);
                animator2.addUpdateListener(animation -> {
                    indicatorPos2 = (float) animation.getAnimatedValue();
                    postInvalidate();
                });
                animator2.start();

                setSelectedPage(position);

                if (content.getChildAt(selectedPage).getLeft() - getScrollX() < 0) {
                    smoothScrollTo(content.getChildAt(selectedPage).getLeft(), 0);
                } else if (content.getChildAt(selectedPage).getRight() - getScrollX() > getWidth()) {
                    smoothScrollTo(content.getChildAt(selectedPage).getRight() - getWidth(), 0);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private TabBuilder tabBuilder;

    public PagerTabStrip(Context context) {
        super(context, null, R.attr.carbon_pagerTabStripStyle);
        initPagerTabStrip(null, R.attr.carbon_pagerTabStripStyle);
    }

    public PagerTabStrip(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.PagerTabStrip, R.attr.carbon_pagerTabStripStyle, R.styleable.PagerTabStrip_carbon_theme), attrs, R.attr.carbon_pagerTabStripStyle);
        initPagerTabStrip(attrs, R.attr.carbon_pagerTabStripStyle);
    }

    public PagerTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.PagerTabStrip, defStyleAttr, R.styleable.PagerTabStrip_carbon_theme), attrs, defStyleAttr);
        initPagerTabStrip(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PagerTabStrip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.PagerTabStrip, defStyleAttr, R.styleable.PagerTabStrip_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initPagerTabStrip(attrs, defStyleAttr);
    }

    private static int[] tintIds = new int[]{
            R.styleable.PagerTabStrip_carbon_tint,
            R.styleable.PagerTabStrip_carbon_tintMode,
            R.styleable.PagerTabStrip_carbon_backgroundTint,
            R.styleable.PagerTabStrip_carbon_backgroundTintMode,
            R.styleable.PagerTabStrip_carbon_animateColorChanges
    };

    private void initPagerTabStrip(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PagerTabStrip, defStyleAttr, R.style.carbon_PagerTabStrip);

        setIndicatorHeight(a.getDimension(R.styleable.PagerTabStrip_carbon_indicatorWidth, 2));
        setFixed(a.getBoolean(R.styleable.PagerTabStrip_carbon_fixedTabs, true));

        Carbon.initTint(this, a, tintIds);

        a.recycle();

        setHorizontalFadingEdgeEnabled(false);
        setHorizontalScrollBarEnabled(false);

        content = new LinearLayout(getContext());
        addView(content, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        initTabs();
    }

    public void setViewPager(final ViewPager viewPager) {
        if (viewPager != null)
            viewPager.removeOnPageChangeListener(pageChangeListener);
        this.viewPager = viewPager;
        if (viewPager != null)
            viewPager.addOnPageChangeListener(pageChangeListener);
        initTabs();
    }

    private void initTabs() {
        content.removeAllViews();

        if (viewPager == null)
            return;
        final PagerAdapter adapter = viewPager.getAdapter();

        if (viewPager.getAdapter() == null)
            return;

        if (tabBuilder == null) {
            tabBuilder = position -> {
                View tab = inflate(getContext(), R.layout.carbon_tab, null);
                ((TextView) tab.findViewById(R.id.carbon_tabText)).setText(getViewPager().getAdapter().getPageTitle(position).toString().toUpperCase());
                return tab;
            };
        }
        for (int i = 0; i < adapter.getCount(); i++) {
            View tab = tabBuilder.getView(i);
            content.addView(tab, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            tab.setSelected(i == 0);
            final int finalI = i;
            tab.setOnClickListener(__ -> viewPager.setCurrentItem(finalI));
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (content.getChildCount() == 0)
            return;
        if (indicatorPos == indicatorPos2)
            indicatorPos2 = content.getChildAt(selectedPage).getWidth();
        paint.setColor(getTint().getColorForState(getDrawableState(), getTint().getDefaultColor()));
        canvas.drawRect(indicatorPos, getHeight() - indicatorHeight, indicatorPos2, getHeight(), paint);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
        setFillViewport(fixed);
    }

    public float getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setIndicatorHeight(float indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        postInvalidate();
    }

    public void setTabBuilder(TabBuilder tabBuilder) {
        this.tabBuilder = tabBuilder;
        initTabs();
    }

    public void setSelectedPage(int position) {
        if (viewPager == null)
            return;
        if (content.getChildCount() > selectedPage)
            content.getChildAt(selectedPage).setSelected(false);
        selectedPage = position;
        if (content.getChildCount() > selectedPage)
            content.getChildAt(selectedPage).setSelected(true);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, selectedPage, getScrollX(), indicatorPos, indicatorPos2);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setSelectedPage(savedState.getSelectedPage());
        indicatorPos = savedState.getIndicatorPos();
        indicatorPos2 = savedState.getIndicatorPos2();
        post(new Runnable() {
            public void run() {
                setScrollX(savedState.getScroll());
            }
        });
    }

    @Override
    protected void dispatchSaveInstanceState(@NonNull SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(@NonNull SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }

    protected static class SavedState extends BaseSavedState {
        private final int selectedPage;
        private final int scroll;
        private final float indicatorPos;
        private final float indicatorPos2;

        private SavedState(Parcelable superState, int selectedPage, int scrollX, float indicatorPos, float indicatorPos2) {
            super(superState);
            this.selectedPage = selectedPage;
            this.scroll = scrollX;
            this.indicatorPos = indicatorPos;
            this.indicatorPos2 = indicatorPos2;
        }

        private SavedState(Parcel in) {
            super(in);
            selectedPage = in.readInt();
            scroll = in.readInt();
            indicatorPos = in.readFloat();
            indicatorPos2 = in.readFloat();
        }

        public int getSelectedPage() {
            return selectedPage;
        }

        public int getScroll() {
            return scroll;
        }

        public float getIndicatorPos() {
            return indicatorPos;
        }

        public float getIndicatorPos2() {
            return indicatorPos2;
        }

        @Override
        public void writeToParcel(@NonNull Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(selectedPage);
            destination.writeInt(scroll);
            destination.writeFloat(indicatorPos);
            destination.writeFloat(indicatorPos2);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
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
        /*if (tint == null)
            return;
        int color = tint.getColorForState(getDrawableState(), tint.getDefaultColor());
        if (leftGlow != null)
            leftGlow.setColor(color);
        if (rightGlow != null)
            rightGlow.setColor(color);
        if (topGlow != null)
            topGlow.setColor(color);
        if (bottomGlow != null)
            bottomGlow.setColor(color);
        scrollBarDrawable = null;*/
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
        this.backgroundTint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, backgroundTintAnimatorListener) : list;
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
    public void setBackgroundTintMode(@Nullable PorterDuff.Mode mode) {
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
            setTintList(AnimatedColorStateList.fromList(tint, tintAnimatorListener));
        if (backgroundTint != null && !(backgroundTint instanceof AnimatedColorStateList))
            setBackgroundTintList(AnimatedColorStateList.fromList(backgroundTint, backgroundTintAnimatorListener));
    }
}
