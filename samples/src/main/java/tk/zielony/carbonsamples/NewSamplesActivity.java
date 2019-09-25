package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.feature.ShadowActivity;
import tk.zielony.carbonsamples.widget.BackdropActivity;
import tk.zielony.carbonsamples.widget.BannerActivity;
import tk.zielony.carbonsamples.widget.BottomBarActivity;
import tk.zielony.carbonsamples.widget.CheckBoxRadioActivity;
import tk.zielony.carbonsamples.widget.FlowLayoutActivity;

@ActivityAnnotation(title = R.string.newSamplesActivity_title)
public class NewSamplesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "These samples are new or have significant changes since the previous release",
                new SampleActivityItem(BannerActivity.class),
                new SampleActivityItem(CheckBoxRadioActivity.class),
                new SampleActivityItem(FlowLayoutActivity.class),
                new SampleActivityItem(BackdropActivity.class),
                new SampleActivityItem(ShadowActivity.class, 0, true),
                new SampleActivityItem(BottomBarActivity.class, 0, true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}

