package tk.zielony.carbonsamples.component

import android.os.Bundle
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity

@SampleAnnotation(layoutId = R.layout.activity_componentview, titleId = R.string.componentViewActivity_title)
class ComponentViewActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
    }
}