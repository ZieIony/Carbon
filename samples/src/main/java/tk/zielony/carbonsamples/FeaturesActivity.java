package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import carbon.Carbon;
import carbon.internal.DebugOverlay;
import carbon.widget.ImageView;
import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.feature.AnchorActivity;
import tk.zielony.carbonsamples.feature.ContextWrapperActivity;
import tk.zielony.carbonsamples.feature.LargeShadowActivity;
import tk.zielony.carbonsamples.feature.PercentLayoutActivity;
import tk.zielony.carbonsamples.feature.RobotoActivity;
import tk.zielony.carbonsamples.feature.RoundedCornersActivity;
import tk.zielony.carbonsamples.feature.ShadowActivity;
import tk.zielony.carbonsamples.feature.TextAppearanceActivity;
import tk.zielony.carbonsamples.feature.TextMarkerActivity;
import tk.zielony.carbonsamples.feature.ZOrderActivity;


public class FeaturesActivity extends Activity {

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
                    debug.setTint(Carbon.getThemeColor(FeaturesActivity.this, R.attr.carbon_iconColor));
                    overlay.show();
                    debugEnabled = true;
                } else {
                    debug.setTint(Carbon.getThemeColor(FeaturesActivity.this, R.attr.colorControlNormal));
                    overlay.dismiss();
                    debugEnabled = false;
                }
            }
        });

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
        toolbar.setText(getString(R.string.featuresActivity_title));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(RobotoActivity.class, getString(R.string.robotoActivity_title)),
                new ViewModel(ShadowActivity.class, getString(R.string.shadowActivity_title)),
                new ViewModel(LargeShadowActivity.class, getString(R.string.largeShadowActivity_title)),
                new ViewModel(TextAppearanceActivity.class, getString(R.string.textappearanceActivity_title)),
                new ViewModel(ZOrderActivity.class, getString(R.string.zOrderActivity_title)),
                new ViewModel(RoundedCornersActivity.class, getString(R.string.roundedCornersActivity_title)),
                new ViewModel(AnchorActivity.class, getString(R.string.anchorsActivity_title)),
                new ViewModel(ContextWrapperActivity.class, getString(R.string.contextWrapperActivity_title)),
                new ViewModel(PercentLayoutActivity.class, getString(R.string.percentLayoutActivity_title)),
                new ViewModel(TextMarkerActivity.class, getString(R.string.textMarkerActivity_title))
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
