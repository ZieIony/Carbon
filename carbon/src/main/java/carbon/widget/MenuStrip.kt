package carbon.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.view.MenuItemCompat
import carbon.Carbon
import carbon.R
import carbon.component.LayoutComponent
import carbon.databinding.CarbonMenustripItemBinding
import carbon.databinding.CarbonMenustripItemCheckableBinding
import carbon.databinding.CarbonMenustripToolsItemBinding
import carbon.databinding.CarbonMenustripToolsItemCheckableBinding
import carbon.drawable.CheckedState
import carbon.drawable.ColorStateListFactory
import carbon.recycler.DividerItemDecoration
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
import carbon.view.SelectionMode
import java.io.Serializable


open class MenuStrip : RecyclerView {

    open class Item : Serializable {
        var id = 0
        var icon: Drawable? = null
        var title: CharSequence? = null
        var iconTintList: ColorStateList? = null
        var groupId = 0
        var isEnabled = true
        var isVisible = true

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
            isEnabled = menuItem.isEnabled
            isVisible = menuItem.isVisible
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Item

            if (id != other.id) return false
            if (title != other.title) return false
            if (groupId != other.groupId) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + (title?.hashCode() ?: 0)
            result = 31 * result + groupId
            return result
        }
    }

    open class CheckableItem : Item {
        var isChecked: Boolean = false

        constructor()

        constructor(id: Int, icon: Drawable, text: CharSequence) : super(id, icon, text)

        constructor(menuItem: MenuItem) : super(menuItem) {
            isChecked = menuItem.isChecked
        }
    }

    class ItemComponent(val parent: ViewGroup) : LayoutComponent<Item>(parent, R.layout.carbon_menustrip_item) {
        private val binding = CarbonMenustripItemBinding.bind(view)

        override fun bind(data: Item) {
            with(binding) {
                root.id = data.id
                root.isEnabled = data.isEnabled
                carbonIcon.setImageDrawable(data.icon)
                carbonIcon.setTintList(data.iconTintList
                        ?: ColorStateListFactory.makeIcon(parent.context))
                carbonText.text = data.title
                carbonText.textColor = data.iconTintList
                        ?: ColorStateListFactory.makePrimaryText(parent.context)
            }
        }
    }

    class CheckableItemComponent(val parent: ViewGroup) : LayoutComponent<CheckableItem>(parent, R.layout.carbon_menustrip_item_checkable) {
        private val binding = CarbonMenustripItemCheckableBinding.bind(view)

        override fun bind(data: CheckableItem) {
            with(binding) {
                root.id = data.id
                root.isEnabled = data.isEnabled
                carbonCheckBox.isChecked = data.isChecked
                carbonCheckBox.setTintList(data.iconTintList
                        ?: ColorStateListFactory.makeIcon(parent.context))
                carbonCheckBox.text = data.title.toString()
                carbonCheckBox.setTextColor(data.iconTintList
                        ?: ColorStateListFactory.makePrimaryText(parent.context))
                carbonCheckBox.setOnCheckedChangeListener { _, isChecked -> data.isChecked = isChecked == CheckedState.CHECKED }
            }
        }
    }

    class ToolItemComponent(val parent: ViewGroup) : LayoutComponent<Item>(parent, R.layout.carbon_menustrip_tools_item) {
        private val binding = CarbonMenustripToolsItemBinding.bind(view)

        override fun bind(data: Item) {
            with(binding) {
                root.id = data.id
                root.isEnabled = data.isEnabled
                try {
                    root.tooltipText = data.title
                } catch (e: java.lang.Exception) {
                }
                carbonIcon.setImageDrawable(data.icon)
                carbonIcon.setTintList(data.iconTintList
                        ?: ColorStateListFactory.makeIcon(parent.context))
            }
        }
    }

    class CheckableToolItemComponent(val parent: ViewGroup) : LayoutComponent<CheckableItem>(parent, R.layout.carbon_menustrip_tools_item_checkable) {
        private val binding = CarbonMenustripToolsItemCheckableBinding.bind(view)

        override fun bind(data: CheckableItem) {
            with(binding) {
                root.id = data.id
                root.isEnabled = data.isEnabled
                try {
                    root.tooltipText = data.title
                } catch (e: java.lang.Exception) {
                }
                carbonCheckBox.isChecked = data.isChecked
                carbonCheckBox.setTintList(data.iconTintList
                        ?: ColorStateListFactory.makeIcon(parent.context))
                carbonCheckBox.setOnCheckedChangeListener { _, isChecked -> data.isChecked = isChecked == CheckedState.CHECKED }
            }
        }
    }

    @Deprecated("Use itemFactory instead")
    var itemLayoutId: Int = 0

    open fun <ItemType : Item> putFactory(type: Class<ItemType>, factory: RowFactory<ItemType>) {
        adapter.putFactory(type, factory)
    }

    private lateinit var _itemFactory: RowFactory<Item>
    var itemFactory: RowFactory<Item>
        get() = _itemFactory
        set(value) {
            adapter.putFactory(Item::class.java, value)
            _itemFactory = value
        }

    private lateinit var _checkableItemFactory: RowFactory<CheckableItem>
    var checkableItemFactory: RowFactory<CheckableItem>
        get() = _checkableItemFactory
        set(value) {
            adapter.putFactory(CheckableItem::class.java, value)
            _checkableItemFactory = value
        }

    private lateinit var _orientation: carbon.view.Orientation
    var orientation: carbon.view.Orientation
        get() = _orientation
        set(value) {
            _orientation = value
            initItems()
        }

    var adapter: RowArrayAdapter<Item> = RowArrayAdapter()
    var selectionMode: SelectionMode
        get() = adapter.selectionMode
        set(value) {
            adapter.selectionMode = value
        }

    private var _onItemClickedListener: OnItemClickedListener<Item>? = null
    var onItemClickedListener
        get() = _onItemClickedListener
        set(value) {
            _onItemClickedListener = value
            initItems()
        }

    private var items: Array<Item>? = null
    var menuItems: Array<Item>?
        get() = items
        set(value) {
            this.items = value
            initItems()
        }

    var selectedItems: List<Item>
        get() = adapter.selectedItems
        set(value) {
            adapter.selectedItems = value
            initItems()
        }

    var selectedIndices: List<Int>
        get() = adapter.selectedIndices
        set(value) {
            adapter.selectedIndices = value
            initItems()
        }

    constructor(context: Context) : super(context, null, R.attr.carbon_menuStripStyle) {
        initMenuStrip(null, R.attr.carbon_menuStripStyle)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, R.attr.carbon_menuStripStyle) {
        initMenuStrip(attrs, R.attr.carbon_menuStripStyle)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initMenuStrip(attrs, defStyleAttr)
    }

    private fun initMenuStrip(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MenuStrip, defStyleAttr, R.style.carbon_MenuStrip)

        orientation = carbon.view.Orientation.values()[a.getInt(R.styleable.MenuStrip_android_orientation, carbon.view.Orientation.VERTICAL.ordinal)]
        checkableItemFactory = RowFactory { CheckableItemComponent(this) }
        itemFactory = RowFactory { ItemComponent(this) }
        selectionMode = SelectionMode.values()[a.getInt(R.styleable.MenuStrip_carbon_selectionMode, SelectionMode.NONE.ordinal)]
        val menuId = a.getResourceId(R.styleable.MenuStrip_carbon_menu, 0)
        if (menuId != 0)
            setMenu(menuId)

        a.recycle()
    }

    fun setMenu(resId: Int) = setMenu(Carbon.getMenu(context, resId))

    fun setMenu(menu: Menu) {
        items = (0 until menu.size()).map {
            val item = menu.getItem(it)
            if (item.isCheckable || item.isChecked) CheckableItem(item) else Item(item)
        }.toTypedArray()
        initItems()
    }

    fun getItem(id: Int) = items?.find { it.id == id }

    private fun initItems() {
        items?.let { items ->
            initAdapter()
            adapter.items = items.filter { it.isVisible }.toTypedArray()
        }
    }

    private fun initAdapter() {
        val vertical = orientation == carbon.view.Orientation.VERTICAL

        layoutManager = LinearLayoutManager(context, if (vertical) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL, false, Gravity.CENTER)

        adapter.setOnItemClickedListener(onItemClickedListener)

        setAdapter(adapter)
    }

    fun refresh() {
        adapter.items = adapter.items
    }

    override fun setDivider(divider: Drawable?, height: Int): DividerItemDecoration? {
        val decoration = super.setDivider(divider, height)
        decoration.setDrawBefore { i ->
            val items = this.items
            items != null && i > 0 && (items[i].groupId != items[i - 1].groupId)
        }
        return decoration
    }

    public override fun onSaveInstanceState(): Parcelable? {
        //begin boilerplate code that allows parent classes to save state
        val superState = super.onSaveInstanceState()

        val ss = SavedState(superState)
        //end

        ss.selectedIndices = ArrayList(selectedIndices)

        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        //begin boilerplate code so parent classes can restore state
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)
        //end

        selectedIndices = state.selectedIndices
    }

    private open class SavedState : Parcelable {

        lateinit var selectedIndices: ArrayList<Int>

        var superState: Parcelable? = null

        constructor() {
            superState = null
        }

        constructor(superState: Parcelable?) {
            this.superState = if (superState !== EMPTY_STATE) superState else null
        }

        private constructor(parcel: Parcel) {
            val superState = parcel.readParcelable<Parcelable>(BottomNavigationView::class.java.classLoader)
            this.superState = superState ?: EMPTY_STATE
            this.selectedIndices = parcel.readSerializable() as ArrayList<Int>
        }

        override fun describeContents(): Int = 0

        override fun writeToParcel(out: Parcel, flags: Int) {
            out.writeParcelable(superState, flags)
            out.writeSerializable(this.selectedIndices)
        }

        companion object {
            val EMPTY_STATE: SavedState = object : MenuStrip.SavedState() {
            }

            //required field that makes Parcelables from a Parcel
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

}
