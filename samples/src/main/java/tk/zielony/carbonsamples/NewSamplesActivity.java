package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.feature.FontResourceActivity;
import tk.zielony.carbonsamples.feature.ShadowActivity;
import tk.zielony.carbonsamples.guidelines.BasilActivity;
import tk.zielony.carbonsamples.guidelines.CraneActivity;
import tk.zielony.carbonsamples.guidelines.ShrineActivity;
import tk.zielony.carbonsamples.widget.BackdropActivity;
import tk.zielony.carbonsamples.widget.BannerActivity;
import tk.zielony.carbonsamples.widget.ButtonsActivity;

public class NewSamplesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.newSamplesActivity_title));

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "These samples are new or have significant changes since the previous release",
                new SampleActivityItem(BannerActivity.class, getString(R.string.bannerActivity_title)),
                new SampleActivityItem(ButtonsActivity.class, getString(R.string.buttonsActivity_title)),
                new SampleActivityItem(BackdropActivity.class, getString(R.string.backdropActivity_title)),
                new SampleActivityItem(ShadowActivity.class, getString(R.string.shadowActivity_title), true),
                new SampleActivityItem(FontResourceActivity.class, getString(R.string.fontResourceActivity_title)),
                new SampleActivityItem(ShrineActivity.class, getString(R.string.shrineActivity_title), true),
                new SampleActivityItem(CraneActivity.class, getString(R.string.craneActivity_title)),
                new SampleActivityItem(BasilActivity.class, getString(R.string.basilActivity_title), true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}

