package carbon.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.view.TintedView;
import carbon.view.VisibleView;

public class ViewPagerIndicator extends View implements TintedView, VisibleView {
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
        initViewPagerIndicator(null, R.attr.carbon_viewPagerIndicatorStyle);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ViewPagerIndicator, R.attr.carbon_viewPagerIndicatorStyle, R.styleable.ViewPagerIndicator_carbon_theme), attrs, R.attr.carbon_viewPagerIndicatorStyle);
        initViewPagerIndicator(attrs, R.attr.carbon_viewPagerIndicatorStyle);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ViewPagerIndicator, defStyleAttr, R.styleable.ViewPagerIndicator_carbon_theme), attrs, defStyleAttr);
        initViewPagerIndicator(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ViewPagerIndicator, defStyleAttr, R.styleable.ViewPagerIndicator_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initViewPagerIndicator(attrs, defStyleAttr);
    }

    private static int[] tintIds = new int[]{
            R.styleable.ViewPagerIndicator_carbon_tint,
            R.styleable.ViewPagerIndicator_carbon_tintMode,
            R.styleable.ViewPagerIndicator_carbon_backgroundTint,
            R.styleable.ViewPagerIndicator_carbon_backgroundTintMode,
            R.styleable.ViewPagerIndicator_carbon_animateColorChanges
    };

    private void initViewPagerIndicator(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, defStyleAttr, R.style.carbon_ViewPagerIndicator);

        Carbon.initTint(this, a, tintIds);

        a.recycle();
    }

    public void setViewPager(final ViewPager viewPager) {
        if (viewPager != null)
            viewPager.removeOnPageChangeListener(pageChangeListener);
        this.viewPager = viewPager;
        if (viewPager != null)
            viewPager.addOnPageChangeListener(pageChangeListener);
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
        int color = getTint().getColorForState(getDrawableState(), getTint().getDefaultColor());
        paint.setColor(color);
        float radius = getHeight() / 2.0f - 1;
        if (pages > 1) {
            for (int i = 0; i < pages; i++)
                canvas.drawCircle(getHeight() / 2.0f + (getWidth() - getHeight()) * i / (pages - 1), getHeight() / 2.0f, radius, paint);
            paint.setStyle(Paint.Style.FILL);
            float frac = (float) (indicatorPos - Math.floor(indicatorPos));
            paint.setAlpha((int) ((1 - frac) * Color.alpha(color)));
            canvas.drawCircle((float) (getHeight() / 2.0f + (getWidth() - getHeight()) * Math.floor(indicatorPos) / (pages - 1)), getHeight() / 2.0f, radius, paint);
            paint.setAlpha((int) (frac * Color.alpha(color)));
            canvas.drawCircle((float) (getHeight() / 2.0f + (getWidth() - getHeight()) * Math.ceil(indicatorPos) / (pages - 1)), getHeight() / 2.0f, radius, paint);
        } else {
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, radius, paint);
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
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

    @Deprecated
    public void setTint(ColorStateList list) {
        setTintList(list);
    }

    @Override
    public void setTintList(ColorStateList list) {
        this.tint = list == null ? null : animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, tintAnimatorListener) : list;
        updateTint();
    }

    @Override
    public void setTint(int color) {
        setTint(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }

    private void updateTint() {
        if (tint == null)
            return;
        tint.getColorForState(getDrawableState(), tint.getDefaultColor());
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

    @Deprecated
    public void setBackgroundTint(ColorStateList list) {
        setBackgroundTintList(list);
    }

    @Override
    public void setBackgroundTintList(ColorStateList list) {
        this.backgroundTint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, backgroundTintAnimatorListener) : list;
        updateBackgroundTint();
    }

    @Override
    public void setBackgroundTint(int color) {
        setBackgroundTint(ColorStateList.valueOf(color));
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
            setTint(AnimatedColorStateList.fromList(tint, tintAnimatorListener));
        if (backgroundTint != null && !(backgroundTint instanceof AnimatedColorStateList))
            setBackgroundTint(AnimatedColorStateList.fromList(backgroundTint, backgroundTintAnimatorListener));
    }
}
