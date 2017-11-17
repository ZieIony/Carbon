package tk.zielony.carbonsamples.widget

import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_toolbar.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SamplesActivity

@ActivityAnnotation(layout = R.layout.activity_toolbar)
class ToolbarActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.setIconVisible(true)

        setSupportActionBar(toolbar)
        title = "Toolbar"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_navigation, menu)
        return true
    }
}
