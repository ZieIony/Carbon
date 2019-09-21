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
import tk.zielony.carbonsamples.widget.CheckBoxRadioActivity;

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
                new SampleActivityItem(ButtonsActivity.class),
                new SampleActivityItem(BackdropActivity.class),
                new SampleActivityItem(ShadowActivity.class, true),
                new SampleActivityItem(FontResourceActivity.class),
                new SampleActivityItem(ShrineActivity.class, true),
                new SampleActivityItem(CraneActivity.class),
                new SampleActivityItem(BasilActivity.class, true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}

