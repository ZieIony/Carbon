package tk.zielony.carbonsamples.widget

import android.os.Bundle
import carbon.recycler.RowFactory
import carbon.widget.MenuStrip
import carbon.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_menus.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity


@SampleAnnotation(
        layoutId = R.layout.activity_menus,
        titleId = R.string.menusActivity_title,
        iconId = R.drawable.ic_menu_black_24dp
)
class MenusActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        button.setOnClickListener {
            val popupMenu = PopupMenu(this)
            popupMenu.setMenu(R.menu.menu_news)
            popupMenu.show(button)
        }

        tools.itemFactory = RowFactory { MenuStrip.ToolItemComponent(it) }
    }
}