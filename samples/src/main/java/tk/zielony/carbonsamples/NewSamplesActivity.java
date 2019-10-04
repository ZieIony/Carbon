package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.widget.BackdropActivity;
import tk.zielony.carbonsamples.widget.BannerActivity;
import tk.zielony.carbonsamples.widget.BottomNavigationViewActivity;
import tk.zielony.carbonsamples.widget.ButtonsActivity;
import tk.zielony.carbonsamples.widget.CheckBoxRadioActivity;
import tk.zielony.carbonsamples.widget.FloatingActionMenuActivity;
import tk.zielony.carbonsamples.widget.FlowLayoutActivity;
import tk.zielony.carbonsamples.widget.NavigationViewActivity;

@ActivityAnnotation(title = R.string.newSamplesActivity_title, layout = R.layout.activity_samplelist)
public class NewSamplesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "These samples are new or have significant changes since the previous release",
                new SampleActivityItem(BannerActivity.class),
                new SampleActivityItem(ButtonsActivity.class),
                new SampleActivityItem(CheckBoxRadioActivity.class, R.drawable.carbon_checkbox_checked),
                new SampleActivityItem(FlowLayoutActivity.class),
                new SampleActivityItem(BackdropActivity.class),
                new SampleActivityItem(BottomNavigationViewActivity.class),
                new SampleActivityItem(NavigationViewActivity.class, 0, true),
                new SampleActivityItem(FloatingActionMenuActivity.class),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}

