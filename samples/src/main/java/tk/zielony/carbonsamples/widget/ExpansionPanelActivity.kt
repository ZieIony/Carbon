package tk.zielony.carbonsamples.widget

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_expansionpanel.*

import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.SamplesActivity
import tk.zielony.randomdata.common.TextGenerator

class ExpansionPanelActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expansionpanel)

        Samples.initToolbar(this, getString(R.string.expansionPanelActivity_title))

        expansionPanel_text.text = TextGenerator(5, false).next()
    }
}
