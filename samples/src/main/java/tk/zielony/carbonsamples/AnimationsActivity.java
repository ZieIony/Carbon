package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.animation.ImageFadeActivity;
import tk.zielony.carbonsamples.animation.PathAnimationActivity;
import tk.zielony.carbonsamples.animation.RippleActivity;
import tk.zielony.carbonsamples.animation.WidgetAnimationsActivity;

@ActivityAnnotation(title = R.string.animationsActivity_title)
public class AnimationsActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Carbon adds easy visibility animations, brightness/saturation fade for images, backports the touch ripple and the circular reveal animation",
                new SampleActivityItem(WidgetAnimationsActivity.class),
                new SampleActivityItem(ImageFadeActivity.class),
                new SampleActivityItem(RippleActivity.class),
                new SampleActivityItem(PathAnimationActivity.class),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
