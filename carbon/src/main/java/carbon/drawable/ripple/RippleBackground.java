package carbon.drawable.ripple;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Draws a ripple background.
 */
class RippleBackground extends RippleComponent {
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    private static final int OPACITY_ENTER_DURATION = 600;
    private static final int OPACITY_ENTER_DURATION_FAST = 120;
    private static final int OPACITY_EXIT_DURATION = 480;

    // Software rendering properties.
    private float mOpacity = 0;

    public RippleBackground(RippleDrawableFroyo owner, Rect bounds) {
        super(owner, bounds);
    }

    public boolean isVisible() {
        return mOpacity > 0;
    }

    @Override
    protected boolean drawSoftware(Canvas c, Paint p) {
        boolean hasContent = false;

        final int origAlpha = p.getAlpha();
        final int alpha = (int) (origAlpha * mOpacity + 0.5f);
        if (alpha > 0) {
            p.setAlpha(alpha);
            c.drawCircle(0, 0, mTargetRadius, p);
            p.setAlpha(origAlpha);
            hasContent = true;
        }

        return hasContent;
    }

    @Override
    protected Animator createSoftwareEnter(boolean fast) {
        // Linear enter based on current opacity.
        final int maxDuration = fast ? OPACITY_ENTER_DURATION_FAST : OPACITY_ENTER_DURATION;
        final int duration = (int) ((1 - mOpacity) * maxDuration);

        final ObjectAnimator opacity = ObjectAnimator.ofFloat(this, OPACITY, 1);
        AnimatorsCompat.setAutoCancel(opacity);
        //opacity.setAutoCancel(true);
        opacity.setDuration(duration);
        opacity.setInterpolator(LINEAR_INTERPOLATOR);

        return opacity;
    }

    @Override
    protected Animator createSoftwareExit() {
        final AnimatorSet set = new AnimatorSet();

        // Linear exit after enter is completed.
        final ObjectAnimator exit = ObjectAnimator.ofFloat(this, RippleBackground.OPACITY, 0);
        exit.setInterpolator(LINEAR_INTERPOLATOR);
        exit.setDuration(OPACITY_EXIT_DURATION);
        AnimatorsCompat.setAutoCancel(exit);
        //exit.setAutoCancel(true);

        final AnimatorSet.Builder builder = set.play(exit);

        // Linear "fast" enter based on current opacity.
        final int fastEnterDuration = (int) ((1 - mOpacity) * OPACITY_ENTER_DURATION_FAST);
        if (fastEnterDuration > 0) {
            final ObjectAnimator enter = ObjectAnimator.ofFloat(this, RippleBackground.OPACITY, 1);
            enter.setInterpolator(LINEAR_INTERPOLATOR);
            enter.setDuration(fastEnterDuration);
            AnimatorsCompat.setAutoCancel(enter);
            //enter.setAutoCancel(true);

            builder.after(enter);
        }

        return set;
    }

    @Override
    protected void jumpValuesToExit() {
        mOpacity = 0;
    }

    private static abstract class BackgroundProperty extends FloatProperty<RippleBackground> {
        public BackgroundProperty(String name) {
            super(name);
        }
    }

    private static final BackgroundProperty OPACITY = new BackgroundProperty("opacity") {
        @Override
        public void setValue(RippleBackground object, float value) {
            object.mOpacity = value;
            object.invalidateSelf();
        }

        @Override
        public Float get(RippleBackground object) {
            return object.mOpacity;
        }
    };
}
