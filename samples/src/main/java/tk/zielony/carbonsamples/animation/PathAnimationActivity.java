package tk.zielony.carbonsamples.animation;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.MotionEvent;
import android.view.View;

import carbon.internal.NURBS;
import carbon.widget.ImageView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.common.DrawableImageGenerator;

public class PathAnimationActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathanimation);

        Samples.initToolbar(this, getString(R.string.pathAnimationActivity_title));

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageDrawable(new DrawableImageGenerator(this).next());

        View layout = findViewById(R.id.layout);
        layout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                NURBS nurbs = new NURBS();
                nurbs.addPoint(new PointF(imageView.getX() + imageView.getWidth() / 2, imageView.getY() + imageView.getHeight() / 2));
                nurbs.addPoint(new PointF(imageView.getX() + imageView.getWidth() / 2, event.getY()));
                nurbs.addPoint(new PointF(event.getX(), event.getY()));
                nurbs.init();

                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(500);
                animator.setInterpolator(new FastOutSlowInInterpolator());
                animator.addUpdateListener(animation -> {
                    PointF point = nurbs.getPoint(animation.getAnimatedFraction());
                    imageView.setX(point.x - imageView.getWidth() / 2);
                    imageView.setY(point.y - imageView.getHeight() / 2);
                });
                animator.start();
            }
            return true;
        });
    }
}
