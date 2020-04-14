package tk.zielony.carbonsamples

import android.os.Bundle
import tk.zielony.carbonsamples.animation.ImageFadeActivity
import tk.zielony.carbonsamples.animation.PathAnimationActivity
import tk.zielony.carbonsamples.animation.RippleActivity
import tk.zielony.carbonsamples.animation.WidgetAnimationsActivity

@SampleAnnotation(titleId = R.string.animationsActivity_title, layoutId = R.layout.activity_samplelist)
class AnimationsActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                "Carbon adds easy visibility animations, brightness/saturation fade for images, backports the touch ripple and the circular reveal animation",
                SampleActivityItem(WidgetAnimationsActivity::class.java),
                SampleActivityItem(ImageFadeActivity::class.java),
                SampleActivityItem(RippleActivity::class.java),
                SampleActivityItem(PathAnimationActivity::class.java)
        ))
    }

}
