package tk.zielony.carbonsamples.library

import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_design.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SamplesActivity

class DesignActivity : SamplesActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design)

        collapsing_toolbar.title = "Design Support Library"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_navigation, menu)
        return true
    }
}
