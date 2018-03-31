package tk.zielony.carbonsamples.guidelines

import android.os.Bundle

import carbon.widget.DropDown
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.SamplesActivity

class MenusBehaviorActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menusbahavior)

        Samples.initToolbar(this, getString(R.string.menusBehaviorActivity_title))

        val dropDown = findViewById<DropDown<String>>(R.id.dropDown)
        dropDown.setItems(arrayOf("NY", "NC", "ND"))
    }
}
