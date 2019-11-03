package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.feature.AnchorActivity;
import tk.zielony.carbonsamples.feature.AutoSizeTextActivity;
import tk.zielony.carbonsamples.feature.BehaviorActivity;
import tk.zielony.carbonsamples.feature.GestureDetectorActivity;
import tk.zielony.carbonsamples.feature.HtmlActivity;
import tk.zielony.carbonsamples.feature.PaginationActivity;
import tk.zielony.carbonsamples.feature.RoundedCornersActivity;
import tk.zielony.carbonsamples.feature.ShadowActivity;
import tk.zielony.carbonsamples.feature.TextAppearanceActivity;
import tk.zielony.carbonsamples.feature.TextMarkerActivity;
import tk.zielony.carbonsamples.feature.XmlFontActivity;
import tk.zielony.carbonsamples.feature.ZOrderActivity;
import tk.zielony.carbonsamples.widget.SVGActivity;

@ActivityAnnotation(title = R.string.featuresActivity_title, layout = R.layout.activity_samplelist)
public class FeaturesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Carbon adds tons of useful features to all popular widgets. These include rounded corners, HTML text, elevation system, anchors and " +
                        "others",
                new SampleActivityItem(GestureDetectorActivity.class, R.drawable.ic_gesture_black_24dp),
                new SampleActivityItem(AutoSizeTextActivity.class),
                new SampleActivityItem(BehaviorActivity.class),
                new SampleActivityItem(HtmlActivity.class),
                new SampleActivityItem(XmlFontActivity.class, R.drawable.ic_font_download_black_24dp),
                new SampleActivityItem(ShadowActivity.class),
                new SampleActivityItem(PaginationActivity.class),
                new SampleActivityItem(TextAppearanceActivity.class),
                new SampleActivityItem(SVGActivity.class),
                new SampleActivityItem(ZOrderActivity.class),
                new SampleActivityItem(RoundedCornersActivity.class),
                new SampleActivityItem(AnchorActivity.class),
                new SampleActivityItem(TextMarkerActivity.class),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
