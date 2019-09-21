package tk.zielony.carbonsamples.widget

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_expansionpanel.*
import tk.zielony.carbonsamples.ActivityAnnotation

import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.SamplesActivity
import tk.zielony.randomdata.common.TextGenerator

@ActivityAnnotation(layout = R.layout.activity_expansionpanel, title = R.string.expansionPanelActivity_title)
class ExpansionPanelActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this)

        expansionPanel_text.text = TextGenerator(5, false).next()
    }
}
