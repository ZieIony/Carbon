package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.nineoldandroids.animation.ValueAnimator;

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

        animator = ValueAnimator.ofFloat(0.1f, 1);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(Animation.REVERSE);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = (int) ((float) valueAnimator.getAnimatedValue() * (getWindow().getDecorView().getWidth() - 100));
                layoutParams.height = (int) ((float) valueAnimator.getAnimatedValue() * (getWindow().getDecorView().getHeight() - 100));
                view.setLayoutParams(layoutParams);
                ((View) view.getParent()).invalidate();
            }
        });
        animator.start();
    }
}
