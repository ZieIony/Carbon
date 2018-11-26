package tk.zielony.carbonsamples.widget

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_banner.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.SamplesActivity

@ActivityAnnotation(layout = R.layout.activity_banner)
class BannerActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this, resources.getString(R.string.bannerActivity_title))

        button1.setOnClickListener { banner.dismiss() }
    }
}
