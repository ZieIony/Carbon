package tk.zielony.carbonsamples.widget

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bottombar.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SamplesActivity

@ActivityAnnotation(layout = R.layout.activity_bottombar, title = R.string.bottomBarActivity_title)
class BottomBarActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomBar.setMenu(R.menu.menu_navigation)
    }
}
