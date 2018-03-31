package tk.zielony.carbonsamples.feature

import android.os.Bundle

import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.SamplesActivity

class PercentLayoutActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_percentlayout)

        Samples.initToolbar(this, getString(R.string.percentLayoutActivity_title))
    }
}
