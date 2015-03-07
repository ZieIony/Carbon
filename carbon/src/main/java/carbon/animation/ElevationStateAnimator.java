package carbon.animation;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.ValueAnimator;

import carbon.R;
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2015-02-20.
 */
public class ElevationStateAnimator extends StateAnimator {
    private final ShadowView view;
    private final float pressedElevation;

    public ElevationStateAnimator(ShadowView view) {
        this.view = view;
        pressedElevation = ((View) view).getResources().getDimension(R.dimen.carbon_elevationLow);
    }

    @Override
    protected void onPressedChanged() {
        if (pressed) {
            setTranslationZ(pressedElevation);
        } else {
            setTranslationZ(0);
        }
    }

    private void setTranslationZ(float translationZ) {
        if (view.getTranslationZ() == translationZ || view.getElevation() == 0)
            return;
        ValueAnimator animator = ValueAnimator.ofFloat(view.getTranslationZ(), translationZ);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationZ((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
