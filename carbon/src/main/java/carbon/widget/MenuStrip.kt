package carbon.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.*
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.Carbon
import carbon.R
import carbon.component.Component
import carbon.component.DataBindingComponent
import carbon.component.DividerItem
import carbon.recycler.RowFactory
import carbon.recycler.RowListAdapter
import java.io.Serializable
import java.util.*

open class MenuStrip : RecyclerView {

    open class Item : Serializable {
        var id: Int = 0
        var icon: Drawable? = null
        var title: CharSequence? = null
        var iconTintList: ColorStateList? = null
        var groupId: Int = 0

        constructor()

        constructor(id: Int, icon: Drawable, text: CharSequence) {
            this.id = id
            this.icon = icon
            this.title = text
        }

        constructor(menuItem: MenuItem) {
            id = menuItem.itemId
            try {   // breaks preview
                this.icon = menuItem.icon
            } catch (e: Exception) {
            }
            this.title = menuItem.title
            iconTintList = MenuItemCompat.getIconTintList(menuItem)
            groupId = menuItem.groupId
        }
    }

    private class SeparatorComponent : Component<DividerItem> {
        private var view: View

        constructor(parent: ViewGroup, layoutId: Int) {
            this.view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        }

        override fun getView(): View = view
    }


    private var items: Array<Item>? = null
    private var itemLayoutId: Int = 0
    private var separatorLayoutId: Int = 0

    private lateinit var _orientation: carbon.view.Orientation
    var orientation: carbon.view.Orientation
        get() = _orientation
        set(orientation) {
            _orientation = orientation
            initItems()
        }

    lateinit var adapter: RowListAdapter<Serializable>

    private var onItemClickedListener: OnItemClickedListener<Item>? = null

    var menuItems: Array<Item>?
        get() = items
        set(items) {
            this.items = items
            initItems()
        }

    constructor(context: Context) : super(context, null, R.attr.carbon_menuStripStyle) {
        initMenuStrip(null, R.attr.carbon_menuStripStyle)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.carbon_menuStripStyle) {
        initMenuStrip(attrs, R.attr.carbon_menuStripStyle)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initMenuStrip(attrs, defStyleAttr)
    }

    private fun initMenuStrip(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MenuStrip, defStyleAttr, R.style.carbon_MenuStrip)

        orientation = carbon.view.Orientation.values()[a.getInt(R.styleable.MenuStrip_android_orientation, carbon.view.Orientation.VERTICAL.ordinal)]
        itemLayoutId = a.getResourceId(R.styleable.MenuStrip_carbon_itemLayout, R.layout.carbon_menustrip_item)
        separatorLayoutId = a.getResourceId(R.styleable.MenuStrip_carbon_separatorLayout, 0)
        val menuId = a.getResourceId(R.styleable.MenuStrip_carbon_menu, 0)
        if (menuId != 0)
            setMenu(menuId)

        a.recycle()
    }

    fun setMenu(resId: Int) {
        setMenu(Carbon.getMenu(context, resId))
    }

    fun setMenu(menu: Menu) {
        items = (0 until menu.size()).map { Item(menu.getItem(it)) }.toTypedArray()
        initItems()
    }

    fun setItemLayout(itemLayoutId: Int) {
        this.itemLayoutId = itemLayoutId
        initItems()
    }

    fun setSeparatorLayout(separatorLayoutId: Int) {
        this.separatorLayoutId = separatorLayoutId
        initItems()
    }

    fun setOnItemClickedListener(onItemClickedListener: OnItemClickedListener<Item>) {
        this.onItemClickedListener = onItemClickedListener
        initItems()
    }

    private fun initItems() {
        if (items == null)
            return

        initAdapter()

        val items: ArrayList<Serializable> = arrayListOf(*this.items!!)
        var i = 0
        while (i < items.size - 1) {
            if ((items[i] as Item).groupId != (items[i + 1] as Item).groupId)
                items.add(++i, DividerItem())
            i++
        }
        adapter.items = items
    }

    private fun initAdapter() {
        val vertical = orientation == carbon.view.Orientation.VERTICAL

        var separatorLayoutId = this.separatorLayoutId
        if (separatorLayoutId == 0)
            separatorLayoutId = if (vertical) R.layout.carbon_menustrip_hseparator_item else R.layout.carbon_menustrip_vseparator_item

        layoutManager = LinearLayoutManager(context, if (vertical) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL, false)
        adapter = RowListAdapter(Item::class.java, RowFactory<Item> { DataBindingComponent<Item>(it, itemLayoutId) })
        adapter.addFactory(DividerItem::class.java, { SeparatorComponent(it, separatorLayoutId) })

        adapter.setOnItemClickedListener(Item::class.java, { view, item, position ->
            onItemClickedListener?.onItemClicked(view, item, position)
        })

        setAdapter(adapter)
    }
}
