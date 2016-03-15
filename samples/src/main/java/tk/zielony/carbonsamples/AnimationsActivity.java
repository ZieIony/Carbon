package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import carbon.Carbon;
import carbon.internal.DebugOverlay;
import carbon.widget.ImageView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.animation.ImageFadeActivity;
import tk.zielony.carbonsamples.animation.ListRippleActivity;
import tk.zielony.carbonsamples.animation.RadialTransitionActivity;
import tk.zielony.carbonsamples.animation.RippleActivity;
import tk.zielony.carbonsamples.animation.RippleComparisonActivity;
import tk.zielony.carbonsamples.animation.WidgetAnimationsActivity;


public class AnimationsActivity extends Activity {
    private boolean debugEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final DebugOverlay overlay = new DebugOverlay(this);

        final ImageView debug = (ImageView) findViewById(R.id.debug);
        debug.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!debugEnabled) {
                    debug.setTint(Carbon.getThemeColor(AnimationsActivity.this, R.attr.carbon_iconColor));
                    overlay.show();
                    debugEnabled = true;
                } else {
                    debug.setTint(Carbon.getThemeColor(AnimationsActivity.this, R.attr.colorControlNormal));
                    overlay.dismiss();
                    debugEnabled = false;
                }
            }
        });

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
        toolbar.setText(getString(R.string.animationsActivity_title));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(WidgetAnimationsActivity.class, getString(R.string.widgetAnimationsActivity_title)),
                new ViewModel(ImageFadeActivity.class, getString(R.string.imageFadeActivity_title)),
                new ViewModel(ListRippleActivity.class, getString(R.string.listRippleActivity_title)),
                new ViewModel(RippleComparisonActivity.class, getString(R.string.rippleComparisonActivity_title), false, true),
                new ViewModel(RadialTransitionActivity.class, getString(R.string.radialTransitionActivity_title)),
                new ViewModel(RippleActivity.class, getString(R.string.rippleActivity_title))
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
