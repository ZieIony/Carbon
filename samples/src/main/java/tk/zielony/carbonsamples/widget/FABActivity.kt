package tk.zielony.carbonsamples.widget

import android.os.Bundle
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.ThemedActivity

@ActivityAnnotation(layout = R.layout.activity_fab, title = R.string.fabActivity_title)
class FABActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this);
    }
}