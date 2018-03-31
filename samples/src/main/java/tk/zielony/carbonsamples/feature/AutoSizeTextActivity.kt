package tk.zielony.carbonsamples.feature

import android.os.Bundle
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_autosizetext.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.SamplesActivity

class AutoSizeTextActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autosizetext)

        Samples.initToolbar(this, getString(R.string.autoSizeTextActivity_title))

        autoSizeText.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            textSize.setTextSize(TypedValue.COMPLEX_UNIT_PX, autoSizeText.textSize)
            textSize.text = "${autoSizeText.textSize / resources.displayMetrics.scaledDensity}sp"
        }
    }
}
