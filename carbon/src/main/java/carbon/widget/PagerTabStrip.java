package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.ValueAnimator;

import carbon.R;

/**
 * Created by Marcin on 2015-02-26.
 */
public class PagerTabStrip extends HorizontalScrollView {
    ViewPager viewPager;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    LinearLayout content;
    private float indicatorPos = 0;
    private int selectedPage = 0;
    private float indicatorPos2 = 0;
    float indicatorHeight;
    int indicatorColor = Color.RED;
    DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    boolean fixed = false;

    private ValueAnimator animator, animator2;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            position = (int) Math.round(position + positionOffset);
            if (position != selectedPage) {
                View view = content.getChildAt(position);

                if (animator != null)
                    animator.cancel();
                if (animator2 != null)
                    animator2.cancel();

                animator = ValueAnimator.ofFloat(indicatorPos, view.getLeft());
                animator.setDuration(200);
                if (position > selectedPage)
                    animator.setStartDelay(100);
                animator.setInterpolator(decelerateInterpolator);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        indicatorPos = (float) animation.getAnimatedValue();
                        postInvalidate();
                    }
                });
                animator.start();

                animator2 = ValueAnimator.ofFloat(indicatorPos2, view.getRight());
                animator2.setDuration(200);
                if (position < selectedPage)
                    animator2.setStartDelay(100);
                animator2.setInterpolator(decelerateInterpolator);
                animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        indicatorPos2 = (float) animation.getAnimatedValue();
                        postInvalidate();
                    }
                });
                animator2.start();

                content.getChildAt(selectedPage).setSelected(false);
                selectedPage = position;
                content.getChildAt(selectedPage).setSelected(true);

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
        this(context, null);
    }

    public PagerTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_pagerTabStripStyle);
    }

    public PagerTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PagerTabStrip, defStyleAttr, 0);

        setIndicatorColor(a.getColor(R.styleable.PagerTabStrip_carbon_indicatorColor, 0));
        setIndicatorHeight(a.getDimension(R.styleable.PagerTabStrip_carbon_indicatorWidth, 2));
        setFixed(a.getBoolean(R.styleable.PagerTabStrip_carbon_fixedTabs, true));

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
        viewPager.addOnPageChangeListener(pageChangeListener);
        initTabs();
    }

    private void initTabs() {
        if (content == null)
            return;

        content.removeAllViews();

        if (viewPager == null)
            return;
        final PagerAdapter adapter = viewPager.getAdapter();

        if (adapter == null)
            return;

        if (tabBuilder == null) {
            tabBuilder = new TabBuilder() {
                @Override
                public View getView(int position) {
                    View tab = inflate(getContext(), R.layout.carbon_tab, null);
                    ((TextView) tab.findViewById(R.id.carbon_tabText)).setText(adapter.getPageTitle(position).toString().toUpperCase());
                    return tab;
                }
            };
        }
        for (int i = 0; i < adapter.getCount(); i++) {
            View tab = tabBuilder.getView(i);
            content.addView(tab, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            tab.setSelected(i == 0);
            final int finalI = i;
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(finalI);
                }
            });
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (content.getChildCount() == 0)
            return;
        if (indicatorPos == indicatorPos2)
            indicatorPos2 = content.getChildAt(selectedPage).getWidth();
        paint.setColor(indicatorColor);
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

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public void setTabBuilder(TabBuilder tabBuilder) {
        this.tabBuilder = tabBuilder;
        initTabs();
    }
}
