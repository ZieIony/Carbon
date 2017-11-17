package tk.zielony.carbonsamples.widget

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_navigationview.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SamplesActivity

@ActivityAnnotation(layout = R.layout.activity_navigationview, title = R.string.navigationViewActivity_title)
class NavigationViewActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        drawerMenu.setMenu(R.menu.menu_navigation)
    }

}