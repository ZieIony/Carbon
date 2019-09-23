package tk.zielony.carbonsamples.library

import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_design.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity

@ActivityAnnotation(layout = R.layout.activity_design, title = R.string.designActivity_title)
class DesignActivity : ThemedActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        collapsing_toolbar.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_navigation, menu)
        return true
    }
}
