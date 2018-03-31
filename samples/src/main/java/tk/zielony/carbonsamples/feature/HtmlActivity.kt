package tk.zielony.carbonsamples.feature

import android.os.Bundle

import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.SamplesActivity

class HtmlActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)

        Samples.initToolbar(this, getString(R.string.htmlActivity_title))
    }
}
