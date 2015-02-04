package pl.zielony.carbon.animation;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Marcin on 2014-11-17.
 */
public class AnimUtils {
    private AnimUtils() {
    }

    public enum Style {
        None, Fade, Pop, Fly, BrightnessSaturationFade
    }

    public static Animator animateIn(View view, Style style, Animator.AnimatorListener listener) {
        switch (style) {
            case Fade:
                return fadeIn(view, listener);
            case Pop:
                return popIn(view, listener);
            case Fly:
                return flyIn(view, listener);
            case BrightnessSaturationFade:
                return view instanceof ImageView ? brightnessSaturationFadeIn((ImageView) view, listener) : fadeIn(view, listener);
        }
        if (listener != null)
            listener.onAnimationEnd(null);
        return null;
    }

    public static Animator animateOut(View view, Style style, Animator.AnimatorListener listener) {
        switch (style) {
            case Fade:
                return fadeOut(view, listener);
            case Pop:
                return popOut(view, listener);
            case Fly:
                return flyOut(view, listener);
            case BrightnessSaturationFade:
                return view instanceof ImageView ? brightnessSaturationFadeOut((ImageView) view, listener) : fadeOut(view, listener);
        }
        if (listener != null)
            listener.onAnimationEnd(null);
        return null;
    }

    public static Animator fadeIn(final View view, Animator.AnimatorListener listener) {
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getAlpha(view), 1);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewHelper.setAlpha(view, (Float) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
        return animator;
    }

    public static Animator fadeOut(final View view, Animator.AnimatorListener listener) {
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getAlpha(view), 0);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewHelper.setAlpha(view, (Float) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator popIn(final View view, Animator.AnimatorListener listener) {
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getAlpha(view), 1);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewHelper.setAlpha(view, (Float) valueAnimator.getAnimatedValue());
                ViewHelper.setScaleX(view, (Float) valueAnimator.getAnimatedValue());
                ViewHelper.setScaleY(view, (Float) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator popOut(final View view, Animator.AnimatorListener listener) {
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getAlpha(view), 0);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewHelper.setAlpha(view, (Float) valueAnimator.getAnimatedValue());
                ViewHelper.setScaleX(view, (Float) valueAnimator.getAnimatedValue());
                ViewHelper.setScaleY(view, (Float) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator flyIn(final View view, Animator.AnimatorListener listener) {
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getAlpha(view), 1);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewHelper.setAlpha(view, (Float) valueAnimator.getAnimatedValue());
                ViewHelper.setTranslationY(view, view.getHeight() / 2.0f * (1 - (Float) valueAnimator.getAnimatedValue()));
            }
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator flyOut(final View view, Animator.AnimatorListener listener) {
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getAlpha(view), 0);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null)
            animator.addListener(listener);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewHelper.setAlpha(view, (Float) valueAnimator.getAnimatedValue());
                ViewHelper.setTranslationY(view, view.getHeight() / 2.0f * (1 - (Float) valueAnimator.getAnimatedValue()));
            }
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator brightnessSaturationFadeIn(final ImageView imageView, Animator.AnimatorListener listener) {
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        animator.setInterpolator(interpolator);
        animator.setDuration(1000);
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
                brightnessMatrix.setScale(scale, scale, scale,  interpolator.getInterpolation(Math.min(fraction * 2, 1)));

                saturationMatrix.preConcat(brightnessMatrix);
                imageView.setColorFilter(new ColorMatrixColorFilter(saturationMatrix));
            }
        });
        animator.start();
        return animator;
    }

    public static ValueAnimator brightnessSaturationFadeOut(final ImageView imageView, Animator.AnimatorListener listener) {
        final ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        animator.setInterpolator(interpolator);
        animator.setDuration(1000);
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
                brightnessMatrix.setScale(scale, scale, scale, interpolator.getInterpolation(Math.min((1 - fraction) * 2, 1)));

                saturationMatrix.preConcat(brightnessMatrix);
                imageView.setColorFilter(new ColorMatrixColorFilter(saturationMatrix));
            }
        });
        animator.start();
        return animator;
    }

    public static float lerp(float interpolation, float val1, float val2) {
        return val1 * (1 - interpolation) + val2 * interpolation;
    }
}
