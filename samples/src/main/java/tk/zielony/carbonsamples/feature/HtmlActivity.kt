package tk.zielony.carbonsamples.feature

import android.os.Bundle
import tk.zielony.carbonsamples.ActivityAnnotation

import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.SamplesActivity

@ActivityAnnotation(layout = R.layout.activity_html, title = R.string.htmlActivity_title)
class HtmlActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this)
    }
}
