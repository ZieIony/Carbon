package tk.zielony.carbonsamples.feature

import android.os.Bundle
import tk.zielony.carbonsamples.SampleAnnotation

import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity

@SampleAnnotation(layoutId = R.layout.activity_html, titleId = R.string.htmlActivity_title)
class HtmlActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
    }
}
