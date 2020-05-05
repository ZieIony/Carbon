package carbon.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import carbon.R;
import carbon.view.View;

public class ViewPagerIndicator extends View {
    ViewPager viewPager;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float indicatorPos = 0;
    private int selectedPage = 0;
    DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();

    private ValueAnimator animator;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            position = Math.round(position + positionOffset);
            if (position != selectedPage) {
                if (animator != null)
                    animator.cancel();

                animator = ValueAnimator.ofFloat(indicatorPos, position);
                animator.setDuration(200);
                if (position > selectedPage)
                    animator.setStartDelay(100);
                animator.setInterpolator(decelerateInterpolator);
                animator.addUpdateListener(animation -> {
                    indicatorPos = (float) animation.getAnimatedValue();
                    postInvalidate();
                });
                animator.start();

                selectedPage = position;
            }

        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    public ViewPagerIndicator(Context context) {
        super(context, null, R.attr.carbon_viewPagerIndicatorStyle);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_viewPagerIndicatorStyle);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerIndicator(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setViewPager(final ViewPager viewPager) {
        if (viewPager != null)
            viewPager.removeOnPageChangeListener(pageChangeListener);
        this.viewPager = viewPager;
        if (viewPager != null)
            viewPager.addOnPageChangeListener(pageChangeListener);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        int pages;
        if (viewPager != null && viewPager.getAdapter() != null) {
            pages = viewPager.getAdapter().getCount();
        } else {
            pages = 5;
        }
        paint.setStyle(Paint.Style.STROKE);
        float radius = getHeight() / 2.0f - 1;
        if (pages > 1) {
            int color = getTint().getColorForState(new int[]{isEnabled() ? android.R.attr.state_enabled : -android.R.attr.state_enabled}, getTint().getDefaultColor());
            paint.setColor(color);
            for (int i = 0; i < pages; i++)
                canvas.drawCircle(getHeight() / 2.0f + (getWidth() - getHeight()) * i / (pages - 1), getHeight() / 2.0f, radius, paint);
            paint.setStyle(Paint.Style.FILL);

            color = getTint().getColorForState(isEnabled() ?
                    new int[]{android.R.attr.state_selected, android.R.attr.state_enabled} :
                    new int[]{-android.R.attr.state_selected}, getTint().getDefaultColor());
            paint.setColor(color);
            float frac = (float) (indicatorPos - Math.floor(indicatorPos));
            paint.setAlpha((int) ((1 - frac) * Color.alpha(color)));
            canvas.drawCircle((float) (getHeight() / 2.0f + (getWidth() - getHeight()) * Math.floor(indicatorPos) / (pages - 1)), getHeight() / 2.0f, radius, paint);
            paint.setAlpha((int) (frac * Color.alpha(color)));
            canvas.drawCircle((float) (getHeight() / 2.0f + (getWidth() - getHeight()) * Math.ceil(indicatorPos) / (pages - 1)), getHeight() / 2.0f, radius, paint);
        } else {
            int color = getTint().getColorForState(new int[android.R.attr.state_selected], getTint().getDefaultColor());
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, radius, paint);
        }
    }

}
