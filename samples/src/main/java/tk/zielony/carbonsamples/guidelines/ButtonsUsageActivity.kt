package tk.zielony.carbonsamples.guidelines

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_buttonsusage.*
import tk.zielony.carbonsamples.R

class ButtonsUsageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buttonsusage)

        toolbar.setIconVisible(true)
        progress.progress = 0.8f
    }
}
