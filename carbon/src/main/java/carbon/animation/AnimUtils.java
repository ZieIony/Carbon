package carbon.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import carbon.R;
import carbon.internal.MathUtils;
import carbon.shadow.ShadowView;
import carbon.widget.ProgressBar;

public class AnimUtils {
    private AnimUtils() {
    }

    public enum Style {
        None, Fade, Pop, Fly, BrightnessSaturationFade, ProgressWidth
    }

    public static ValueAnimator animateIn(View view, Style style, Animator.AnimatorListener listener) {
        switch (style) {
            case Fade:
                return fadeIn(view, listener);
            case Pop:
                return popIn(view, listener);
            case Fly:
                return flyIn(view, listener);
            case BrightnessSaturationFade:
                return view instanceof ImageView ? brightnessSaturationFadeIn((ImageView) view, listener) : fadeIn(view, listener);
            case ProgressWidth:
                return view instanceof ProgressBar ? progressWidthIn((ProgressBar) view, listener) : fadeIn(view, listener);
        }
        if (listener != null)
            listener.onAnimationEnd(null);
        return null;
    }

    public static ValueAnimator animateOut(View view, Style style, Animator.AnimatorListener listener) {
        switch (style) {
            case Fade:
                return fadeOut(view, listener);
            case Pop:
                return popOut(view, listener);
            case Fly:
                return flyOut(view, listener);
            case BrightnessSaturationFade:
                return view instanceof ImageView ? brightnessSaturationFadeOut((ImageView) view, listener) : fadeOut(view, listener);
            case ProgressWidth:
                return view instanceof ProgressBar ? progressWidthOut((ProgressBar) view, listener) : fadeOut(view, listener);
        }
        if (listener != null)
            listener.onAnimationEnd(null);
        return null;
    }

    public static ValueAnimator fadeIn(final View view, Animator.AnimatorListener listener) {
        if (view.getVisibility() != View.VISIBLE)
            view.setAlpha(0);
        float start = view.getAlpha();
        ValueAnimator animator = ValueAnimator.ofFloat(start, 1);
        animator.setDuration((long) (200 * (1 - start)));
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(valueAnimator -> {
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
            if (view.getParent() != null)
                ((View) view.getParent()).postInvalidate();
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator fadeOut(final View view, Animator.AnimatorListener listener) {
        float start = view.getAlpha();
        ValueAnimator animator = ValueAnimator.ofFloat(start, 0);
        animator.setDuration((long) (200 * start));
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(valueAnimator -> {
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
            if (view.getParent() != null)
                ((View) view.getParent()).postInvalidate();
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator popIn(final View view, Animator.AnimatorListener listener) {
        if (view.getVisibility() != View.VISIBLE)
            view.setAlpha(0);
        float start = view.getAlpha();
        ValueAnimator animator = ValueAnimator.ofFloat(start, 1);
        animator.setDuration((long) (200 * (1 - start)));
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(valueAnimator -> {
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
            view.setScaleX((Float) valueAnimator.getAnimatedValue());
            view.setScaleY((Float) valueAnimator.getAnimatedValue());
            if (view.getParent() != null)
                ((View) view.getParent()).postInvalidate();
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator popOut(final View view, Animator.AnimatorListener listener) {
        float start = view.getAlpha();
        ValueAnimator animator = ValueAnimator.ofFloat(start, 0);
        animator.setDuration((long) (200 * start));
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(valueAnimator -> {
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
            view.setScaleX((Float) valueAnimator.getAnimatedValue());
            view.setScaleY((Float) valueAnimator.getAnimatedValue());
            if (view.getParent() != null)
                ((View) view.getParent()).postInvalidate();
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator flyIn(final View view, Animator.AnimatorListener listener) {
        if (view.getVisibility() != View.VISIBLE)
            view.setAlpha(0);
        float start = view.getAlpha();
        ValueAnimator animator = ValueAnimator.ofFloat(start, 1);
        animator.setDuration((long) (200 * (1 - start)));
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(valueAnimator -> {
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
            view.setTranslationY(Math.min(view.getHeight() / 2, view.getResources().getDimension(R.dimen.carbon_1dip) * 50.0f) * (1 - (Float) valueAnimator.getAnimatedValue()));
            if (view.getParent() != null)
                ((View) view.getParent()).postInvalidate();
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator flyOut(final View view, Animator.AnimatorListener listener) {
        float start = view.getAlpha();
        ValueAnimator animator = ValueAnimator.ofFloat(start, 0);
        animator.setDuration((long) (200 * start));
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(valueAnimator -> {
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
            view.setTranslationY(Math.min(view.getHeight() / 2, view.getResources().getDimension(R.dimen.carbon_1dip) * 50.0f) * (1 - (Float) valueAnimator.getAnimatedValue()));
            if (view.getParent() != null)
                ((View) view.getParent()).postInvalidate();
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator progressWidthIn(final ProgressBar circularProgress, Animator.AnimatorListener listener) {
        final float arcWidth = circularProgress.getBarPadding() + circularProgress.getBarWidth();
        float start = circularProgress.getBarWidth();
        ValueAnimator animator = ValueAnimator.ofFloat(circularProgress.getBarWidth(), arcWidth);
        animator.setDuration((long) (100 * (arcWidth - start)));
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(valueAnimator -> {
            float value = (Float) valueAnimator.getAnimatedValue();
            circularProgress.setBarWidth(value);
            circularProgress.setBarPadding(arcWidth - value);
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator progressWidthOut(final ProgressBar circularProgress, Animator.AnimatorListener listener) {
        final float arcWidth = circularProgress.getBarPadding() + circularProgress.getBarWidth();
        float start = circularProgress.getBarWidth();
        ValueAnimator animator = ValueAnimator.ofFloat(start, 0);
        animator.setDuration((long) (100 * start));
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(valueAnimator -> {
            float value = (Float) valueAnimator.getAnimatedValue();
            circularProgress.setBarWidth(value);
            circularProgress.setBarPadding(arcWidth - value);
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator brightnessSaturationFadeIn(final ImageView imageView, Animator.AnimatorListener listener) {
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        animator.setInterpolator(interpolator);
        animator.setDuration(800);
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            ColorMatrix saturationMatrix = new ColorMatrix();
            ColorMatrix brightnessMatrix = new ColorMatrix();

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = animator.getAnimatedFraction();

                saturationMatrix.setSaturation((Float) animator.getAnimatedValue());

                float scale = 2 - interpolator.getInterpolation(Math.min(fraction * 4 / 3, 1));
                brightnessMatrix.setScale(scale, scale, scale, 1);

                saturationMatrix.preConcat(brightnessMatrix);
                imageView.setColorFilter(new ColorMatrixColorFilter(saturationMatrix));
                imageView.setAlpha(interpolator.getInterpolation(Math.min(fraction * 2, 1)));

                if (imageView.getParent() != null)
                    ((View) imageView.getParent()).postInvalidate();
            }
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator brightnessSaturationFadeOut(final ImageView imageView, Animator.AnimatorListener listener) {
        final ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        animator.setInterpolator(interpolator);
        animator.setDuration(800);
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            ColorMatrix saturationMatrix = new ColorMatrix();
            ColorMatrix brightnessMatrix = new ColorMatrix();

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = animator.getAnimatedFraction();

                saturationMatrix.setSaturation((Float) animator.getAnimatedValue());

                float scale = 2 - interpolator.getInterpolation(Math.min((1 - fraction) * 4 / 3, 1));
                brightnessMatrix.setScale(scale, scale, scale, 1);

                saturationMatrix.preConcat(brightnessMatrix);
                imageView.setColorFilter(new ColorMatrixColorFilter(saturationMatrix));
                imageView.setAlpha(interpolator.getInterpolation(Math.min((1 - fraction) * 2, 1)));

                if (imageView.getParent() != null)
                    ((View) imageView.getParent()).postInvalidate();
            }
        });
        animator.start();
        return animator;
    }

    public static int lerpColor(float interpolation, int val1, int val2) {
        int a = (int) MathUtils.lerp(val1 >> 24, val2 >> 24, interpolation);
        int r = (int) MathUtils.lerp((val1 >> 16) & 0xff, (val2 >> 16) & 0xff, interpolation);
        int g = (int) MathUtils.lerp((val1 >> 8) & 0xff, (val2 >> 8) & 0xff, interpolation);
        int b = (int) MathUtils.lerp(val1 & 0xff, val2 & 0xff, interpolation);
        return Color.argb(a, r, g, b);
    }

    public static void setupElevationAnimator(StateAnimator stateAnimator, final ShadowView view) {
        {
            final ValueAnimator animator = ValueAnimator.ofFloat(0, 0);
            animator.setDuration(200);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animator.setFloatValues(view.getTranslationZ(), ((View) view).getResources().getDimension(R.dimen.carbon_translationButton));
                }
            };
            animator.addUpdateListener(animation -> view.setTranslationZ((Float) animation.getAnimatedValue()));
            stateAnimator.addState(new int[]{android.R.attr.state_pressed}, animator, animatorListener);
        }
        {
            final ValueAnimator animator = ValueAnimator.ofFloat(0, 0);
            animator.setDuration(200);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animator.setFloatValues(view.getTranslationZ(), 0);
                }
            };
            animator.addUpdateListener(animation -> view.setTranslationZ((Float) animation.getAnimatedValue()));
            stateAnimator.addState(new int[]{-android.R.attr.state_pressed, android.R.attr.state_enabled}, animator, animatorListener);
        }
        {
            final ValueAnimator animator = ValueAnimator.ofFloat(0, 0);
            animator.setDuration(200);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animator.setFloatValues(view.getElevation(), 0);
                }
            };
            animator.addUpdateListener(animation -> view.setTranslationZ((Float) animation.getAnimatedValue()));
            stateAnimator.addState(new int[]{android.R.attr.state_enabled}, animator, animatorListener);
        }
        {
            final ValueAnimator animator = ValueAnimator.ofFloat(0, 0);
            animator.setDuration(200);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animator.setFloatValues(view.getTranslationZ(), -view.getElevation());
                }
            };
            animator.addUpdateListener(animation -> view.setTranslationZ((Float) animation.getAnimatedValue()));
            stateAnimator.addState(new int[]{-android.R.attr.state_enabled}, animator, animatorListener);
        }
    }

}
