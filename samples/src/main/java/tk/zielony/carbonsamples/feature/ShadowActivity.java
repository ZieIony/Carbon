package tk.zielony.carbonsamples.feature;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ShadowActivity extends Activity {
    ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);

        final View view = findViewById(R.id.button);

        animator = ValueAnimator.ofFloat(0, getWindowManager().getDefaultDisplay().getWidth());
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(Animation.REVERSE);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewHelper.setTranslationX(view, (Float) animator.getAnimatedValue() - view.getWidth() / 2);
                ((View) view.getParent()).postInvalidate();
            }
        });
        animator.start();
    }
}
