package tk.zielony.carbonsamples.feature

import android.app.Activity
import android.content.Context
import android.os.Bundle

import carbon.CarbonContextWrapper
import tk.zielony.carbonsamples.R

class ContextWrapperActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contextwrapper)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CarbonContextWrapper.wrap(newBase))
    }
}
