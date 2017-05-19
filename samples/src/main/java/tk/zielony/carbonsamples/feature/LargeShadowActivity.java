package tk.zielony.carbonsamples.feature;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import carbon.widget.ImageView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.common.DrawableImageGenerator;

public class LargeShadowActivity extends SamplesActivity {
    ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_largeshadow);

        Samples.initToolbar(this, getString(R.string.largeShadowActivity_title));

        final View view = findViewById(R.id.layout);
        View parent = (View) view.getParent();
        int margin = getResources().getDimensionPixelSize(R.dimen.carbon_margin);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageDrawable(new DrawableImageGenerator(this).next());

        animator = ValueAnimator.ofFloat(0.2f, 1);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(2000);
        animator.addUpdateListener(valueAnimator -> {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = (int) ((float) valueAnimator.getAnimatedValue() * (parent.getWidth() - margin * 2));
            layoutParams.height = (int) ((float) valueAnimator.getAnimatedValue() * (parent.getHeight() - margin * 2));
            view.setLayoutParams(layoutParams);
        });
        animator.start();
    }
}
