package tk.zielony.carbonsamples;

import android.os.Bundle;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.animation.ImageFadeActivity;
import tk.zielony.carbonsamples.animation.RippleActivity;
import tk.zielony.carbonsamples.animation.WidgetAnimationsActivity;


public class AnimationsActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.animationsActivity_title));

        setItems(new Object[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Carbon adds easy visibility animations, brightness/saturation fade for images, backports the touch ripple and the circular reveal animation",
                new SampleActivityItem(WidgetAnimationsActivity.class, getString(R.string.widgetAnimationsActivity_title)),
                new SampleActivityItem(ImageFadeActivity.class, getString(R.string.imageFadeActivity_title)),
                new SampleActivityItem(RippleActivity.class, getString(R.string.rippleActivity_title)),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
