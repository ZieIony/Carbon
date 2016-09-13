package tk.zielony.carbonsamples.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.nineoldandroids.animation.ValueAnimator;

import java.util.List;

import carbon.animation.AnimUtils;
import carbon.widget.LinearLayout;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-04-24.
 */
public class ShareToolbarActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_toolbar);

        final LinearLayout shareToolbar = (LinearLayout) findViewById(R.id.shareToolbar);
        final View root = shareToolbar.getRootView();
        findViewById(R.id.shareIcon).setOnClickListener(view -> {
            view.setVisibility(View.GONE);

            final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.addUpdateListener(animation -> {
                float frac = animator.getAnimatedFraction();
                float cornerRadius = AnimUtils.lerp(frac, shareToolbar.getHeight() / 2.0f, 0);
                shareToolbar.setCornerRadius((int) cornerRadius);
                float left = AnimUtils.lerp(frac, root.getWidth() - shareToolbar.getHeight() - getResources().getDimension(R.dimen.carbon_padding), 0);
                float right = AnimUtils.lerp(frac, root.getWidth() - getResources().getDimension(R.dimen.carbon_padding), root.getWidth());
                shareToolbar.layout((int) left, shareToolbar.getTop(), (int) right, shareToolbar.getBottom());
                shareToolbar.setElevation(frac);
                shareToolbar.setBackgroundColor(AnimUtils.lerpColor(frac, 0xff9ACE00, 0xffffffff));
                if (shareToolbar.getParent() != null)
                    ((View) shareToolbar.getParent()).postInvalidate();
            });
            animator.setStartDelay(200);
            animator.setDuration(500);
            view.setEnabled(false);
            animator.start();
            final List<View> viewsWithTag = shareToolbar.findViewsWithTag("animate");
            view.getHandler().postDelayed(() -> {
                for (int i = 0; i < viewsWithTag.size(); i++) {
                    final int finalI = i;
                    view.getHandler().postDelayed(() -> viewsWithTag.get(finalI).setVisibility(View.VISIBLE), i * 40);
                }
            }, 500);
        });
    }
}
