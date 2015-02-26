package carbon.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.ValueAnimator;

import carbon.R;

/**
 * Created by Marcin on 2015-02-26.
 */
public class PagerTabStrip extends HorizontalScrollView {
    private ViewPager viewPager;
    private int tabResId = R.layout.carbon_tab;
    private Paint paint = new Paint();
    LinearLayout content;
    private float indicatorPos = 0;
    private int selectedPage = 0;
    private float indicatorPos2 = 0;
    float indicatorHeight;
    DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();

    public PagerTabStrip(Context context) {
        super(context);
        init();
    }

    public PagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagerTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFillViewport(true);

        content = new LinearLayout(getContext());
        addView(content, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        indicatorHeight = getResources().getDimension(R.dimen.dip)*2;
    }

    public void setViewPager(final ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                position= (int)Math.round(position+positionOffset);
                if (position != selectedPage) {
                    ValueAnimator.clearAllAnimations();
                    View view = content.getChildAt(position);

                    ValueAnimator animator = ValueAnimator.ofFloat(indicatorPos, view.getLeft());
                    animator.setDuration(200);
                    if(position>selectedPage)
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

                    ValueAnimator animator2 = ValueAnimator.ofFloat(indicatorPos2, view.getRight());
                    animator2.setDuration(200);
                    if(position<selectedPage)
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

                    content.getChildAt(selectedPage).findViewById(R.id.carbon_tabText).setSelected(false);
                    selectedPage = position;
                    content.getChildAt(selectedPage).findViewById(R.id.carbon_tabText).setSelected(true);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        PagerAdapter adapter = viewPager.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            View tab = inflate(getContext(), tabResId, null);
            ((TextView) tab.findViewById(R.id.carbon_tabText)).setText(adapter.getPageTitle(i).toString().toUpperCase());
            content.addView(tab, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            tab.findViewById(R.id.carbon_tabText).setSelected(i==0);
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
        if(indicatorPos==indicatorPos2)
            indicatorPos2 = content.getChildAt(0).getWidth();
        paint.setColor(Color.RED);
        canvas.drawRect(indicatorPos, getHeight() - indicatorHeight, indicatorPos2, getHeight(), paint);
    }
}
