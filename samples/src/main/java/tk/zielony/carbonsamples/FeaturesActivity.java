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

@ActivityAnnotation(title = R.string.featuresActivity_title)
public class FeaturesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Carbon adds tons of useful features to allPreferences popular widgets. These include rounded corners, HTML text, elevation system, theme attributes, anchors and " +
                        "others",
                new SampleActivityItem(FontResourceActivity.class),
                new SampleActivityItem(AutoSizeTextActivity.class),
                new SampleActivityItem(BehaviorActivity.class),
                new SampleActivityItem(HtmlActivity.class),
                new SampleActivityItem(RobotoActivity.class),
                new SampleActivityItem(ShadowActivity.class, 0, true),
                new SampleActivityItem(LargeShadowActivity.class),
                new SampleActivityItem(PaginationActivity.class),
                new SampleActivityItem(TextAppearanceActivity.class),
                new SampleActivityItem(SVGActivity.class),
                new SampleActivityItem(ZOrderActivity.class),
                new SampleActivityItem(RoundedCornersActivity.class),
                new SampleActivityItem(AnchorActivity.class),
                new SampleActivityItem(ContextWrapperActivity.class),
                new SampleActivityItem(TextMarkerActivity.class),
                new SampleActivityItem(ThemeActivity.class),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
