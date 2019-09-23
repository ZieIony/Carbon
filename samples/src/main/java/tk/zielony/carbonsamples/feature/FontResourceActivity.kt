package tk.zielony.carbonsamples.feature

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_font_resource.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.common.TextGenerator

@ActivityAnnotation(layout = R.layout.activity_font_resource, title = R.string.fontResourceActivity_title)
class FontResourceActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val generator = TextGenerator(3, true)
        textView1.text = generator.next()
        textView2.text = generator.next()
        textView3.text = generator.next()
        textView4.text = generator.next()
    }
}
