package tk.zielony.carbonsamples.guidelines

import android.os.Bundle
import carbon.widget.DropDown
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity

@SampleAnnotation(layoutId = R.layout.activity_menusbahavior, titleId = R.string.menusBehaviorActivity_title)
class MenusBehaviorActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        val dropDown = findViewById<DropDown>(R.id.dropDown)
        dropDown.setItems(arrayOf("NY", "NC", "ND"))
    }
}
