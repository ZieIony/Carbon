package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.feature.AnchorActivity;
import tk.zielony.carbonsamples.feature.AutoSizeTextActivity;
import tk.zielony.carbonsamples.feature.BehaviorActivity;
import tk.zielony.carbonsamples.feature.ContextWrapperActivity;
import tk.zielony.carbonsamples.feature.FontResourceActivity;
import tk.zielony.carbonsamples.feature.HtmlActivity;
import tk.zielony.carbonsamples.feature.LargeShadowActivity;
import tk.zielony.carbonsamples.feature.PaginationActivity;
import tk.zielony.carbonsamples.feature.RobotoActivity;
import tk.zielony.carbonsamples.feature.RoundedCornersActivity;
import tk.zielony.carbonsamples.feature.ShadowActivity;
import tk.zielony.carbonsamples.feature.TextAppearanceActivity;
import tk.zielony.carbonsamples.feature.TextMarkerActivity;
import tk.zielony.carbonsamples.feature.ThemeActivity;
import tk.zielony.carbonsamples.feature.ZOrderActivity;
import tk.zielony.carbonsamples.widget.SVGActivity;


public class FeaturesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.featuresActivity_title));

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Carbon adds tons of useful features to allPreferences popular widgets. These include rounded corners, HTML text, elevation system, theme attributes, anchors and " +
                        "others",
                new SampleActivityItem(FontResourceActivity.class, getString(R.string.fontResourceActivity_title)),
                new SampleActivityItem(AutoSizeTextActivity.class, getString(R.string.autoSizeTextActivity_title)),
                new SampleActivityItem(BehaviorActivity.class, getString(R.string.behaviorActivity_title)),
                new SampleActivityItem(HtmlActivity.class, getString(R.string.htmlActivity_title)),
                new SampleActivityItem(RobotoActivity.class, getString(R.string.robotoActivity_title)),
                new SampleActivityItem(ShadowActivity.class, getString(R.string.shadowActivity_title), true),
                new SampleActivityItem(LargeShadowActivity.class, getString(R.string.largeShadowActivity_title)),
                new SampleActivityItem(PaginationActivity.class, getString(R.string.paginationActivity_title)),
                new SampleActivityItem(TextAppearanceActivity.class, getString(R.string.textappearanceActivity_title)),
                new SampleActivityItem(SVGActivity.class, getString(R.string.svgActivity_title)),
                new SampleActivityItem(ZOrderActivity.class, getString(R.string.zOrderActivity_title)),
                new SampleActivityItem(RoundedCornersActivity.class, getString(R.string.roundedCornersActivity_title)),
                new SampleActivityItem(AnchorActivity.class, getString(R.string.anchorsActivity_title)),
                new SampleActivityItem(ContextWrapperActivity.class, getString(R.string.contextWrapperActivity_title)),
                new SampleActivityItem(TextMarkerActivity.class, getString(R.string.textMarkerActivity_title)),
                new SampleActivityItem(ThemeActivity.class, getString(R.string.themeActivity_title)),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
