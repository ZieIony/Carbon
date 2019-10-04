package tk.zielony.carbonsamples.demo;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.List;

import carbon.Carbon;
import carbon.animation.AnimUtils;
import carbon.internal.MathUtils;
import carbon.widget.LinearLayout;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.ThemedActivity;

@ActivityAnnotation(layout = R.layout.activity_share_toolbar, title = R.string.shareToolbarActivity_title)
public class ShareToolbarActivity extends ThemedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        final LinearLayout shareToolbar = findViewById(R.id.shareToolbar);
        final View root = shareToolbar.getRootView();
        findViewById(R.id.shareIcon).setOnClickListener(view -> {
            view.setVisibility(View.GONE);

            final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.addUpdateListener(animation -> {
                float frac = animator.getAnimatedFraction();
                float cornerRadius = MathUtils.lerp(shareToolbar.getHeight() / 2.0f, 0, frac);
                shareToolbar.setCornerRadius((int) cornerRadius);
                float left = MathUtils.lerp(root.getWidth() - shareToolbar.getHeight() - getResources().getDimension(R.dimen.carbon_padding), 0, frac);
                float right = MathUtils.lerp(root.getWidth() - getResources().getDimension(R.dimen.carbon_padding), root.getWidth(), frac);
                shareToolbar.layout((int) left, shareToolbar.getTop(), (int) right, shareToolbar.getBottom());
                shareToolbar.setElevation(frac);
                int inColor = Carbon.getThemeColor(this, R.attr.colorAccent);
                int outColor = Carbon.getThemeColor(this, R.attr.carbon_colorForeground);
                shareToolbar.setBackgroundColor(AnimUtils.lerpColor(frac, inColor, outColor));
            });
            animator.setDuration(300);
            view.setOnClickListener(null);
            animator.start();
            final List<View> viewsWithTag = shareToolbar.findViewsWithTag("animate");
            view.getHandler().postDelayed(() -> {
                for (int i = 0; i < viewsWithTag.size(); i++) {
                    final int finalI = i;
                    view.getHandler().postDelayed(() -> viewsWithTag.get(finalI).setVisibility(View.VISIBLE), i * 40);
                }
            }, 200);
        });
    }
}
