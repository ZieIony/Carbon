package tk.zielony.carbonsamples.feature

import android.content.Context
import carbon.CarbonContextWrapper
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SamplesActivity

@ActivityAnnotation(layout = R.layout.activity_contextwrapper, title = R.string.contextWrapperActivity_title)
class ContextWrapperActivity : SamplesActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CarbonContextWrapper.wrap(newBase))
    }
}
