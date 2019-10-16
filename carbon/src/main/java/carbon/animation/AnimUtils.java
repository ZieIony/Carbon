package carbon.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import carbon.R;
import carbon.internal.MathUtils;
import carbon.view.ShadowView;
import carbon.widget.ProgressBar;

public class AnimUtils {
    private AnimUtils() {
    }

    interface AnimatorFactory {
        Animator getAnimator();
    }

    public enum Style {
        None(() -> null, () -> null),
        Fade(AnimUtils::getFadeInAnimator, AnimUtils::getFadeOutAnimator),
        Pop(AnimUtils::getPopInAnimator, AnimUtils::getPopOutAnimator),
        Fly(AnimUtils::getFlyInAnimator, AnimUtils::getFlyOutAnimator),
        Slide(AnimUtils::getSlideInAnimator, AnimUtils::getSlideOutAnimator),
        BrightnessSaturationFade(AnimUtils::getBrightnessSaturationFadeInAnimator, AnimUtils::getBrightnessSaturationFadeOutAnimator),
        ProgressWidth(AnimUtils::getProgressWidthInAnimator, AnimUtils::getProgressWidthOutAnimator);

        private AnimatorFactory inAnimator;

        private AnimatorFactory outAnimator;

        Style(AnimatorFactory inAnimator, AnimatorFactory outAnimator) {
            this.inAnimator = inAnimator;
            this.outAnimator = outAnimator;
        }

        public Animator getInAnimator() {
            return inAnimator.getAnimator();
        }

        public Animator getOutAnimator() {
            return outAnimator.getAnimator();
        }
    }

    public static ValueAnimator getFadeInAnimator() {
        ViewAnimator animator = new ViewAnimator();
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setOnSetupValuesListener(() -> {
            View view = animator.getTarget();
            if (view.getVisibility() != View.VISIBLE)
                view.setAlpha(0);
            float start = view.getAlpha();
            animator.setFloatValues(start, 1);
            animator.setDuration((long) (200 * (1 - start)));
        });
        animator.addUpdateListener(valueAnimator -> {
            View view = animator.getTarget();
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
        });
        return animator;
    }

    public static ValueAnimator getFadeOutAnimator() {
        ViewAnimator animator = new ViewAnimator();
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setOnSetupValuesListener(() -> {
            View view = animator.getTarget();
            float start = view.getAlpha();
            animator.setFloatValues(start, 0);
            animator.setDuration((long) (200 * start));
        });
        animator.addUpdateListener(valueAnimator -> {
            View view = animator.getTarget();
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
        });
        return animator;
    }

    public static Animator getPopInAnimator() {
        ViewAnimator animator = new ViewAnimator();
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setOnSetupValuesListener(() -> {
            View view = animator.getTarget();
            if (view.getVisibility() != View.VISIBLE)
                view.setAlpha(0);
            float start = view.getAlpha();
            animator.setFloatValues(start, 1);
            animator.setDuration((long) (200 * (1 - start)));
        });
        animator.addUpdateListener(valueAnimator -> {
            View view = animator.getTarget();
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
            view.setScaleX((Float) valueAnimator.getAnimatedValue());
            view.setScaleY((Float) valueAnimator.getAnimatedValue());
        });
        return animator;
    }

    public static Animator getPopOutAnimator() {
        ViewAnimator animator = new ViewAnimator();
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setOnSetupValuesListener(() -> {
            View view = animator.getTarget();
            float start = view.getAlpha();
            animator.setFloatValues(start, 0);
            animator.setDuration((long) (200 * start));
        });
        animator.addUpdateListener(valueAnimator -> {
            View view = animator.getTarget();
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
            view.setScaleX((Float) valueAnimator.getAnimatedValue());
            view.setScaleY((Float) valueAnimator.getAnimatedValue());
        });
        return animator;
    }

    public static ValueAnimator getFlyInAnimator() {
        ViewAnimator animator = new ViewAnimator();
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.setOnSetupValuesListener(() -> {
            View view = animator.getTarget();
            if (view.getVisibility() != View.VISIBLE)
                view.setAlpha(0);
            float start = view.getAlpha();
            animator.setFloatValues(start, 1);
            animator.setDuration((long) (200 * (1 - start)));
        });
        animator.addUpdateListener(valueAnimator -> {
            View view = animator.getTarget();
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
            view.setTranslationY(Math.min(view.getHeight() / 2, view.getResources().getDimension(R.dimen.carbon_1dip) * 50.0f) * (1 - (Float) valueAnimator.getAnimatedValue()));
        });
        return animator;
    }

    public static ValueAnimator getFlyOutAnimator() {
        ViewAnimator animator = new ViewAnimator();
        animator.setInterpolator(new FastOutLinearInInterpolator());
        animator.setOnSetupValuesListener(() -> {
            View view = animator.getTarget();
            float start = view.getAlpha();
            animator.setFloatValues(start, 0);
            animator.setDuration((long) (200 * start));
        });
        animator.addUpdateListener(valueAnimator -> {
            View view = animator.getTarget();
            view.setAlpha((Float) valueAnimator.getAnimatedValue());
            view.setTranslationY(Math.min(view.getHeight() / 2, view.getResources().getDimension(R.dimen.carbon_1dip) * 50.0f) * (1 - (Float) valueAnimator.getAnimatedValue()));
        });
        return animator;
    }

    public static ValueAnimator getSlideInAnimator() {
        ViewAnimator animator = new ViewAnimator();
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.setOnSetupValuesListener(() -> {
            View view = animator.getTarget();
            animator.setFloatValues(view.getTranslationY(), 0);
            int height = view.getMeasuredHeight();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof ViewGroup.MarginLayoutParams)
                height += ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            long duration = (long) (200 * Math.abs(view.getTranslationY() / height));
            animator.setDuration(duration);
        });
        animator.addUpdateListener(valueAnimator -> {
            View view = animator.getTarget();
            view.setTranslationY((Float) valueAnimator.getAnimatedValue());
        });
        return animator;
    }

    public static ValueAnimator getSlideOutAnimator() {
        return getSlideOutAnimator(Gravity.BOTTOM);
    }

    public static ValueAnimator getSlideOutAnimator(int gravity) {
        ViewAnimator animator = new ViewAnimator();
        animator.setInterpolator(new FastOutLinearInInterpolator());
        animator.setOnSetupValuesListener(() -> {
            View view = animator.getTarget();
            int height = view.getMeasuredHeight();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            boolean top = (gravity & Gravity.BOTTOM) == Gravity.BOTTOM;
            if (layoutParams != null && layoutParams instanceof ViewGroup.MarginLayoutParams)
                height += top ? ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin : ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
            animator.setFloatValues(view.getTranslationY(), top ? height : -height);
            long duration = (long) (200 * (1 - Math.abs(view.getTranslationY() / height)));
            animator.setDuration(duration);
        });
        animator.addUpdateListener(valueAnimator -> {
            View view = animator.getTarget();
            view.setTranslationY((Float) valueAnimator.getAnimatedValue());
        });
        return animator;
    }

    public static ValueAnimator getProgressWidthInAnimator() {
        ViewAnimator animator = new ViewAnimator();
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.setOnSetupValuesListener(() -> {
            ProgressBar circularProgress = (ProgressBar) animator.getTarget();
            final float arcWidth = circularProgress.getBarPadding() + circularProgress.getBarWidth();
            float start = circularProgress.getBarWidth();
            animator.setFloatValues(circularProgress.getBarWidth(), arcWidth);
            animator.setDuration((long) (100 * (arcWidth - start)));
        });
        animator.addUpdateListener(valueAnimator -> {
            ProgressBar circularProgress = (ProgressBar) animator.getTarget();
            final float arcWidth = circularProgress.getBarPadding() + circularProgress.getBarWidth();
            float value = (Float) valueAnimator.getAnimatedValue();
            circularProgress.setBarWidth(value);
            circularProgress.setBarPadding(arcWidth - value);
        });
        return animator;
    }

    public static Animator getProgressWidthOutAnimator() {
        ViewAnimator animator = new ViewAnimator();
        animator.setInterpolator(new FastOutLinearInInterpolator());
        animator.setOnSetupValuesListener(() -> {
            ProgressBar circularProgress = (ProgressBar) animator.getTarget();
            float start = circularProgress.getBarWidth();
            animator.setFloatValues(start, 0);
            animator.setDuration((long) (100 * start));
        });
        animator.addUpdateListener(valueAnimator -> {
            ProgressBar circularProgress = (ProgressBar) animator.getTarget();
            final float arcWidth = circularProgress.getBarPadding() + circularProgress.getBarWidth();
            float value = (Float) valueAnimator.getAnimatedValue();
            circularProgress.setBarWidth(value);
            circularProgress.setBarPadding(arcWidth - value);
        });
        return animator;
    }

    public static Animator getBrightnessSaturationFadeInAnimator() {
        ViewAnimator animator = new ViewAnimator();
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        animator.setInterpolator(interpolator);
        animator.setOnSetupValuesListener(() -> {
            animator.setFloatValues(0, 1);  // TODO: start values
            animator.setDuration(800);
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            ColorMatrix saturationMatrix = new ColorMatrix();
            ColorMatrix brightnessMatrix = new ColorMatrix();

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ImageView imageView = (ImageView) animator.getTarget();
                float fraction = animator.getAnimatedFraction();

                saturationMatrix.setSaturation((Float) animator.getAnimatedValue());

                float scale = 2 - interpolator.getInterpolation(Math.min(fraction * 4 / 3, 1));
                brightnessMatrix.setScale(scale, scale, scale, 1);

                saturationMatrix.preConcat(brightnessMatrix);
                imageView.setColorFilter(new ColorMatrixColorFilter(saturationMatrix));
                imageView.setAlpha(interpolator.getInterpolation(Math.min(fraction * 2, 1)));
            }
        });
        return animator;
    }

    public static Animator getBrightnessSaturationFadeOutAnimator() {
        ViewAnimator animator = new ViewAnimator();
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        animator.setInterpolator(interpolator);
        animator.setOnSetupValuesListener(() -> {
            animator.setFloatValues(1, 0);
            animator.setDuration(800);
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            ColorMatrix saturationMatrix = new ColorMatrix();
            ColorMatrix brightnessMatrix = new ColorMatrix();

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ImageView imageView = (ImageView) animator.getTarget();
                float fraction = animator.getAnimatedFraction();

                saturationMatrix.setSaturation((Float) animator.getAnimatedValue());

                float scale = 2 - interpolator.getInterpolation(Math.min((1 - fraction) * 4 / 3, 1));
                brightnessMatrix.setScale(scale, scale, scale, 1);

                saturationMatrix.preConcat(brightnessMatrix);
                imageView.setColorFilter(new ColorMatrixColorFilter(saturationMatrix));
                imageView.setAlpha(interpolator.getInterpolation(Math.min((1 - fraction) * 2, 1)));
            }
        });
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
            animator.setInterpolator(new FastOutSlowInInterpolator());
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
            animator.setInterpolator(new FastOutSlowInInterpolator());
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
            animator.setInterpolator(new FastOutSlowInInterpolator());
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
            animator.setInterpolator(new FastOutSlowInInterpolator());
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
