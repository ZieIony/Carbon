package tk.zielony.carbonsamples.guidelines

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_buttonsusage.*
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SamplesActivity

@SampleAnnotation(layoutId = R.layout.activity_buttonsusage, titleId = R.string.buttonsUsageActivity_title)
class ButtonsUsageActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.setIconVisible(true)
        progress.progress = 0.8f
    }
}
