package tk.zielony.carbonsamples.widget

import android.os.Bundle
import carbon.widget.BottomBar
import kotlinx.android.synthetic.main.activity_bottombar.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.Samples
import tk.zielony.carbonsamples.ThemedActivity

@ActivityAnnotation(layout = R.layout.activity_bottombar, title = R.string.bottomBarActivity_title)
class BottomBarActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Samples.initToolbar(this)

        bottomBar3.setItems(arrayOf(
            BottomBar.Item(resources.getDrawable(R.drawable.ic_android_black_24dp), "Android"),
            BottomBar.Item(resources.getDrawable(R.drawable.ic_calendar_black_24dp), "Calendar"),
            BottomBar.Item(resources.getDrawable(R.drawable.ic_comment_black_24dp), "Comment")
        ))
    }
}
