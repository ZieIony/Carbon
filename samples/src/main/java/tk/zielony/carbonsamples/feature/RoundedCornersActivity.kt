package tk.zielony.carbonsamples.feature

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_rounded_corners.*
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.common.DrawableImageGenerator

@SampleAnnotation(layoutId = R.layout.activity_rounded_corners, titleId = R.string.roundedCornersActivity_title, iconId = R.drawable.ic_rounded_corner_black_24dp)
class RoundedCornersActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        image.setImageDrawable(DrawableImageGenerator(this).next())
    }
}
