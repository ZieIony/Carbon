package tk.zielony.carbonsamples.widget

import android.os.Bundle
import carbon.drawable.ColorStateListFactory
import carbon.widget.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bottomnavigationview.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity

class BadgeItem : BottomNavigationView.Item() {
    var notificationCount: Int = 0
}

@SampleAnnotation(layoutId = R.layout.activity_bottomnavigationview, titleId = R.string.bottomNavigationViewActivity_title)
class BottomNavigationViewActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        bottomBar.menuItems = arrayOf(
                BadgeItem().apply {
                    icon = resources.getDrawable(R.drawable.ic_android_black_24dp)
                    title = "Android"
                    iconTintList = ColorStateListFactory.makeIconSecondary(this@BottomNavigationViewActivity)
                },
                BadgeItem().apply {
                    icon = resources.getDrawable(R.drawable.ic_today_black_24dp)
                    title = "Calendar"
                    iconTintList = ColorStateListFactory.makeIconSecondary(this@BottomNavigationViewActivity)
                    notificationCount = 346
                },
                BadgeItem().apply {
                    icon = resources.getDrawable(R.drawable.ic_comment_black_24dp)
                    title = "Comment"
                    iconTintList = ColorStateListFactory.makeIconSecondary(this@BottomNavigationViewActivity)
                    notificationCount = 1
                })

        bottomBar3.menuItems = arrayOf(
                BottomNavigationView.Item().apply {
                    icon = resources.getDrawable(R.drawable.ic_android_black_24dp)
                    title = "Android"
                    iconTintList = ColorStateListFactory.makeIconSecondary(this@BottomNavigationViewActivity)
                },
                BottomNavigationView.Item().apply {
                    icon = resources.getDrawable(R.drawable.ic_today_black_24dp)
                    title = "Calendar"
                    iconTintList = ColorStateListFactory.makeIconSecondary(this@BottomNavigationViewActivity)
                },
                BottomNavigationView.Item().apply {
                    icon = resources.getDrawable(R.drawable.ic_comment_black_24dp)
                    title = "Comment"
                    iconTintList = ColorStateListFactory.makeIconSecondary(this@BottomNavigationViewActivity)
                })
    }
}
