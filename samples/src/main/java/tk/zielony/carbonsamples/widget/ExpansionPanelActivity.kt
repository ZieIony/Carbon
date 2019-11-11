package tk.zielony.carbonsamples.widget

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_expansionpanel.*
import tk.zielony.carbonsamples.SampleAnnotation

import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.common.TextGenerator

@SampleAnnotation(layoutId = R.layout.activity_expansionpanel, titleId = R.string.expansionPanelActivity_title)
class ExpansionPanelActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        expansionPanel_text.text = TextGenerator(5, false).next()
    }
}
