package tk.zielony.carbonsamples.component

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_componentview.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.carbonsamples.demo.SongItem
import tk.zielony.carbonsamples.demo.SongRow

@SampleAnnotation(layoutId = R.layout.activity_componentview, titleId = R.string.componentViewActivity_title)
class ComponentViewActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()

        songComponent.getComponent<SongRow>().data = SongItem(3, "Classes in the Jar", 245)
    }
}