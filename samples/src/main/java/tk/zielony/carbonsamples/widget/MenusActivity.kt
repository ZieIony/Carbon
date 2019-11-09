package tk.zielony.carbonsamples.widget

import android.os.Bundle
import carbon.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_menus.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity


@ActivityAnnotation(layout = R.layout.activity_menus, title = R.string.menusActivity_title)
class MenusActivity: ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        button.setOnClickListener {
            val popupMenu = PopupMenu(this)
            popupMenu.setMenu(R.menu.menu_news)
            popupMenu.show(button)
        }
    }
}