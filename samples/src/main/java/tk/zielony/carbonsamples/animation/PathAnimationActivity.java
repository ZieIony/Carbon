package tk.zielony.carbonsamples.animation;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import carbon.internal.MathUtils;
import carbon.internal.NURBS;
import carbon.widget.ImageView;
import carbon.widget.LinearLayout;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.randomdata.common.DrawableImageGenerator;

@ActivityAnnotation(layout = R.layout.activity_pathanimation, title = R.string.pathAnimationActivity_title)
public class PathAnimationActivity extends ThemedActivity {

    boolean expanded = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        ImageView imageView = findViewById(R.id.image);
        imageView.setImageDrawable(new DrawableImageGenerator(this).next());

        LinearLayout card = findViewById(R.id.card);

        View layout = findViewById(R.id.layout);
        layout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                NURBS nurbs = new NURBS();
                nurbs.addPoint(new PointF(card.getX() + card.getWidth() / 2, card.getY() + card.getHeight() / 2));
                nurbs.addPoint(new PointF(event.getX(), card.getY() + card.getHeight() / 2));
                nurbs.addPoint(new PointF(event.getX(), event.getY()));
                nurbs.init();

                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                float srcWidth = card.getWidth();
                float srcHeight = card.getHeight();
                float destWidth = expanded ? getResources().getDimension(R.dimen.carbon_contentSpace) : layout.getWidth();
                float destHeight = destWidth * 9.0f / 16;
                animator.setDuration(500);
                animator.setInterpolator(new FastOutSlowInInterpolator());
                animator.addUpdateListener(animation -> {
                    PointF point = nurbs.getPoint((Float) animation.getAnimatedValue());
                    int w = (int) MathUtils.lerp(srcWidth, destWidth, (Float) animation.getAnimatedValue());
                    int h = (int) MathUtils.lerp(srcHeight, destHeight, (Float) animation.getAnimatedValue());
                    int x = (int) point.x - w / 2;
                    int y = (int) point.y - h / 2;

                    card.setBounds(x, y, w, h);
                });
                animator.start();
                expanded = !expanded;
            }
            return true;
        });
    }
}