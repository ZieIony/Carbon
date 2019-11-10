package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.animation.ImageFadeActivity
import tk.zielony.carbonsamples.animation.PathAnimationActivity
import tk.zielony.carbonsamples.animation.RippleActivity
import tk.zielony.carbonsamples.animation.WidgetAnimationsActivity

@ActivityAnnotation(title = R.string.animationsActivity_title, layout = R.layout.activity_samplelist)
class AnimationsActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Carbon adds easy visibility animations, brightness/saturation fade for images, backports the touch ripple and the circular reveal animation",
                SampleActivityItem(WidgetAnimationsActivity::class.java),
                SampleActivityItem(ImageFadeActivity::class.java),
                SampleActivityItem(RippleActivity::class.java),
                SampleActivityItem(PathAnimationActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
