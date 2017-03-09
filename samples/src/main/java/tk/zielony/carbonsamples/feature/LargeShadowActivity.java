package tk.zielony.carbonsamples.feature;

import tk.zielony.carbonsamples.SamplesActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.nineoldandroids.animation.ValueAnimator;

import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class LargeShadowActivity extends SamplesActivity {
    ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_largeshadow);

        final View view = findViewById(R.id.button);

        animator = ValueAnimator.ofFloat(0.2f, 1);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(Animation.REVERSE);
        animator.setDuration(2000);
        animator.addUpdateListener(valueAnimator -> {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = (int) ((float) valueAnimator.getAnimatedValue() * (getWindow().getDecorView().getWidth() - 100));
            layoutParams.height = (int) ((float) valueAnimator.getAnimatedValue() * (getWindow().getDecorView().getHeight() - 100));
            view.setLayoutParams(layoutParams);
            ((View) view.getParent()).postInvalidate();
        });
        animator.start();
    }
}
